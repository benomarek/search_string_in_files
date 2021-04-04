package com.app;

import com.app.arguments.ArgParser;
import com.app.arguments.ArgsValidator;
import com.app.arguments.Argument;
import com.app.arguments.ArgumentType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main {

    private static final int DEFAULT_NUM_OF_THREADS = 1;

    public static void main(String[] args) {

        Map<ArgumentType, Argument> argsMap = ArgParser.parse(args);

        ArgsValidator.validate(argsMap);

        Set<String> filePaths = getPaths(argsMap.get(ArgumentType.PATH));

        String searchedString = argsMap.get(ArgumentType.SEARCH).getValue();

        Integer numOfThread = argsMap.containsKey(ArgumentType.THREADS) ? Integer.parseInt(argsMap.get(ArgumentType.THREADS).getValue()) : DEFAULT_NUM_OF_THREADS;

        StringFinder stringFinder = new StringFinder(filePaths, searchedString, numOfThread);

        long startTime = System.nanoTime();
        stringFinder.start();
        long endTime = System.nanoTime();

        long duration = endTime - startTime;
        double elapsedTimeInSecond = (double) duration / 1_000_000_000;
        System.out.println("Search duration: " + elapsedTimeInSecond + " sec.");

    }

    private static Set<String> getPaths(Argument pathArg) {

        Set<String> paths = new HashSet<>();
        try {
            Files.walk(Paths.get(pathArg.getValue()))
                    .filter(Files::isRegularFile)
                    .forEach(path -> paths.add(path.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return paths;
    }


}
