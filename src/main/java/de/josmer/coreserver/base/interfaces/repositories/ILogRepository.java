package de.josmer.coreserver.base.interfaces.repositories;

import java.util.List;

public interface ILogRepository {

    List<String> infoLogs();

    List<String> errorLogs();
}
