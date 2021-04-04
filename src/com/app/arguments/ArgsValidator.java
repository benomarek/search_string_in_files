package com.app.arguments;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class ArgsValidator {


    public static void validate(Map<ArgumentType, Argument> argsMap) {

        boolean error = false;
        StringBuilder s = new StringBuilder();

        if (!argsMap.containsKey(ArgumentType.PATH)) {
            s.append("Missing argument -p \n");
            error = true;
        }

        if (!argsMap.containsKey(ArgumentType.SEARCH)) {
            s.append("Missing argument -s\n");
            error = true;
        }

        if (!error && !isValidPathArg(argsMap.get(ArgumentType.PATH))) {
            s.append("Invalid argument -p \n");
            error = true;
        }

        if (!error && !isValidThreadArg(argsMap.get(ArgumentType.THREADS))) {
            s.append("Invalid argument -t \n");
            error = true;
        }

        if (!error && !isValidSearchedStringArg(argsMap.get(ArgumentType.SEARCH))) {
            s.append("Searched string must have 128 chars.");
            error = true;
        }

        if (error) {
            System.err.println(s.toString());
            System.exit(1);
        }
    }

    private static boolean isValidThreadArg(Argument argument) {
        if (argument == null) return true; //optional params

        if (argument.getValue() == null) return false;

        try {
            Integer.parseInt(argument.getValue());
            return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }

    }

    private static boolean isValidPathArg(Argument argument) {
        return argument.getValue() != null && (Files.isRegularFile(Paths.get(argument.getValue())) || Files.isDirectory(Paths.get(argument.getValue())));
    }

    private static boolean isValidSearchedStringArg(Argument argument) {
        return argument.getValue() != null && argument.getValue().length() < 128;
    }

}
