package de.josmer.coreserver.repositories;

import de.josmer.coreserver.base.interfaces.repositories.ILogRepository;
import org.junit.Test;
import utils.Factory;

import static org.junit.Assert.assertTrue;

public class LogRepositoryTest extends RepositoryTesting {

    private final ILogRepository logRepository;

    public LogRepositoryTest() {
        this.logRepository = Factory.get(LogRepository.class);
    }

    @Test
    public void infoLogs() {
        assertTrue(!logRepository.infoLogs().isEmpty());
    }

    @Test
    public void errorLogs() {
        assertTrue(logRepository.errorLogs().isEmpty());
    }
}
