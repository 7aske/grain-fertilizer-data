package com._7aske.grain.data.dsl;

import com._7aske.grain.data.dsl.ast.*;
import com._7aske.grain.data.dsl.token.BinaryToken;
import com._7aske.grain.data.dsl.token.FieldToken;
import com._7aske.grain.data.dsl.token.OperationToken;
import com._7aske.grain.data.dsl.token.Token;
import com._7aske.grain.data.meta.EntityInformation;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import static com._7aske.grain.data.dsl.Operation.*;

public class DslParser {
    private int argIndex;
    private final EntityInformation entityInformation;
    private final Object[] args;
    private final Deque<Node> stack = new ArrayDeque<>();
    private Deque<Token> tokens;

    public DslParser(EntityInformation entityInformation, Object[] args) {
        this.entityInformation = entityInformation;
        this.args = args;
        this.argIndex = 0;
    }


    public ParsingResult parse(String dsl) {
        ParsingResult parsingResult = new ParsingResult();

        LexingResult lexingResult = new DslLexer().lex(dsl);
        parsingResult.setRootOperation(lexingResult.getRootOperation());

        this.tokens = lexingResult.getTokens();

        while (!tokens.isEmpty()) {
            Node current = createNode(tokens.poll());
            stack.push(current);
        }

        parsingResult.setTree(stack.poll());

        return parsingResult;
    }

    private Node parseNode() {
        Node node = createNode(tokens.poll());
        if ((node instanceof FieldNode || node instanceof ValueNode) && !tokens.isEmpty()) {
            stack.push(node);
            node = parseNode();
        }

        return node;
    }

    private Node createNode(Token token) {
        Node node = null;
        if (token instanceof FieldToken fieldToken) {
            if (!entityInformation.hasField(fieldToken.getField())) {
                throw new IllegalArgumentException("Field not found: " + fieldToken.getField());
            }
            node = new FieldNode(fieldToken.getField());
            if (tokens.isEmpty()) {
                node = new OperationNode(node, EQUALS, new ValueNode(args[argIndex++]));
            }
        } else if (token instanceof OperationToken operationToken) {
            if (operationToken.getOperation().isLogical()) {
                node = new OperationNode(stack.poll(), operationToken.getOperation(), parseNode());
            } else if (operationToken.getOperation().isUnary()) {
                node = new OperationNode(stack.poll(), operationToken.getOperation(), null);
                if (!tokens.isEmpty()) {
                    stack.push(node);
                    node = parseNode();
                } else {
                    Node field = new OperationNode(((BinaryNode) node).getLeft(), EQUALS, new ValueNode(args[argIndex++]));
                    ((BinaryNode) node).setLeft(field);
                }
            } else if (operationToken.getOperation().isLiteral()) {
                node = new OperationNode(stack.poll(), operationToken.getOperation(), new ValueNode(args[argIndex++]));
            } else {
                throw new IllegalArgumentException("Unknown operation: " + operationToken.getOperation());
            }
        } else {
            throw new IllegalArgumentException("Unknown token: " + token);
        }

        if (node instanceof OperationNode operationNode) {
            node = fixPrecedenceRight(operationNode);
        }

        return node;
    }

    private Node fixPrecedenceRight(OperationNode node) {
        if (node.getRight() instanceof OperationNode rightOperationNode) {
            if (rightOperationNode.getOperation().getPrecedence() < node.getOperation().getPrecedence()) {
                node.setRight(rightOperationNode.getLeft());
                rightOperationNode.setLeft(node);
                return rightOperationNode;
            }
        }

        if (node.getLeft() instanceof OperationNode leftOperationNode) {
            if (leftOperationNode.getOperation().getPrecedence() < node.getOperation().getPrecedence()) {
                node.setLeft(leftOperationNode.getLeft());
                leftOperationNode.setLeft(node);
                return leftOperationNode;
            }
        }

        return node;
    }
}
