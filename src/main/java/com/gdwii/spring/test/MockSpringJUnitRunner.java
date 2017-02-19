package com.gdwii.spring.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Mock与Spring集成的测试
 * 使用方式：
 *      1.在测试类上加上@RunWith(MockSpringJUnitRunner.class)
 *      2.加载Spring配置文件,如@ContextConfiguration("classpath:test-mock-user.xml")
 *      3.配置Mock对象替换原Spring中对象的BeanPostProcessor
 *           <bean class="com.gdwii.spring.test.MockSpringJUnitRunner.MockBeanPostProcessor"/>
 *      4.在测试类中将想要Mock的类,将该类作为测试类的一个属性,属性名保持与Spring容器中的一致
 *           @Mock
 *            private UserDao userDao;
 *      5.在@Before方法中配置mock相关的方法
 *          Mockito.when(userDao.insertUser(Mockito.any())).thenReturn("这是一个mock返回值");
 */
public class MockSpringJUnitRunner extends Runner {
    /**
     * 实际上运行单元测试的JunitRunner
     */
    private SpringJUnit4ClassRunner springJUnit4ClassRunner;

    /**
     * 需要替换的mock对象
     */
    private static Map<String, Object> mockObjectMap = new HashMap<>();;

    private static final Log logger = LogFactory.getLog(SpringJUnit4ClassRunner.class);

    public MockSpringJUnitRunner(Class<?> clazz) throws InitializationError {
        if (logger.isDebugEnabled()) {
            logger.debug("MockSpringJUnitRunner constructor called with [" + clazz + "]");
        }
        initMockObjectMap(clazz);
        springJUnit4ClassRunner = new MockSpringJUnit4ClassRunner(clazz);
    }

    /**
     * 创建mock对象用于替换原Spring容器中的对象
     * @param clazz
     */
    private void initMockObjectMap(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Mock.class)) {
                mockObjectMap.put(field.getName(), Mockito.mock((Class) field.getGenericType()));
            }
        }
    }

    @Override
    public Description getDescription() {
        return springJUnit4ClassRunner.getDescription();
    }

    @Override
    public void run(RunNotifier runNotifier) {
        springJUnit4ClassRunner.run(runNotifier);
    }

    @Override
    public int testCount() {
        return springJUnit4ClassRunner.testCount();
    }

    /**
     * 创建该类的目的是为了设置测试实例中的mock属性
     */
    private class MockSpringJUnit4ClassRunner extends SpringJUnit4ClassRunner{

        public MockSpringJUnit4ClassRunner(Class<?> clazz) throws InitializationError {
            super(clazz);
        }

        @Override
        protected Object createTest() throws Exception {
            Object testObject =  super.createTest();
            setMockObject2TestObject(testObject);
            return testObject;
        }

        /**
         * 将创建的mock对象设置到测试对象中
         * 用于设置mock的规则
         */
        public void setMockObject2TestObject(Object testObject) {
            System.out.println(testObject);
            for(Map.Entry<String, Object> mockObject : mockObjectMap.entrySet()){
                ReflectionTestUtils.setField(testObject, mockObject.getKey(), mockObject.getValue());
            }
        }
    }

    public static class MockBeanPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
            if(mockObjectMap.containsKey(s)){
                return mockObjectMap.get(s);
            }
            return o;
        }

        @Override
        public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
            return o;
        }
    }
}