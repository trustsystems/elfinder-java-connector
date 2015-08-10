package br.com.trustsystems.elfinder.command;

import br.com.trustsystems.elfinder.core.ElfinderContext;

public interface ElfinderCommand {

    void execute(ElfinderContext context) throws Exception;

}
