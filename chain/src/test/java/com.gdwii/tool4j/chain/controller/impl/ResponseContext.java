package com.gdwii.tool4j.chain.controller.impl;

import com.gdwii.tool4j.chain.controller.Response;
import com.gdwii.tool4j.chain.impl.ContextBase;

/**
 * @className: com.gdwii.tool4j.chain.controller.impl.ResponseContext
 * @description: Response Impl
 * @author gdw
 * @date 2019-05-15 13:03:35
 */
public class ResponseContext extends ContextBase implements Response {
    private final String name;

    public ResponseContext(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
