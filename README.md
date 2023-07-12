# Sample to use OpenRewrite with JHipster Lite

JHipster Lite is a maven project, which generates other java projects.

The idea/vision is to use open rewrite for common tasks like adding dependencies.

For this the idea is to execute recipes manually via java on a different project/source set.

In this sample we try to evaluate if a gradle dependency can be added to 
build.gradle (or build.gradle.kts) which is located in a different folder (in this case test-gradle).

Current state: It seems not to work. The gradle file looks to be parsed, but the list of 
source files is empty.