package com.gdwii.tool4j.chain.controller;

/**
 * @className: com.gdwii.tool4j.chain.controller.Controller
 * @description: Controller
 * @author gdw
 * @date 2019-05-15 12:57:37
 */
public interface Controller {
    void addHandler(RequestHandler handler);
    RequestHandler getHandler(String name) throws ProcessException;
    void process(Request request) throws ProcessException;
}
