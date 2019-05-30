package de.josmer.coreserver.base.interfaces.connector;

import de.josmer.coreserver.base.entities.User;

public interface IUserConnector {

    <E extends User> String hashedPassword(final Query<E> query);
}
