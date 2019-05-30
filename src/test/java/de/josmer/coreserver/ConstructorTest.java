package de.josmer.coreserver;

import de.josmer.coreserver.api.security.crypting.GenKey;
import de.josmer.coreserver.api.security.token.Token;
import de.josmer.coreserver.base.Toolbox;
import de.josmer.coreserver.base.interfaces.connector.Columns;
import org.junit.Test;
import utils.Factory;
import utils.TestFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class ConstructorTest {

    private static final Logger LOGGER = Logger.getLogger(ConstructorTest.class.getName());

    @Test
    public void testAppUtils() {
        test(Toolbox.class);
    }


    @Test
    public void testToken() {
        test(Token.class);
    }

    @Test
    public void testGenKey() {
        test(GenKey.class);
    }

    @Test
    public void testAppFactory() {
        test(TestFactory.class);
    }

    @Test
    public void testFactory() {
        test(Factory.class);
    }

    @Test
    public void testColumns() {
        test(Columns.class);
    }

    @Test
    public void testString() {
        try {
            Constructor<String> constructor = String.class.getDeclaredConstructor();
            assertFalse(Modifier.isPrivate(constructor.getModifiers()));
        } catch (NoSuchMethodException ex) {
            LOGGER.info(ex.getMessage());
            fail();
        }
    }

    public <E> void test(Class<E> clazz) {
        try {
            Constructor<E> constructor = clazz.getDeclaredConstructor();
            assertTrue("Constructor is not private", Modifier.isPrivate(constructor.getModifiers()));
            constructor.setAccessible(true);
            constructor.newInstance();
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException ex) {
            LOGGER.info(ex.getMessage());
            fail();
        }
    }
}
