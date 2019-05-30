package de.josmer.coreserver.base.utils;

import de.josmer.coreserver.base.Toolbox;
import de.josmer.coreserver.base.enums.Rights;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ToolboxTest {

    @Test
    public void testIsNull() {
        assertTrue(Toolbox.isNull(null));
        assertFalse(Toolbox.isNull(""));
    }

    @Test
    public void testIsNotNull() {
        assertTrue(Toolbox.isNotNull(""));
        assertFalse(Toolbox.isNotNull(null));
    }

    @Test
    public void testIsEmptyOrNull() {
        assertTrue(Toolbox.isEmptyOrNull(""));
        assertTrue(Toolbox.isEmptyOrNull(null));
        assertFalse(Toolbox.isEmptyOrNull(" "));
    }

    @Test
    public void testGetRandomString() {
        for (int i = 1; i < 11; i++) {
            assertTrue(Toolbox.getRandomString(i, false, false).length() == i);
        }
    }

    @Test
    public void testGetRights() {
        List<String> rights = Toolbox.rights();
        assertTrue(Rights.values().length == rights.size());
    }

    @Test
    public void testGetRight() {
        assertTrue(Toolbox.right("") == null);
    }
}
