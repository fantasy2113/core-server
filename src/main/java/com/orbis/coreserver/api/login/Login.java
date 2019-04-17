package com.orbis.coreserver.api.login;

import com.orbis.coreserver.base.Toolbox;
import com.orbis.coreserver.base.entities.User;
import com.orbis.coreserver.base.interfaces.login.ILogin;
import com.orbis.coreserver.base.interfaces.repositories.IUserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import javax.inject.Inject;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.ConcurrentHashMap;

public final class Login implements ILogin {

    private static final Logger LOGGER = LogManager.getLogger(Login.class.getName());
    private static final Integer MAX_TRY = 10;
    private final IUserRepository userRepository;
    private final ConcurrentHashMap<String, Integer> failedLogin;

    @Inject
    public Login(IUserRepository userRepository) {
        this.userRepository = userRepository;
        this.failedLogin = new ConcurrentHashMap<>();
    }

    public static boolean isPassword(final String plainPassword, final String hashedPassword) {
        if (Toolbox.isNull(plainPassword) || Toolbox.isNull(hashedPassword) || hashedPassword.length() != 60) {
            return false;
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    @Override
    public int userAuthenticationFails(final String login) {
        if (failedLogin.containsKey(login)) {
            return failedLogin.get(login);
        }
        return 0;
    }

    private OptionalInt getUserId(final String login, final String plainPassword, User user) {
        if (isFailedLogin(login)) {
            return checkPassword(login, plainPassword, user);
        }
        deactivateUser(user);
        return OptionalInt.empty();
    }

    private OptionalInt checkPassword(final String login, final String plainPassword, User user) {
        if (isPassword(plainPassword, userRepository.hashedPassword(user.getId()))) {
            removeLogin(login);
            userRepository.updateLastLogin(user.getId(), Toolbox.dateTimeNow());
            LOGGER.info("login was successful for user: " + login);
            return OptionalInt.of(user.getId());
        }
        addLogin(login);
        return OptionalInt.empty();
    }

    private void deactivateUser(User user) {
        user.setIsActive(false);
        userRepository.update(user);
        LOGGER.info("login failed: deactivate user: " + user.getUserEmail());
    }

    private boolean isFailedLogin(final String login) {
        if (failedLogin.containsKey(login)) {
            return failedLogin.get(login) < MAX_TRY;
        }
        return true;
    }

    private void removeLogin(final String login) {
        LOGGER.info("login: remove failed login for user: " + login);
        failedLogin.remove(login);
    }

    private void addLogin(final String login) {
        LOGGER.info("login failed: wrong user password for user: " + login);
        failedLogin.computeIfPresent(login, (k, v) -> v + 1);
        failedLogin.putIfAbsent(login, 1);
    }

    @Override
    public final OptionalInt userAuthentication(final String login, final String plainPassword) {
        if (Toolbox.isNull(login) || Toolbox.isNull(plainPassword)) {
            LOGGER.info("login failed: parameter was null");
            return OptionalInt.empty();
        }
        Optional<User> optionalUser = userRepository.get(login);
        if (optionalUser.isPresent()) {
            return getUserId(login, plainPassword, optionalUser.get());
        }
        LOGGER.info("login failed: user-login was wrong");
        return OptionalInt.empty();
    }
}
