# OpenAI Chatbot Service

A Spring Boot service for integrating with OpenAI's API.

## Technology Stack

- Java 17
- Spring Boot 3.4.1
- Maven
- OpenAI Java Client

## Prerequisites

- JDK 17 or higher
- Maven 3.6+
- OpenAI API Key

## Configuration

Set your OpenAI API key as an environment variable:

```bash
export OPENAI_API_KEY=your-api-key-here
```

Or update the `application.properties` file with your API key.

## Running the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Building the Application

```bash
mvn clean package
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
├── pom.xml
└── README.md
```
