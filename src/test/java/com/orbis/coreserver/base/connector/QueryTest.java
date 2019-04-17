package com.orbis.coreserver.base.connector;

import com.orbis.coreserver.base.entities.User;
import com.orbis.coreserver.base.enums.EntityTypes;
import com.orbis.coreserver.base.interfaces.connector.Query;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QueryTest {

    private Query<User> data;

    @Test
    public void testGetQuery() {
        data = new Query<>(EntityTypes.USER, User.class, "");
        assertEquals("", data.getStatement());
        data = new Query<>(EntityTypes.USER, User.class, "query");
        assertEquals("query", data.getStatement());
        data = new Query<>(EntityTypes.USER, User.class, " drop ");
        assertEquals("- empty -", data.getStatement());
        data = new Query<>(EntityTypes.USER, User.class, " truncate ");
        assertEquals("- empty -", data.getStatement());
    }

    @Test
    public void testGetEntity() {
        data = new Query<>(EntityTypes.USER, User.class, "");
        assertEquals(User.class, data.getClassTyp());
    }

    @Test
    public void testGetEntityType() {
        data = new Query<>(EntityTypes.USER, User.class, "");
        assertEquals(EntityTypes.USER, data.getEntityTyp());
    }

}
