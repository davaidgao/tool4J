package com.gdwii.tool4j.chain.controller;

/**
 * @className: com.gdwii.tool4j.chain.controller.RequestHandler
 * @description: RequestHandler
 * @author gdw
 * @date 2019-05-15 12:59:36
 */
public interface RequestHandler {
    String getName();
    void handle(Request request) throws ProcessException;
}
