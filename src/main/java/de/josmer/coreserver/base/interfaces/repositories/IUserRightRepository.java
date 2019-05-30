package de.josmer.coreserver.base.interfaces.repositories;

import de.josmer.coreserver.base.entities.User;
import de.josmer.coreserver.base.entities.UserRight;
import de.josmer.coreserver.base.entities.beans.RightsBean;
import de.josmer.coreserver.base.enums.Rights;

import java.util.Optional;

public interface IUserRightRepository extends IRepository<UserRight>, IRepositoryFromForeignKey<UserRight> {

    Optional<UserRight> get(int userId, String api, Rights right);

    RightsBean getRights(User user);

    int updateRights(RightsBean rightsBean);

}
