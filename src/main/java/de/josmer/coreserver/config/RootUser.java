package de.josmer.coreserver.config;

import de.josmer.coreserver.base.Toolbox;
import de.josmer.coreserver.base.entities.User;
import de.josmer.coreserver.base.entities.UserRight;
import de.josmer.coreserver.base.interfaces.repositories.IUserRepository;
import de.josmer.coreserver.base.interfaces.repositories.IUserRightRepository;
import de.josmer.coreserver.connector.MySqlConnector;
import de.josmer.coreserver.connector.UserConnector;
import de.josmer.coreserver.connector.mapper.Mapper;
import de.josmer.coreserver.repositories.UserRepository;
import de.josmer.coreserver.repositories.UserRightRepository;

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
