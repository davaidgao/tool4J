package com.gdwii.tool4j.chain;

import com.gdwii.tool4j.chain.controller.*;
import com.gdwii.tool4j.chain.controller.impl.ControllerCatalog;
import com.gdwii.tool4j.chain.controller.impl.HandlerCommand;
import com.gdwii.tool4j.chain.controller.impl.RequestContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @className: com.gdwii.tool4j.chain.ProcessingTest
 * @description: 以测试驱动的方法测试责任链模式
 * @author gdw
 * @date 2019-05-15 12:55:10
 */
public class ProcessingTest {
    @Test
    public void testRequestResponseName() throws ProcessException {
        String NAME = "TestProcessing";

        Controller controller = new ControllerCatalog();

        RequestHandler handler = new HandlerCommand(NAME);
        controller.addHandler(handler);
        Request request = new RequestContext(NAME);
        controller.process(request);
        Response response = request.getResponse();

        assertNotNull(response);
        assertEquals(NAME, response.getName());
    }
}