package com._7aske.grain.data.dsl;

import com._7aske.grain.data.dsl.ast.Node;
import com._7aske.grain.data.meta.EntityField;
import com._7aske.grain.data.meta.EntityInformation;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.metamodel.EntityType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DslParserTest {

    @Test
    void testTokenize() {
        String methodName = "findByNameLikeAndAgeNotGreaterThan";
//        String methodName = "findByNameAndAgeNot";

        EntityInformation entityInformation = getEntityInformation();

        DslParser parser = new DslParser(entityInformation);
        ParsingResult parsingResult = parser.parse(methodName);

        System.out.println(parsingResult);
        Node tree = parsingResult.getTree();

        assertEquals(RootOperation.FIND_BY, parsingResult.getRootOperation());
        assertNotNull(tree);
    }

    @Test
    void testNotIsNull() {
        String methodName = "findByNameNotIsNull";
        EntityInformation entityInformation = getEntityInformation();

        DslParser parser = new DslParser(entityInformation);
        ParsingResult parsingResult = parser.parse(methodName);

        System.out.println(parsingResult);
        Node tree = parsingResult.getTree();

        assertEquals(RootOperation.FIND_BY, parsingResult.getRootOperation());
        assertNotNull(tree);
    }

    private EntityInformation getEntityInformation() {
        EntityInformation entityInformation = new EntityInformation(null);
        entityInformation.setEntityFields(List.of(
                new EntityField("name", "name", String.class, null),
                new EntityField("age", "age", Integer.class, null)
        ));
        return entityInformation;
    }
}