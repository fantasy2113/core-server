package de.josmer.coreserver.base.entities;

import de.josmer.coreserver.base.entities.beans.AddressBean;
import de.josmer.coreserver.base.entities.beans.PersonBean;

public class CustomerContact extends AbstractEntity {

    private int customerId;
    private AddressBean address;
    private PersonBean person;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public AddressBean getAddress() {
        return address;
    }

    public void setAddress(AddressBean address) {
        this.address = address;
    }

    public PersonBean getPerson() {
        return person;
    }

    public void setPerson(PersonBean person) {
        this.person = person;
    }

}
