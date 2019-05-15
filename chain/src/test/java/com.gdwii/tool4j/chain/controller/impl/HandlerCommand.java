package com.gdwii.tool4j.chain.controller.impl;

import com.gdwii.tool4j.chain.Command;
import com.gdwii.tool4j.chain.Context;
import com.gdwii.tool4j.chain.controller.ProcessException;
import com.gdwii.tool4j.chain.controller.Request;
import com.gdwii.tool4j.chain.controller.RequestHandler;
import com.gdwii.tool4j.chain.controller.Response;

/**
 * @className: com.gdwii.tool4j.chain.controller.impl.HandlerCommand
 * @description: RequestHandler Impl
 * @author gdw
 * @date 2019-05-15 13:02:46
 */
public class HandlerCommand implements Command, RequestHandler {
    private final String name;

    public HandlerCommand(String name) {
        this.name = name;
    }

    @Override
    public boolean execute(Context context) throws Exception {
        handle((Request)context);
        return true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void handle(Request request) throws ProcessException {
        String name = request.getName();
        Response response = new ResponseContext(name);
        request.setResponse(response);
    }
}
