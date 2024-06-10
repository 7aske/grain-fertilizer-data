package com._7aske.grain.data.dsl;

import com._7aske.grain.data.dsl.token.FieldToken;
import com._7aske.grain.data.dsl.token.OperationToken;
import com._7aske.grain.data.meta.EntityField;
import com._7aske.grain.data.meta.EntityInformation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class DslLexerTest {

    @Test
    void lex() {
        EntityInformation entityInformation = new EntityInformation(null);
        entityInformation.setEntityFields(List.of(
                new EntityField("name", "name", String.class, null),
                new EntityField("age", "age", Integer.class, null)

        ));
        DslLexer lexer = new DslLexer(entityInformation);
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

    @Test
    void lexJoin() {
        EntityInformation entityInformation = new EntityInformation(null);
        entityInformation.setEntityFields(List.of(
                new EntityField("name", "name", String.class, null),
                new EntityField("age", "age", Integer.class, null)

        ));
        DslLexer lexer = new DslLexer(entityInformation);
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