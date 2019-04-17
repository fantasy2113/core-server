package com.orbis.coreserver.base.entities;

import com.orbis.coreserver.base.entities.beans.AddressBean;

public class CustomerAddress extends AbstractEntity {

    private int customerId;
    private String type;
    private AddressBean address;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AddressBean getAddress() {
        return address;
    }

    public void setAddress(AddressBean address) {
        this.address = address;
    }

}
