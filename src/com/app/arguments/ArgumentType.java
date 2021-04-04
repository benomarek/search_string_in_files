package com.app.arguments;

public enum ArgumentType {
    PATH("-p"),
    SEARCH("-s"),
    HELP("-h"),
    THREADS("-t");

    private final String value;

    ArgumentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ArgumentType findByValue(String v) {
        for (ArgumentType argumentType : ArgumentType.values()) {
            if (v.equals(argumentType.value)) {
                return argumentType;
            }
        }
        return null;
    }
}
