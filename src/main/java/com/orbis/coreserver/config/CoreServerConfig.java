package com.orbis.coreserver.config;

import org.glassfish.jersey.server.ResourceConfig;

public final class CoreServerConfig extends ResourceConfig {

    public CoreServerConfig(boolean isDeveloperMode) {
        register(new CoreServerBinder());
        packages(isDeveloperMode, "com.orbis.coreserver.api.services");
    }

}
