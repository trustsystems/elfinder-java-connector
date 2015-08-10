package br.com.trustsystems.elfinder.command;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.trustsystems.elfinder.support.concurrency.GenericCache;

public class CommandFactory implements ElfinderCommandFactory {

    private static final Logger logger = LoggerFactory.getLogger(CommandFactory.class);

    private String classNamePattern;

    private final GenericCache<String, ElfinderCommand> cache = new GenericCache<>();

    @Override
    public ElfinderCommand get(final String commandName) {
        if (commandName == null || commandName.trim().isEmpty()) {
            logger.error(String.format("Command %s cannot be null or empty", commandName));
            throw new RuntimeException(String.format("Command %s cannot be null or empty", commandName));
        }

        ElfinderCommand command = null;

        try {
            command = cache.getValue(commandName, new Callable<ElfinderCommand>() {
                @Override
                public ElfinderCommand call() throws Exception {
                    logger.debug(String.format("trying recovery command!: %s", commandName));
                    String className = String.format(getClassNamePattern(), commandName.substring(0, 1).toUpperCase() + commandName.substring(1));
                    return (ElfinderCommand) Class.forName(className).newInstance();
                }
            });
            logger.debug(String.format("command found!: %s", commandName));
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Unable to get/create command instance.", e);
        }
        return command;
    }

    private String getClassNamePattern() {
        return classNamePattern;
    }

    public void setClassNamePattern(String classNamePattern) {
        this.classNamePattern = classNamePattern;
    }
}
