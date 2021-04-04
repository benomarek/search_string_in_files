package com.app.arguments;

import java.util.HashMap;
import java.util.Map;

public class ArgParser {

    public static Map<ArgumentType, Argument> parse(String[] arguments) {

        Map<ArgumentType, Argument> argsMap = new HashMap<>();

        Argument argument = null;
        for (final String a : arguments) {

            if (a.charAt(0) == '-') {
                if (a.length() < 2) {
                    System.err.println("Error at argument " + a);
                    break;
                }

                argument = new Argument(a);
                ArgumentType value = ArgumentType.findByValue(a);
                if (value == null) {
                    System.err.println("Unknown argument " + a);
                    System.exit(1);
                }

                if (ArgumentType.HELP == value) {
                    System.out.println(
                            "args: \n " +
                                    "-p : path to file or folder \n " +
                                    "-s : string to be searched - max 128 chars \n " +
                                    "-t : <optional> number of threads used for search");
                    System.exit(1);

                }

                argsMap.put(value, argument);
            } else if (argument != null) {
                argument.setValue(a);
            } else {
                System.err.println("Illegal parameter usage");
                System.exit(1);
            }
        }

        return argsMap;
    }

}
