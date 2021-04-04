package com.app;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StringFinder {

    private final ArrayBlockingQueue<String> queue;
    private final byte[] searchedByteArray;
    private int numOfThreads = 1;

    public StringFinder(Set<String> paths, String searchedString) {
        this.queue = new ArrayBlockingQueue<>(paths.size(), true, paths);
        this.searchedByteArray = searchedString.getBytes();
    }

    public StringFinder(Set<String> path, String searchedString, Integer threadNum) {
        this(path, searchedString);
        numOfThreads = threadNum;
    }

    public void start() {

        ExecutorService executorService = Executors.newCachedThreadPool();
        ExecutorCompletionService<Boolean> s = new ExecutorCompletionService<>(executorService);

        for (int i = 0; i < numOfThreads; i++) {
            s.submit(this::search);
        }

        waitForAllToFinish(s);
        executorService.shutdown();
    }

    private void waitForAllToFinish(ExecutorCompletionService<Boolean> s) {

        int received = 0;
        boolean errors = false;

        while (received < numOfThreads && !errors) {
            try {
                s.take();
                received++;
            } catch (InterruptedException e) {
                e.printStackTrace();
                errors = true;
            }
        }
    }

    private boolean search() {

        String path;
        while ((path = queue.poll()) != null) {

            File f = new File(path);
            try (RandomAccessFile reader = new RandomAccessFile(f, "r")) {

                reader.seek(0);
                int matchIndex = 0;

                while (true) {
                    try {

                        if (matchIndex == searchedByteArray.length) {
                            System.out.println(new OutPut(searchedByteArray, reader.getFilePointer(), f.getName(), reader).print());

                            matchIndex = 0;
                        }

                        byte b = reader.readByte();

                        if (b == searchedByteArray[matchIndex]) {
                            matchIndex++;
                            continue;
                        }

                        matchIndex = 0;
                    } catch (EOFException e) {
                        break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        }
        return true;
    }
}
