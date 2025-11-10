# OpenAI Chatbot Service

A full-stack application combining a Spring Boot backend with a Next.js frontend for integrating with OpenAI's API.

## Technology Stack

### Backend
- Java 21
- Spring Boot 3.4.1
- Gradle 8.5
- OpenAI Java Client

### Frontend
- Next.js 14
- React 18
- TypeScript 5

## Prerequisites

- JDK 21 or higher
- OpenAI API Key
- Node.js and npm are automatically managed by Gradle (no manual installation needed)

## Configuration

Set your OpenAI API key as an environment variable:

```bash
export OPENAI_API_KEY=your-api-key-here
```

Or update the `application.properties` file with your API key.

## Running the Application

### Production Mode (Full Stack)

Build and run the complete application (frontend + backend in single JAR):

#### On Windows:
```bash
gradlew.bat build
gradlew.bat bootRun
```

#### On Linux/Mac:
```bash
./gradlew build
./gradlew bootRun
```

The application will start on `http://localhost:8080` and serve both the frontend and API.

### Development Mode (Separate Servers)

For active development with hot reload:

**Terminal 1 - Backend:**
```bash
# Windows
gradlew.bat bootRun

# Linux/Mac
./gradlew bootRun
```
Backend API runs on `http://localhost:8080`

**Terminal 2 - Frontend:**
```bash
cd frontend
npm run dev
```
Frontend dev server runs on `http://localhost:3000`

## Building the Application

The build process automatically:
1. Downloads Node.js and npm (first build only)
2. Installs frontend dependencies
3. Builds the Next.js static export
4. Copies frontend files to backend resources
5. Creates a Spring Boot JAR with embedded frontend

```bash
# Windows
gradlew.bat clean build

# Linux/Mac
./gradlew clean build
```

Output: `build/libs/openai-chatbot-0.0.1-SNAPSHOT.jar`

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
│   │   ├── java/com/openai/chatbot/
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── config/
│   │   │   ├── dto/
│   │   │   ├── exception/
│   │   │   └── OpenaiChatbotApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/               # Frontend build output (auto-generated)
│   └── test/
│       └── groovy/com/openai/chatbot/
├── frontend/                          # Next.js frontend
│   ├── pages/
│   │   ├── _app.tsx
│   │   ├── _document.tsx
│   │   └── index.tsx
│   ├── public/
│   ├── package.json
│   ├── next.config.js
│   ├── tsconfig.json
│   └── .gitignore
├── gradle/
│   └── wrapper/
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
└── README.md
```

## Available Gradle Tasks

### Frontend Tasks
- `gradlew installFrontendDependencies` - Install npm packages
- `gradlew buildFrontend` - Build Next.js static export
- `gradlew copyFrontendToStatic` - Copy frontend build to backend
- `gradlew cleanFrontend` - Clean frontend build artifacts
- `gradlew devFrontend` - Run Next.js dev server

### Backend Tasks
- `gradlew bootRun` - Run Spring Boot application
- `gradlew test` - Run backend tests
- `gradlew build` - Build complete application (frontend + backend)
- `gradlew clean` - Clean all build artifacts
