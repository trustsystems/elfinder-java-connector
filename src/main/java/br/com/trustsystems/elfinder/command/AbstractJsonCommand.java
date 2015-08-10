package br.com.trustsystems.elfinder.command;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import br.com.trustsystems.elfinder.ElFinderConstants;
import br.com.trustsystems.elfinder.service.ElfinderStorage;

public abstract class AbstractJsonCommand extends AbstractCommand {

    public static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json; charset=UTF-8";

    protected abstract void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, JSONObject json) throws Exception;

    @Override
    final public void execute(ElfinderStorage elfinderStorage, HttpServletRequest request, HttpServletResponse response) throws Exception {

        JSONObject json = new JSONObject();

        try (PrintWriter writer = response.getWriter()) {
            execute(elfinderStorage, request, json);
            response.setContentType(APPLICATION_JSON_CHARSET_UTF_8);
            json.write(writer);
            writer.flush();
        } catch (Exception e) {
            logger.error("Unable to execute abstract json command", e);
            json.put(ElFinderConstants.ELFINDER_JSON_RESPONSE_ERROR, e.getMessage());
        }
    }

}