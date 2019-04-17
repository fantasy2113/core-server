package com.orbis.coreserver.api.security.crypting;

import com.orbis.coreserver.api.security.token.Token;
import com.orbis.coreserver.base.Authentication;
import com.orbis.coreserver.executors.TokenExecutor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CryptTest {

    @Test
    public void testCrypt() {
        for (int i = 0; i < 10; i++) {
            runTest();
        }
    }

    private void runTest() {
        int userId = 1;

        Token.init();

        String cleartextToken = Token.getCode(userId);
        String encodedToken = Token.get(userId);

        Authentication authentication = Token.getAuthentication(encodedToken);

        assertEquals(cleartextToken.replace(Token.getDelimiter() + userId, ""), authentication.getToken().get());
        assertEquals(userId, authentication.getUserId().getAsInt());

        new TokenExecutor().run();

        authentication = Token.getAuthentication(encodedToken);

        assertEquals(false, authentication.getToken().isPresent());
        assertEquals(false, authentication.getUserId().isPresent());

        cleartextToken = Token.getCode(userId);
        encodedToken = Token.get(userId);

        authentication = Token.getAuthentication(encodedToken);

        assertEquals(cleartextToken.replace(Token.getDelimiter() + userId, ""), authentication.getToken().get());
        assertEquals(userId, authentication.getUserId().getAsInt());
    }

}
