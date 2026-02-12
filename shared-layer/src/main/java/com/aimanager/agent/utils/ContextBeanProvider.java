package com.aimanager.agent.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ContextBeanProvider implements ApplicationContextAware {

    private static ApplicationContext ctx;

    public static <T> T getBean(Class<T> clazz) {
        return (T) ctx.getBean(clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext ac) {
        ctx = ac;
    }
}