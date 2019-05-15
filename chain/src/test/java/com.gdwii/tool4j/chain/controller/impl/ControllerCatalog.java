package com.gdwii.tool4j.chain.controller.impl;

import com.gdwii.tool4j.chain.Command;
import com.gdwii.tool4j.chain.controller.Controller;
import com.gdwii.tool4j.chain.controller.ProcessException;
import com.gdwii.tool4j.chain.controller.Request;
import com.gdwii.tool4j.chain.controller.RequestHandler;
import com.gdwii.tool4j.chain.impl.CatalogBase;

/**
 * @author gdw
 * @className: com.gdwii.tool4j.chain.controller.impl.ControllerCatalog
 * @description: Controller Impl
 * @date 2019-05-15 13:00:38
 */
public class ControllerCatalog extends CatalogBase implements Controller {
    @Override
    public void addHandler(RequestHandler handler) {
        addCommand(handler.getName(), (Command) handler);
    }

    @Override
    public RequestHandler getHandler(String name) {
        return (RequestHandler) getCommand(name);
    }

    @Override
    public void process(Request request) throws ProcessException {
        RequestHandler handler = getHandler(request.getName());
        if (handler != null) {
            handler.handle(request);
        }
    }
}
