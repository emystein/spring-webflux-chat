import com.github.gradle.node.npm.task.NpmTask

plugins {
	java
	id("org.springframework.boot") version "3.0.5"
	id("io.spring.dependency-management") version "1.1.0"
	id("com.github.node-gradle.node") version "3.5.1"
}

group = "ar.com.flow"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

node {
	version.set("18.14.0")
	download.set(true)
	nodeProjectDir.set(file("src/main/ui"))
}

val buildUI = tasks.register<NpmTask>("buildUI") {
	dependsOn(tasks.npmInstall)
	args.set(listOf("run", "build:dev"))
}

val copyUIBundleToBuildDirectory = tasks.register<Copy>("copyUIBundleToBuildDirectory") {
	dependsOn(buildUI)
	from("src/main/ui/dist")
	into("build/resources/main/public")
}

tasks.processResources {
	dependsOn(copyUIBundleToBuildDirectory)
}
