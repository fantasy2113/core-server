package com.orbis.coreserver.repositories;

import com.orbis.coreserver.base.entities.beans.AddressBean;
import com.orbis.coreserver.base.entities.beans.PersonBean;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import utils.ISetUp;
import utils.TestUtils;

import static org.junit.Assert.assertEquals;

public abstract class RepositoryTesting implements ISetUp {

    @BeforeClass
    public static void setUpClass() {
        clean();
    }

    @AfterClass
    public static void tearDownClass() {
        clean();
    }

    private static void clean() {
        TestUtils.setConfig();
        TestUtils.cleanTables();
    }

    @Before
    @Override
    public final void setUp() {
        clean();
    }

    protected void testPerson(PersonBean person) {
        assertEquals("Dr. med.", person.getTitle());
        assertEquals("Herr", person.getSalutation());
        assertEquals("Karsten", person.getFirstName());
        assertEquals("Müller", person.getLastName());
    }

    protected void updatePerson(PersonBean person) {
        person.setTitle("Dr. med._");
        person.setSalutation("Herr_");
        person.setFirstName("Karsten_");
        person.setLastName("Müller_");
    }

    protected void testPersonAfterUpdate(PersonBean person) {
        assertEquals("Dr. med._", person.getTitle());
        assertEquals("Herr_", person.getSalutation());
        assertEquals("Karsten_", person.getFirstName());
        assertEquals("Müller_", person.getLastName());
    }

    protected void testAddress(AddressBean address) {
        assertEquals("Super Str.", address.getStreetName());
        assertEquals("1", address.getStreetNr());
        assertEquals("20000", address.getZipCode());
        assertEquals("Oyten", address.getPlace());
        assertEquals("05-345-34", address.getTelephone());
        assertEquals("info@mail.de", address.getEmail());
        assertEquals("4324-5345-435345", address.getFax());
    }

    protected void updateAddress(AddressBean address) {
        address.setStreetName("Super Str._");
        address.setStreetNr("1_");
        address.setZipCode("20000_");
        address.setPlace("Oyten_");
        address.setTelephone("05-345-34_");
        address.setEmail("info@mail.de_");
        address.setFax("4324-5345-435345_");
    }

    protected void testAddressAfterUpdate(AddressBean address) {
        assertEquals("Super Str._", address.getStreetName());
        assertEquals("1_", address.getStreetNr());
        assertEquals("20000_", address.getZipCode());
        assertEquals("Oyten_", address.getPlace());
        assertEquals("05-345-34_", address.getTelephone());
        assertEquals("info@mail.de_", address.getEmail());
        assertEquals("4324-5345-435345_", address.getFax());
    }
}
