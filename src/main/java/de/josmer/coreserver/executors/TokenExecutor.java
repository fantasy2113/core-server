package de.josmer.coreserver.executors;

import de.josmer.coreserver.api.security.token.Token;
import de.josmer.coreserver.base.interfaces.executor.IExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class TokenExecutor implements IExecutor {

    private static final Logger LOGGER = LogManager.getLogger(TokenExecutor.class.getName());

    @Override
    public void start() {
        ScheduledExecutorService tokenService = Executors.newScheduledThreadPool(1);
        long midnight = LocalDateTime.now().until(LocalDate.now().plusDays(1).atStartOfDay(), ChronoUnit.MINUTES);
        tokenService.scheduleAtFixedRate(this, midnight, 1440, TimeUnit.MINUTES);
    }

    @Override
    public void run() {
        Token.init();
        LOGGER.info("init token");
    }
}
