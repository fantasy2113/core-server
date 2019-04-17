package com.orbis.coreserver.base.interfaces.repositories;

import com.orbis.coreserver.base.entities.User;
import com.orbis.coreserver.base.entities.UserRight;
import com.orbis.coreserver.base.entities.beans.RightsBean;
import com.orbis.coreserver.base.enums.Rights;

import java.util.Optional;

public interface IUserRightRepository extends IRepository<UserRight>, IRepositoryFromForeignKey<UserRight> {

    Optional<UserRight> get(int userId, String api, Rights right);

    RightsBean getRights(User user);

    int updateRights(RightsBean rightsBean);

}
