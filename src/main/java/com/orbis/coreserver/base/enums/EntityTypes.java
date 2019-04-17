package com.orbis.coreserver.base.enums;

import java.util.Locale;

public enum EntityTypes {
    USER_RIGHT, USER, CUSTOMER_ADDRESS, CUSTOMER_CONTACT, CUSTOMER;

    @Override
    public String toString() {
        return name().toLowerCase(Locale.ENGLISH);
    }
}
