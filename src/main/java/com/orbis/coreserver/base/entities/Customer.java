package com.orbis.coreserver.base.entities;

import com.orbis.coreserver.base.entities.beans.AddressBean;
import com.orbis.coreserver.base.entities.beans.PersonBean;

public class Customer extends AbstractEntity {

    private boolean commercial;
    private String companyName;
    private AddressBean address;
    private PersonBean person;

    public boolean isCommercial() {
        return commercial;
    }

    public void setCommercial(boolean commercial) {
        this.commercial = commercial;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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
