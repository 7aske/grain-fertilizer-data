package com._7aske.grain.data.dsl;

import com._7aske.grain.data.dsl.token.*;
import com._7aske.grain.data.meta.EntityInformation;
import com._7aske.grain.util.StringUtils;

import java.util.*;

public class DslParser {
    public List<Token> parse(String dsl, EntityInformation entityInformation, Object[] args) {
        List<Token> tokens = new ArrayList<>();

        for (RootOperation rootOperation : RootOperation.values()) {
            if (dsl.startsWith(rootOperation.getRepr())) {
                tokens.add(new RootOperationToken(rootOperation));
                dsl = dsl.substring(rootOperation.getRepr().length());
                break;
            }
        }

        // NameAndAge
        Deque<Token> stack = new ArrayDeque<>();
        int argIndex = 0;
        while (!dsl.isEmpty()) {
            StringBuilder token = new StringBuilder();
            do {
                token.append(dsl.charAt(0));
                dsl = dsl.substring(1);
                for (Operation operation : Operation.values()) {
                    if (token.toString().endsWith(operation.getRepr())) {
                        String field = StringUtils.uncapitalize(token.substring(0, token.length() - operation.getRepr().length()));
                        token.setLength(0);
                        // left?
                        Token left = null;
                        if (!field.isEmpty()) {
                            if (!entityInformation.hasField(field)) {
                                throw new RuntimeException("Invalid field: " + field);
                            }
                            left = new FieldToken(field);
                        } else {
                            left = stack.pop();
                        }

                        Token current = new OperationToken(left, operation, null);
                        stack.push(current);
                    }
                }

                for (QueryOperation queryOperation : QueryOperation.values()) {
                    if (token.toString().endsWith(queryOperation.getRepr())) {
                        String field = StringUtils.uncapitalize(token.substring(0, token.length() - queryOperation.getRepr().length()));
                        token.setLength(0);

                        Token fieldToken = null;
                        if (!field.isEmpty()) {
                            if (!entityInformation.hasField(field)) {
                                throw new RuntimeException("Invalid field: " + field);
                            }
                            fieldToken = new FieldToken(field);
                        }

                        Token current = null;
                        if (fieldToken == null) {
                            current = stack.pop();
                            if (current instanceof OperationToken operationToken) {
                                if (operationToken.getRight() instanceof QueryOperationToken queryOperationToken) {
                                    queryOperationToken.setLeft(new QueryOperationToken(queryOperationToken.getLeft(), queryOperation, queryOperationToken.getRight()));
                                    queryOperationToken.setRight(null);
                                } else {
                                    throw new RuntimeException("Invalid query operation");
                                }
                            } else {
                                stack.push(current);
                            }
                        } else {
                            ValueToken valueToken = new ValueToken(args[argIndex++]);
                            current = new QueryOperationToken(fieldToken, queryOperation, valueToken);
                        }

                        if (!stack.isEmpty()) {
                            Token left = stack.pop();
                            if (left instanceof OperationToken leftOperationToken) {
                                leftOperationToken.setRight(current);
                                stack.push(leftOperationToken);
                            } else {
                                stack.push(left);
                            }
                        } else {
                            stack.push(current);
                        }
                    }
                }
            } while (!dsl.isBlank());

            if (!token.toString().isBlank()) {
                stack.push(new FieldToken(token.toString()));
            }
        }

        tokens.addAll(stack);

        return tokens;
    }
}
