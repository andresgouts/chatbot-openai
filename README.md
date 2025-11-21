# OpenAI Chatbot Service

A full-stack application combining a Spring Boot backend with a Next.js frontend for integrating with OpenAI's API.

## Technology Stack

### Backend
- Java 21
- Spring Boot 3.4.1
- Spring Data JPA (conversation persistence)
- H2 Database (file-based persistence)
- Flyway (database migrations)
- Spring Security 6 (security headers, CORS)
- Gradle 8.5
- OpenAI Java Client
- SpringDoc OpenAPI (API documentation)

### Frontend
- Next.js 14.2.33
- React 18.3.1
- TypeScript 5
- Jest & React Testing Library

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

### Backend Tests
```bash
# Windows
gradlew.bat test

# Linux/Mac
./gradlew test
```

### Frontend Tests
```bash
cd frontend
npm test                # Run all tests
npm run test:watch      # Run tests in watch mode
```

## API Documentation

Once the application is running, you can access the interactive API documentation:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

The Swagger UI provides:
- Interactive API testing
- Complete endpoint documentation
- Request/response schemas
- Example requests

## Health Check

Check the application health at:

```
http://localhost:8080/actuator/health
```

## H2 Database Console

Access the H2 database console at:

```
http://localhost:8080/h2-console
```

Connection details:
- **JDBC URL**: `jdbc:h2:file:./data/chatbot`
- **Username**: `sa`
- **Password**: (leave empty)

## Project Structure

```
openai-chatbot/
├── src/
│   ├── main/
│   │   ├── java/com/openai/chatbot/
│   │   │   ├── controller/          # REST API endpoints
│   │   │   ├── service/             # Business logic
│   │   │   ├── config/              # Spring configuration
│   │   │   ├── dto/                 # Data transfer objects
│   │   │   ├── entity/              # JPA entities
│   │   │   ├── repository/          # Spring Data repositories
│   │   │   ├── exception/           # Custom exceptions
│   │   │   └── OpenaiChatbotApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── db/migration/        # Flyway database migrations
│   │       └── static/              # Frontend build output (auto-generated)
│   └── test/
│       └── groovy/com/openai/chatbot/
├── data/                             # H2 database files (auto-generated)
├── frontend/                          # Next.js frontend
│   ├── pages/
│   │   ├── _app.tsx
│   │   ├── _document.tsx
│   │   ├── index.tsx
│   │   ├── 404.tsx                 # Custom 404 error page
│   │   └── 500.tsx                 # Custom 500 error page
│   ├── __tests__/                  # Jest tests
│   │   └── index.test.tsx
│   ├── public/
│   ├── package.json
│   ├── jest.config.js
│   ├── jest.setup.js
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

## Features

### Chat Functionality
- **OpenAI Integration**: Real-time chat with OpenAI GPT models
- **Conversation History**: Persistent storage of all conversations and messages
- **Auto-generated Titles**: Conversations automatically titled from first message (first 50 characters)
- **Continue Conversations**: Resume previous conversations by conversation ID
- **Message Persistence**: Immediate save after each user/assistant message pair

### API Endpoints

#### Chat API
- `POST /api/chat` - Send a chat message and receive AI response
  - Supports continuing existing conversations via `conversationId`
  - Automatically creates new conversation if none provided
  - Returns AI response with conversation ID

#### Conversation API
- `GET /api/conversations?userId={uuid}` - List all conversations for a user
  - Returns conversation ID and title only (for performance)
  - Ordered by most recently updated
- `GET /api/conversations/{id}` - Get complete conversation with all messages
  - Returns full conversation details including all messages
  - Messages include role, content, and timestamp

### Data Persistence
- **H2 Database**: File-based persistence at `./data/chatbot`
- **Flyway Migrations**: Version-controlled database schema
- **JPA/Hibernate**: Clean entity-based data modeling
- **Transaction Management**: ACID guarantees for conversation operations
- **Unicode Support**: Proper handling of emojis and multi-byte characters

### Security
- **Spring Security** configured with:
  - Content Security Policy (CSP)
  - XSS Protection
  - Frame Options (clickjacking prevention)
  - MIME type sniffing protection
  - Referrer Policy
- **CORS** enabled for development mode (localhost:3000)
- **Input Sanitization**: Control character removal in conversation titles
- **SPA routing** support for client-side navigation

### API Documentation
- **SpringDoc OpenAPI**: Interactive API documentation
- **Swagger UI**: Browser-based API testing interface
- **Complete Schemas**: Full request/response documentation
- **Example Requests**: Pre-populated examples for testing

### Frontend
- **Error pages**: Custom 404 and 500 error pages
- **Testing**: Jest and React Testing Library configured
- **TypeScript**: Strict type checking enabled
- **Static export**: No server-side rendering, fully static

### Development
- **Hot reload**: Both frontend and backend support hot reload in development mode
- **Separate dev servers**: Frontend (3000) and backend (8080) run independently
- **Automated builds**: Gradle automatically builds and integrates frontend
- **H2 Console**: Database inspection via web interface
- **Comprehensive Testing**: Spock/Groovy tests for backend, Jest for frontend
