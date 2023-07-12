package org.example;

import org.openrewrite.*;
import org.openrewrite.config.Environment;
import org.openrewrite.gradle.AddDependency;
import org.openrewrite.gradle.GradleParser;
import org.openrewrite.internal.InMemoryLargeSourceSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {

        Path projectDir = Paths.get("./test-gradle-2");
        AddDependency recipe = new AddDependency(
                "org.openrewrite",
                "rewrite-java",
                "8.1.2",
                null,
                "implementation",
                "java.util.String",
                null,
                null,
                null,
                true
        );
        List<Path> sourcePaths = Files.find(projectDir, 999, (p, bfa) -> {
                        return bfa.isRegularFile()  && p.getFileName().toString().endsWith(".gradle");}
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