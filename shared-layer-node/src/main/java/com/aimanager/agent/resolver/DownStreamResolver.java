package com.aimanager.agent.resolver;

import com.aimanager.agent.models.DownStreamType;
import com.aimanager.agent.services.*;
import com.aimanager.agent.utils.ContextBeanProvider;

public class DownStreamResolver {

    public static DownStreamProcess resolve(DownStreamType downStreamType) {
        switch (downStreamType) {
            case OPEN_PR:
                return ContextBeanProvider.getBean(OpenPRService.class);
            case SUMMARY_PR:
                return ContextBeanProvider.getBean(SummaryPRService.class);
            case DECLINE_PR:
                return ContextBeanProvider.getBean(DeclinePRService.class);
            case REMINDER_PR:
                return ContextBeanProvider.getBean(ReminderPRService.class);
            default:
                return null;
        }
    }
}
