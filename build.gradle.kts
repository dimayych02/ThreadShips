plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"


repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.testng:testng:7.3.0")
    testImplementation("io.rest-assured:rest-assured:5.1.0")
    implementation("io.qameta.allure:allure-testng:2.20.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.22")
    testImplementation("org.aspectj:aspectjweaver:1.9.8")
    implementation ("org.apache.kafka:kafka-streams:2.7.0")
    implementation ("org.apache.kafka:kafka-clients:2.7.0")
    testImplementation ("org.apache.kafka:kafka-streams-test-utils:2.7.0")
    testImplementation ("org.awaitility:awaitility:4.2.0")
    implementation ("org.apache.curator:curator-test:2.9.0")
    implementation ("org.apache.commons:commons-math3:3.6.1")
}

tasks.test {
    useTestNG()
}
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}