package com.orbis.coreserver.api.security.token;

import com.orbis.coreserver.base.entities.UserRight;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    @Test
    public void check() {
        assertFalse(Token.check(null));
        assertFalse(Token.check("dfgdfg"));
        Token.init();
        assertFalse(Token.check(null));
    }

    @Test
    public void isAccessDeniedWithoutUserRight() {
        Token.init();
        assertTrue(Token.isAccessDenied(""));
    }

    @Test
    public void isAccessDenied() {
        Token.init();
        String token = Token.getCode(0).replace(Token.getDelimiter() + 0, "");
        assertTrue(Token.isAccessDenied("", null));
        assertTrue(Token.isAccessDenied("", new UserRight()));
        assertTrue(Token.isAccessDenied(token, null));
    }
}
