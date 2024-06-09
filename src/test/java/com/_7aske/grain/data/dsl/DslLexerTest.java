package com._7aske.grain.data.dsl;

import com._7aske.grain.data.dsl.token.FieldToken;
import com._7aske.grain.data.dsl.token.OperationToken;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class DslLexerTest {

    @Test
    void lex() {
        DslLexer lexer = new DslLexer();
        LexingResult lexingResult = lexer.lex("findAllByNameNotAndAgeLessThan");

        System.out.println(lexingResult);

        assertEquals(5, lexingResult.getTokens().size());
        assertEquals(RootOperation.FIND_ALL_BY, lexingResult.getRootOperation());
        assertInstanceOf(FieldToken.class, lexingResult.getTokens().pop());
        assertInstanceOf(OperationToken.class, lexingResult.getTokens().pop());
        assertInstanceOf(OperationToken.class, lexingResult.getTokens().pop());
        assertInstanceOf(FieldToken.class, lexingResult.getTokens().pop());
        assertInstanceOf(OperationToken.class, lexingResult.getTokens().pop());
    }
}