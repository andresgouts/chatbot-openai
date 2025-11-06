# CLAUDE.m

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

A Spring Boot service for integrating with OpenAI's API. This is a minimal Spring Boot application currently containing only the application entry point.

**Technology Stack:**
- Java 17
- Spring Boot 3.4.1
- Gradle 8.5
- OpenAI Java Client (theokanning.openai-gpt3-java v0.18.2)
- Lombok for reducing boilerplate
- Spring Boot Actuator for health checks

## Essential Commands

### Running the Application
```bash
# Windows
gradlew.bat bootRun

# Linux/Mac
./gradlew bootRun
```
Application runs on `http://localhost:8080`

### Building
```bash
# Windows
gradlew.bat build

# Linux/Mac
./gradlew build
```

### Testing
```bash
# Windows
gradlew.bat test

# Linux/Mac
./gradlew test
```

To run a single test class:
```bash
# Windows
gradlew.bat test --tests ClassName

# Linux/Mac
./gradlew test --tests ClassName
```

To run a specific test method:
```bash
# Windows
gradlew.bat test --tests ClassName.methodName

# Linux/Mac
./gradlew test --tests ClassName.methodName
```

### Clean and Rebuild
```bash
# Windows
gradlew.bat clean build

# Linux/Mac
./gradlew clean build
```

## Configuration

**OpenAI API Key**: Must be set as environment variable `OPENAI_API_KEY` or configured in `src/main/resources/application.properties`

**Key application properties:**
- `openai.api.key`: OpenAI API key (defaults to `${OPENAI_API_KEY}`)
- `openai.model`: Default model is `gpt-3.5-turbo`
- `server.port`: Default is `8080`
- Actuator endpoints: `/actuator/health` and `/actuator/info` are exposed

## Architecture

**Package Structure:**
- Base package: `com.openai.chatbot`
- Main class: `OpenaiChatbotApplication.java`
- Standard Spring Boot layered architecture expected (controllers, services, repositories)

**Dependencies:**
- Spring Boot Web for REST APIs
- Spring Boot Validation for request validation
- Spring Boot Actuator for monitoring
- Spring Boot DevTools for development hot reload
- Lombok annotations are available for reducing boilerplate

## Development Notes

- Uses Lombok: Enable annotation processing in your IDE
- Spring DevTools is included for automatic restart during development
- Logging: Root level is INFO, `com.openai.chatbot` package is set to DEBUG
- Health check available at: `http://localhost:8080/actuator/health`
