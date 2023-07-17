package org.example;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void main(String[] args) throws IOException, InterruptedException {

        Path projectDir = Paths.get("./test-gradle");

        File file = projectDir.toFile();
        ProcessBuilder builder = new ProcessBuilder("gradle", "--init-script", "init.gradle", "rewriteDryRun").directory(file);
        Process process = builder.start();
        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), System.out::println);
        Future<?> future = executorService.submit(streamGobbler);

        int exitCode = process.waitFor();

        System.exit(exitCode);

    }
}