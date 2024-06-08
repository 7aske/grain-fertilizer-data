package com._7aske.grain.data.dsl;

import com._7aske.grain.data.dsl.token.Token;
import com._7aske.grain.data.meta.EntityField;
import com._7aske.grain.data.meta.EntityInformation;
import com._7aske.grain.web.page.Pageable;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DslParserTest {

    @Test
    void testTokenize() {
        String methodName = "findByNameLikeAndAgeNotGreaterThan";

        EntityInformation entityInformation = new EntityInformation(Object.class);
        entityInformation.setEntityFields(List.of(
                new EntityField("name", "name", String.class),
                new EntityField("age", "age", Integer.class)
        ));

        DslParser parser = new DslParser();
        List<Token> tokens = parser.parse(methodName, entityInformation, new Object[]{"John", 20});

        System.out.println(tokens);

        assertEquals(2, tokens.size());
    }

}