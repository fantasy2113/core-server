package com.orbis.coreserver.base.interfaces.connector;

import com.orbis.coreserver.base.entities.User;

public interface IUserConnector {

    <E extends User> String hashedPassword(final Query<E> query);
}
