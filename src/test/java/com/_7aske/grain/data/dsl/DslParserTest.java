package com._7aske.grain.data.dsl;

import com._7aske.grain.data.dsl.ast.Node;
import com._7aske.grain.data.meta.EntityField;
import com._7aske.grain.data.meta.EntityInformation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DslParserTest {

    @Test
    void testTokenize() {
//        String methodName = "findByNameLikeAndAgeNotGreaterThan";
        String methodName = "findByNameAndAgeNot";

        EntityInformation entityInformation = new EntityInformation(Object.class);
        entityInformation.setEntityFields(List.of(
                new EntityField("name", "name", String.class),
                new EntityField("age", "age", Integer.class)
        ));

        DslParser parser = new DslParser(entityInformation, new Object[]{"John", 20});
        ParsingResult parsingResult = parser.parse(methodName);

        System.out.println(parsingResult);
        Node tree = parsingResult.getTree();

        assertEquals(RootOperation.FIND_BY, parsingResult.getRootOperation());
        assertNotNull(tree);
    }

}