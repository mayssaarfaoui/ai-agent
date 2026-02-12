package com.aimanager.agent.resolver;

import com.aimanager.agent.models.IteratorType;
import com.aimanager.agent.nodes.DownStreamAgent;
import com.aimanager.agent.services.PRDownStream;
import com.aimanager.agent.services.TaskDownStream;
import com.aimanager.agent.services.UserDownStream;
import com.aimanager.agent.utils.ContextBeanProvider;

public class IteratorResolver {

    public static DownStreamAgent resolve(IteratorType iteratorType) {
        switch (iteratorType) {
            case ITERATOR_TASK:
                return ContextBeanProvider.getBean(TaskDownStream.class);
            case ITERATOR_USER:
                return ContextBeanProvider.getBean(UserDownStream.class);
            case ITERATOR_PR:
                return ContextBeanProvider.getBean(PRDownStream.class);
            default:
                return null;
        }
    }
}
