package com._7aske.grain.data.dsl;

import com._7aske.grain.data.dsl.ast.*;
import com._7aske.grain.data.dsl.token.FieldToken;
import com._7aske.grain.data.dsl.token.OperationToken;
import com._7aske.grain.data.dsl.token.Token;
import com._7aske.grain.data.meta.EntityInformation;

import java.util.ArrayDeque;
import java.util.Deque;

import static com._7aske.grain.data.dsl.Operation.EQUALS;

public class DslParser {
    private int argIndex;
    private final EntityInformation entityInformation;
    private final Deque<Node> stack = new ArrayDeque<>();
    private Deque<Token> tokens;

    public DslParser(EntityInformation entityInformation) {
        this.entityInformation = entityInformation;
        this.argIndex = 0;
    }

    public ParsingResult parse(String dsl) {
        ParsingResult parsingResult = new ParsingResult();

        LexingResult lexingResult = new DslLexer(entityInformation).lex(dsl);
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
        Node node;
        if (token instanceof FieldToken fieldToken) {
            Node prev = stack.poll();
            if (prev instanceof NavigableNode navigableNode) {
                if (tokens.isEmpty() || tokens.peek() instanceof OperationToken operationToken && operationToken.operation().isLogical()) {
                    node = new OperationNode(new JoinNode(navigableNode, fieldToken.field()), EQUALS, new ValueNode(argIndex++));
                } else {
                    node = new JoinNode(navigableNode, fieldToken.field());
                }
            } else if (entityInformation.hasField(fieldToken.field())) {
                node = new FieldNode(fieldToken.field());
                if (tokens.isEmpty()) {
                    node = new OperationNode(node, EQUALS, new ValueNode(argIndex++));
                }
            } else {
                throw new IllegalArgumentException("Unknown field: " + fieldToken.field());
            }
        } else if (token instanceof OperationToken operationToken) {
            if (operationToken.operation().isLogical()) {
                node = new OperationNode(stack.poll(), operationToken.operation(), parseNode());
            } else if (operationToken.operation().isUnary()) {
                node = new OperationNode(stack.poll(), operationToken.operation(), null);
                if (!tokens.isEmpty()) {
                    stack.push(node);
                    node = parseNode();
                }
            } else if (operationToken.operation().isLiteral()) {
                node = new OperationNode(stack.poll(), operationToken.operation(), new ValueNode(argIndex++));
            } else {
                throw new IllegalArgumentException("Unknown operation: " + operationToken.operation());
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
