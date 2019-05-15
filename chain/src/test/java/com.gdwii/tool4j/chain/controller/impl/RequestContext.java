package com.gdwii.tool4j.chain.controller.impl;

import com.gdwii.tool4j.chain.controller.Request;
import com.gdwii.tool4j.chain.controller.Response;
import com.gdwii.tool4j.chain.impl.ContextBase;

/**
 * @className: com.gdwii.tool4j.chain.controller.impl.RequestContext
 * @description: Request Impl
 * @author gdw
 * @date 2019-05-15 13:03:04
 */
public class RequestContext extends ContextBase implements Request {
    private String name;

    private Response response;

    public RequestContext(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Response getResponse() {
        return response;
    }

    @Override
    public void setResponse(Response response) {
        this.response = response;
    }
}
