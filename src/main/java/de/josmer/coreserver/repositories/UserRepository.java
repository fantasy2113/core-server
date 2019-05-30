package de.josmer.coreserver.repositories;

import de.josmer.coreserver.base.Toolbox;
import de.josmer.coreserver.base.entities.User;
import de.josmer.coreserver.base.enums.EntityTypes;
import com.orbis.coreserver.base.interfaces.connector.*;
import de.josmer.coreserver.base.interfaces.repositories.IUserRepository;
import de.josmer.coreserver.base.interfaces.connector.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import javax.inject.Inject;
import java.util.Locale;
import java.util.Optional;

public final class UserRepository extends Repository<User> implements IUserRepository {
    private static final Logger LOGGER = LogManager.getLogger(UserRepository.class.getName()); //NOSONAR
    private final IUserConnector userConnector;

    @Inject
    public UserRepository(final IConnector connector, final IUserConnector userConnector) {
        super(connector, EntityTypes.USER, User.class);
        this.userConnector = userConnector;
    }

    public static String hashPassword(final String plainTextPassword) {
        if (Toolbox.isNull(plainTextPassword)) {
            return BCrypt.hashpw("Orbis123!_", BCrypt.gensalt());
        }
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    @Override
    public final Optional<User> get(final String userEmail) {
        final String statement = SELECT_FROM + getTableName()
                + WHERE + Columns.EMAIL + EQ
                + APO + userEmail + APO
                + AND + Columns.ACTIVE + EQ + APO + 1 + APO
                + LIMIT_1 + END;

        return Optional.ofNullable(entity(statement));
    }

    @Override
    public final String hashedPassword(final int id) {
        final String statement = SELECT_FROM_PW + getTableName()
                + WHERE + Columns.ID + EQ
                + id + LIMIT_1 + END;

        return userConnector.hashedPassword(new Query<>(getEntityTyp(), getClassTyp(), statement));
    }

    @Override
    public final ConnectorResponse updatePassword(final int userId, final String plainPassword) {
        Optional<User> user = get(userId);
        if (!user.isPresent() || !isNewPasswordValid(user.get().getUserEmail(), plainPassword)) {
            return new ConnectorResponse();
        }

        final String statement = UPDATE + getTableName() + SET
                + Columns.PW + EQ + APO + hashPassword(plainPassword) + APO
                + getEqId(userId);

        return execute(statement);
    }

    @Override
    public ConnectorResponse updateLastLogin(final int userId, final String dateTimeNow) {
        final String statement = UPDATE + getTableName() + SET
                + Columns.LAST_LOGIN + EQ + APO + Toolbox.dateTimeNow() + APO
                + getEqId(userId);

        return execute(statement);
    }


    @Override
    public ConnectorResponse newPassword(final int userId, final String plainPassword) {
        return updatePassword(userId, plainPassword);
    }

    @Override
    public final ConnectorResponse save(final User entity) {
        if (!isNewPasswordValid(entity.getUserEmail(), entity.getPw())) {
            return new ConnectorResponse();
        }

        final String statement = INSERT_INTO + getTableName()
                + OPEN
                + Columns.EMAIL + COL
                + Columns.CREATED + COL
                + Columns.MODIFIED + COL
                + Columns.PW
                + CLOSE
                + VALUES
                + OPEN
                + APO + entity.getUserEmail() + APO + COL
                + APO + Toolbox.dateTimeNow() + APO + COL
                + APO + Toolbox.dateTimeNow() + APO + COL
                + APO + hashPassword(entity.getPw()) + APO
                + CLOSE + END;

        return execute(statement);
    }

    @Override
    public final ConnectorResponse update(final User entity) {
        final String statement = UPDATE + getTableName() + SET
                + Columns.ACTIVE + EQ + APO + parseBoolean(entity.isActive()) + APO + COL
                + Columns.EMAIL + EQ + APO + entity.getUserEmail() + APO + COL
                + Columns.MODIFIED + EQ + APO + Toolbox.dateTimeNow() + APO
                + getEqId(entity.getId());

        return execute(statement);
    }

    private boolean isNewPasswordValid(final String userLogin, final String plainPassword) {
        if (Toolbox.isEmptyOrNull(userLogin) || Toolbox.isEmptyOrNull(plainPassword)) {
            LOGGER.info("Some parameter is null.");
            return false;
        }
        if (isPasswordLength(plainPassword)) {
            LOGGER.info("Password should be more than 8 characters in length.");
            return false;
        }
        if (isContainsUserLogin(plainPassword, userLogin)) {
            LOGGER.info("Password Should not be same as user name");
            return false;
        }
        if (!isContainsUpperCase(plainPassword)) {
            LOGGER.info("Password should contain atleast one upper case alphabet");
            return false;
        }
        if (!isContainsLowerCase(plainPassword)) {
            LOGGER.info("Password should contain atleast one lower case alphabet");
            return false;
        }
        if (!isContainsNumbers(plainPassword)) {
            LOGGER.info("Password should contain atleast one number.");
            return false;
        }
        if (!isContainsSpecialChar(plainPassword)) {
            LOGGER.info("Password should contain atleast one special character");
            return false;
        }
        LOGGER.info("Password is valid.");
        return true;
    }

    private boolean isContainsSpecialChar(final String plainPassword) {
        return plainPassword.matches("(.*[,~,!,@,#,$,%,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)");
    }

    private boolean isContainsNumbers(final String plainPassword) {
        return plainPassword.matches("(.*[0-9].*)");
    }

    private boolean isContainsLowerCase(final String plainPassword) {
        return plainPassword.matches("(.*[a-z].*)");
    }

    private boolean isContainsUpperCase(final String plainPassword) {
        return plainPassword.matches("(.*[A-Z].*)");
    }

    private boolean isContainsUserLogin(final String plainPassword, final String userLogin) {
        return plainPassword.toLowerCase(Locale.ENGLISH).contains(userLogin.toLowerCase(Locale.ENGLISH));
    }

    private boolean isPasswordLength(final String plainPassword) {
        return plainPassword.length() < 8;
    }
}
