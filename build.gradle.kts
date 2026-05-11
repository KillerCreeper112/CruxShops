
plugins {
    java
    alias(libs.plugins.paperweight)
    alias(libs.plugins.shadow)
}

version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper)
    compileOnly(files(
        "E:\\Plugins\\YO\\CruxCore\\build\\libs\\CruxCore-1.0-all.jar",
        "E:\\Plugins\\YO\\CruxCurrency\\build\\libs\\CruxCurrency-1.0.jar",
        "E:\\Plugins\\YO\\CruxSummoning\\build\\libs\\CruxSummoning-1.0.jar",
        "E:\\Plugins\\YO\\CruxRewards\\build\\libs\\CruxRewards-1.0.jar",
      "E:\\Plugins\\YO\\CruxQuest\\build\\libs\\CruxQuest-1.0-all.jar"
    ))
    implementation("com.zaxxer:HikariCP:7.0.2")

    compileOnly(fileTree("libs"){
        include("*.jar")
    })
}

tasks{
    assemble{
        //dependsOn(reobfJar)
        dependsOn(shadowJar)
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

allprojects{

    plugins.apply("java")

    repositories {
        mavenCentral()
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    tasks.withType<Test> {
        systemProperty("file.encoding", "UTF-8")
    }

    tasks.withType<Javadoc>{
        options.encoding = "UTF-8"
    }
}













