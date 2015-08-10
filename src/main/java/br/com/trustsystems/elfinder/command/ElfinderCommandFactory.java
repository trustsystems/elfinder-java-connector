package br.com.trustsystems.elfinder.command;

public interface ElfinderCommandFactory {

    ElfinderCommand get(String commandName);

}