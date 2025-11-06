# OpenAI Chatbot Service

A Spring Boot service for integrating with OpenAI's API.

## Technology Stack

- Java 17
- Spring Boot 3.4.1
- Gradle 8.5
- OpenAI Java Client

## Prerequisites

- JDK 17 or higher
- OpenAI API Key

## Configuration

Set your OpenAI API key as an environment variable:

```bash
export OPENAI_API_KEY=your-api-key-here
```

Or update the `application.properties` file with your API key.

## Running the Application

### On Windows:
```bash
gradlew.bat bootRun
```

### On Linux/Mac:
```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`

## Building the Application

```bash
# Windows
gradlew.bat build

# Linux/Mac
./gradlew build
```

## Running Tests

```bash
# Windows
gradlew.bat test

# Linux/Mac
./gradlew test
```

## Health Check

Once running, you can check the application health at:

```
http://localhost:8080/actuator/health
```

## Project Structure

```
openai-chatbot/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/openai/chatbot/
│   │   │       └── OpenaiChatbotApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/openai/chatbot/
├── gradle/
│   └── wrapper/
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
└── README.md
```
