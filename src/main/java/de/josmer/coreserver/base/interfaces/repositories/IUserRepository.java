package de.josmer.coreserver.base.interfaces.repositories;

import de.josmer.coreserver.base.entities.User;
import de.josmer.coreserver.base.interfaces.connector.ConnectorResponse;

import java.util.Optional;

public interface IUserRepository extends IRepository<User> {

    Optional<User> get(String userEmail);

    String hashedPassword(int userId);

    ConnectorResponse newPassword(int userId, String plainPassword);

    ConnectorResponse updatePassword(int userId, String plainPassword);

    ConnectorResponse updateLastLogin(int userId, String dateTimeNow);
}
