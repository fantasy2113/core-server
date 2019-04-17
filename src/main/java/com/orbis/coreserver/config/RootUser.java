package com.orbis.coreserver.config;

import com.orbis.coreserver.base.Toolbox;
import com.orbis.coreserver.base.entities.User;
import com.orbis.coreserver.base.entities.UserRight;
import com.orbis.coreserver.base.interfaces.repositories.IUserRepository;
import com.orbis.coreserver.base.interfaces.repositories.IUserRightRepository;
import com.orbis.coreserver.connector.MySqlConnector;
import com.orbis.coreserver.connector.UserConnector;
import com.orbis.coreserver.connector.mapper.Mapper;
import com.orbis.coreserver.repositories.UserRepository;
import com.orbis.coreserver.repositories.UserRightRepository;

import java.util.Arrays;
import java.util.List;

public final class RootUser {

    private static final String ROOT_EMAIL = "root";
    private final List<String> services;
    private final IUserRepository userRepository;

    public RootUser(String... services) {
        this.services = Arrays.asList(services);
        this.userRepository = new UserRepository(new MySqlConnector(new Mapper()), new UserConnector());
    }

    private boolean isRight(IUserRightRepository userRightRepository, UserRight userRight) {
        return !userRightRepository.get(userRight.getUserId(), userRight.getService(), Toolbox.right(userRight.getServiceRight())).isPresent();
    }

    public void create() {
        if (!isRootUser()) {
            createRootUser();
        }
        userRepository.get(ROOT_EMAIL).ifPresent(u -> {
            IUserRightRepository userRightRepository = new UserRightRepository(new MySqlConnector(new Mapper()));
            List<String> rights = Toolbox.rights();
            UserRight right = new UserRight();
            right.setUserId(u.getId());
            services.forEach(a -> {
                        right.setService(a);
                        rights.forEach(r -> {
                                    right.setServiceRight(r);
                                    if (isRight(userRightRepository, right)) {
                                        userRightRepository.save(right);
                                    }
                                }
                        );
                    }
            );
        });
    }

    private boolean isRootUser() {
        return userRepository.get(ROOT_EMAIL).isPresent();
    }

    private void createRootUser() {
        User user = new User();
        user.setUserEmail(ROOT_EMAIL);
        user.setPw("OrsWin9IstGeil!");
        userRepository.save(user);
    }

}
