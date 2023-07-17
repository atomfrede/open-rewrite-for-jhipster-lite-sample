package org.example;

import org.openrewrite.*;
import org.openrewrite.config.Environment;
import org.openrewrite.config.ResourceLoader;
import org.openrewrite.gradle.AddDependency;
import org.openrewrite.gradle.GradleParser;
import org.openrewrite.internal.InMemoryLargeSourceSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

public class Main {
    public static void main(String[] args) throws IOException {

        Path projectDir = Paths.get("./test-gradle");
        AddDependency recipe = new AddDependency(
                "com.google.guava",
                "guava",
                "29.X",
                "-jre",
                "implementation",
                "org.junit.jupiter.api.*",
                "test",
                "jar",
                "com.fasterxml.jackson*",
                true
        );
        List<Path> sourcePaths = Files.find(projectDir, 999, (p, bfa) -> {
                        return bfa.isRegularFile()  && (p.getFileName().toString().endsWith(".gradle") || p.getFileName().toString().endsWith(".java"));}
                )
                .toList();

        GradleParser gradleParser = GradleParser.builder().build();
        ExecutionContext ctx = new InMemoryExecutionContext(Throwable::printStackTrace);

        List<SourceFile> cus = gradleParser.parse(sourcePaths, projectDir, ctx)
                .collect(Collectors.toList());

        InMemoryLargeSourceSet inMemoryLargeSourceSet = new InMemoryLargeSourceSet(cus);


        RecipeRun run = recipe.run(inMemoryLargeSourceSet, ctx, 25);
        Changeset changeset = run.getChangeset();
        List<Result> allResults = changeset.getAllResults();

        for (Result allResult : allResults) {
            System.out.println(allResult.diff(projectDir));
        }
    }
}