package com.orbis.coreserver.base.interfaces.login;

import java.util.OptionalInt;

public interface ILogin {

    OptionalInt userAuthentication(final String login, final String plainPassword);

    int userAuthenticationFails(String login);

}
