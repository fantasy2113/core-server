package de.josmer.coreserver.config;

import de.josmer.coreserver.base.interfaces.executor.IExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CoreServerExecutor {

    private static final Logger LOGGER = LogManager.getLogger(CoreServerExecutor.class.getName());
    private final Set<Class<? extends IExecutor>> executorsClasses;

    public CoreServerExecutor() {
        this.executorsClasses = new Reflections("de.josmer.executors").getSubTypesOf(IExecutor.class);
    }

    public void startAll() {
        List<IExecutor> executors = getExecutors();
        executors.forEach(IExecutor::start);
        executors.stream().map(e -> e.getClass().getSimpleName() + " - started").forEach(LOGGER::info);
    }

    private List<IExecutor> getExecutors() {
        List<IExecutor> executors = new ArrayList<>();
        for (Class<? extends IExecutor> executorsClass : executorsClasses) {
            try {
                executors.add(executorsClass.newInstance());
            } catch (InstantiationException | IllegalAccessException ex) {
                LOGGER.info(ex.getMessage());
            }
        }
        return executors;
    }

}
