package de.josmer.coreserver.repositories;

import de.josmer.coreserver.base.Toolbox;
import de.josmer.coreserver.base.entities.User;
import de.josmer.coreserver.base.entities.UserRight;
import de.josmer.coreserver.base.entities.beans.RightBean;
import de.josmer.coreserver.base.entities.beans.RightsBean;
import de.josmer.coreserver.base.enums.EntityTypes;
import de.josmer.coreserver.base.enums.Rights;
import de.josmer.coreserver.base.interfaces.connector.Columns;
import de.josmer.coreserver.base.interfaces.connector.ConnectorResponse;
import de.josmer.coreserver.base.interfaces.connector.IConnector;
import de.josmer.coreserver.base.interfaces.repositories.IUserRightRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class UserRightRepository extends Repository<UserRight> implements IUserRightRepository {
    private static final Logger LOGGER = LogManager.getLogger(UserRightRepository.class.getName());

    @Inject
    public UserRightRepository(final IConnector connector) {
        super(connector, EntityTypes.USER_RIGHT, UserRight.class);
    }

    @Override
    public final Optional<UserRight> get(final int userId, final String api, final Rights role) {
        final String statement = SELECT_FROM
                + getTableName() + WHERE + Columns.USER_ID
                + EQ + userId + AND + Columns.SERVICE
                + EQ + APO + api + APO
                + AND + Columns.SERVICE_RIGHT + EQ + APO
                + role.toString() + APO + LIMIT_1 + END;

        return Optional.ofNullable(entity(statement));
    }

    @Override
    public RightsBean getRights(final User user) {
        RightsBean rightsBean = new RightsBean();
        try {
            rightsBean.setUserId(user.getId());
            rightsBean.setLogin(user.getUserEmail());
            List<RightBean> rights = new ArrayList<>();

            getAll(user.getId()).forEach(r -> {
                RightBean rightBean = new RightBean();
                rightBean.setService(r.getService());
                rightBean.setServiceRight(r.getServiceRight());
                rights.add(rightBean);
            });

            rightsBean.setRights(rights);
        } catch (Exception ex) {
            LOGGER.info(ex.getMessage());
        }
        return rightsBean;
    }

    @Override
    public int updateRights(RightsBean rightsBean) {
        int updates = 0;
        try {
            for (RightBean rightBean : rightsBean.getRights()) {
                if (!get(rightsBean.getUserId(), rightBean.getService(), Toolbox.right(rightBean.getServiceRight())).isPresent()) {
                    UserRight userRight = new UserRight();
                    userRight.setUserId(rightsBean.getUserId());
                    userRight.setService(rightBean.getService());
                    userRight.setServiceRight(rightBean.getServiceRight());
                    updates += save(userRight).getUpdates();
                }
            }
        } catch (Exception ex) {
            LOGGER.info(ex.getMessage());
        }
        return updates;
    }

    @Override
    public final ConnectorResponse save(final UserRight entity) {
        final String statement = INSERT_INTO + getTableName()
                + OPEN
                + Columns.USER_ID + COL
                + Columns.SERVICE + COL
                + Columns.CREATED + COL
                + Columns.MODIFIED + COL
                + Columns.SERVICE_RIGHT
                + CLOSE
                + VALUES
                + OPEN
                + APO + entity.getUserId() + APO + COL
                + APO + entity.getService() + APO + COL
                + APO + Toolbox.dateTimeNow() + APO + COL
                + APO + Toolbox.dateTimeNow() + APO + COL
                + APO + entity.getServiceRight() + APO
                + CLOSE + END;

        return execute(statement);
    }

    @Override
    public final ConnectorResponse update(final UserRight entity) {
        final String statement = UPDATE + getTableName() + SET
                + Columns.USER_ID + EQ + APO + entity.getUserId() + APO + COL
                + Columns.SERVICE + EQ + APO + entity.getService() + APO + COL
                + Columns.MODIFIED + EQ + APO + Toolbox.dateTimeNow() + APO + COL
                + Columns.SERVICE_RIGHT + EQ + APO + entity.getServiceRight() + APO
                + getEqId(entity.getId());

        return execute(statement);
    }

    @Override
    public final List<UserRight> getAll(final int foreignKey) {
        return all(foreignKey, Columns.USER_ID);
    }
}
