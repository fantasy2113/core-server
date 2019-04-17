package com.orbis.coreserver.base.interfaces.repositories;

import com.orbis.coreserver.base.entities.User;
import com.orbis.coreserver.base.interfaces.connector.ConnectorResponse;

import java.util.Optional;

public interface IUserRepository extends IRepository<User> {

    Optional<User> get(String userEmail);

    String hashedPassword(int userId);

    ConnectorResponse newPassword(int userId, String plainPassword);

    ConnectorResponse updatePassword(int userId, String plainPassword);

    ConnectorResponse updateLastLogin(int userId, String dateTimeNow);
}
