package com.gdwii.tool4j.chain.controller;

/**
 * @className: com.gdwii.tool4j.chain.controller.Request
 * @description: Request
 * @author gdw
 * @date 2019-05-15 12:58:03
 */
public interface Request {
    String getName();
    Response getResponse();
    void setResponse(Response response);
}