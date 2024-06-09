package com._7aske.grain.data.dsl;

import com._7aske.grain.data.dsl.token.FieldToken;
import com._7aske.grain.data.dsl.token.OperationToken;
import com._7aske.grain.data.dsl.token.Token;
import com._7aske.grain.util.StringUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class DslLexer {
    public LexingResult lex(String dsl) {
        LexingResult lexingResult = new LexingResult();
        for (RootOperation rootOperation : RootOperation.values()) {
            if (dsl.startsWith(rootOperation.getRepr())) {
                lexingResult.setRootOperation(rootOperation);
                dsl = dsl.substring(rootOperation.getRepr().length());
                break;
            }
        }

        List<Token> tokens = new ArrayList<>();

        while (!dsl.isEmpty()) {
            StringBuilder token = new StringBuilder();
            do {
                token.append(dsl.charAt(0));
                dsl = dsl.substring(1);

                for (Operation operation : Operation.values()) {
                    if (token.toString().endsWith(operation.getRepr())) {
                        String field = StringUtils.uncapitalize(token.substring(0, token.length() - operation.getRepr().length()));
                        token.setLength(0);

                        if (!field.isEmpty()) {
                            tokens.add(new FieldToken(field));
                        }

                        tokens.add(new OperationToken(operation));
                    }
                }
            } while (!dsl.isBlank());

            if (!token.isEmpty()) {
                tokens.add(new FieldToken(StringUtils.uncapitalize(token.toString())));
            }
        }

        lexingResult.setTokens(new ArrayDeque<>(tokens));

        return lexingResult;
    }
}
