package com._7aske.grain.data.dsl;

import com._7aske.grain.data.dsl.ast.FieldNode;
import com._7aske.grain.data.dsl.token.FieldToken;
import com._7aske.grain.data.dsl.token.OperationToken;
import com._7aske.grain.data.dsl.token.Token;
import com._7aske.grain.data.meta.EntityInformation;
import com._7aske.grain.util.StringUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class DslLexer {
    private final EntityInformation entityInformation;

    public DslLexer(EntityInformation entityInformation) {
        this.entityInformation = entityInformation;
    }


    public LexingResult lex(String dsl) {
        LexingResult lexingResult = new LexingResult();
        for (RootOperation rootOperation : RootOperation.values()) {
            if (dsl.startsWith(rootOperation.getRepr())) {
                lexingResult.setRootOperation(rootOperation);
                dsl = dsl.substring(rootOperation.getRepr().length());
                break;
            }
        }

        Deque<Token> tokens = new ArrayDeque<>();

        while (!dsl.isEmpty()) {
            StringBuilder token = new StringBuilder();
            do {
                token.append(dsl.charAt(0));
                dsl = dsl.substring(1);

                String asOperation = token.toString();
                String asField = StringUtils.uncapitalize(asOperation);

                if (entityInformation.hasField(tokens.peekLast() instanceof FieldToken fieldtoken ? fieldtoken.getField() : null, asField)) {
                    tokens.add(new FieldToken(asField));
                    token.setLength(0);
                } else {
                    for (Operation operation : Operation.values()) {
                        if (asOperation.equals(operation.getRepr())) {
                            tokens.add(new OperationToken(operation));
                            token.setLength(0);
                            break;
                        }
                    }
                }

            } while (!dsl.isBlank());

            if (!token.isEmpty()) {
                tokens.add(new FieldToken(StringUtils.uncapitalize(token.toString())));
            }
        }

        lexingResult.setTokens(tokens);

        return lexingResult;
    }
}
