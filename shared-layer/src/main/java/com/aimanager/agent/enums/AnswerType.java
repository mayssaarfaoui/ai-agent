package com.aimanager.agent.enums;/* see LICENSE file in the root */

import static java.lang.System.exit;

public enum AnswerType implements TypeBase {
    AGREEMENT(Aliases.AGREEMENT, 601),
    BLOCKED(Aliases.BLOCKED, 901),
    DELIVERY_DATE(Aliases.DELIVERY_DATE, 401),
    MULTIPLE_CHOICE(Aliases.MULTIPLE_CHOICE, 301),
    NUMBER(Aliases.NUMBER, 201),
    OOO(Aliases.OOO, 701),
    SCHEDULE_STATUS(Aliases.SCHEDULE_STATUS, 801),
    TASK_STATUS(Aliases.TASK_STATUS, 501),
    YES_NO(Aliases.YES_NO, 101);

    private final int id;
    private final String name;

    AnswerType(String name, int id) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static boolean isAnswerType(String type) {
        type = type.toLowerCase();
        switch (type) {
            case "agreement": // fixme use enum getName
                return true;
            case "blocked":
                return true;
            case "delivery_date":
                return true;
            case "multiple_choice":
                return true;
            case "number":
                return true;
            case "ooo":
                return true;
            case "schedule_status":
                return true;
            case "yes_no":
                return true;
            default:
                System.out.println("Invalid Answer Type");
                exit(1);
        }
        return false;
    }

    public static class Aliases{
        public static final String AGREEMENT="Agreement";
        public static final String BLOCKED="Blocked";
        public static final String DELIVERY_DATE="Delivery Date";
        public static final String MULTIPLE_CHOICE="Multiple Choice";
        public static final String NUMBER="Number";
        public static final String OOO="OOO";
        public static final String SCHEDULE_STATUS="Schedule Status";
        public static final String TASK_STATUS="TASK_STATUS";
        public static final String YES_NO="YES_NO";
    }
}