# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

A full-stack monorepo application combining a Spring Boot backend with a Next.js frontend for integrating with OpenAI's API.

**Technology Stack:**

**Backend:**
- Java 21
- Spring Boot 3.4.1
- Gradle 8.5 (with gradle-node-plugin 7.0.1)
- Spring Security 6 (security headers, CORS)
- OpenAI Java Client (theokanning.openai-gpt3-java v0.18.2)
- Lombok for reducing boilerplate
- Spring Boot Actuator for health checks
- Spock/Groovy for testing

**Frontend:**
- Next.js 14.2.33 (static export)
- React 18.3.1
- TypeScript 5
- Pages Router architecture
- Jest & React Testing Library for testing

## Essential Commands

### Running the Application

**Production Mode (Full Stack):**
```bash
# Windows
gradlew.bat build
gradlew.bat bootRun

# Linux/Mac
./gradlew build
./gradlew bootRun
```
Application runs on `http://localhost:8080` (serves frontend + API)

**Development Mode (Separate Servers):**

Terminal 1 - Backend:
```bash
# Windows
gradlew.bat bootRun

# Linux/Mac
./gradlew bootRun
```
Backend runs on `http://localhost:8080`

Terminal 2 - Frontend:
```bash
cd frontend
npm run dev
```
Frontend dev server runs on `http://localhost:3000`

### Building

The build process automatically:
1. Downloads Node.js and npm (first build only, managed by gradle-node-plugin)
2. Installs frontend dependencies (npm install)
3. Builds Next.js static export (npm run build)
4. Copies frontend output to `src/main/resources/static/`
5. Compiles backend and creates JAR with embedded frontend

```bash
# Windows
gradlew.bat clean build

# Linux/Mac
./gradlew clean build
```

Output: `build/libs/openai-chatbot-0.0.1-SNAPSHOT.jar`

### Testing

**Backend Tests (Spock/Groovy):**
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

**Frontend Tests (Jest):**
```bash
cd frontend
npm test              # Run all tests
npm run test:watch    # Run tests in watch mode
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

**Monorepo Structure:**
```
chatbot-openai/
├── src/main/java/com/openai/chatbot/    # Backend Java code
│   ├── controller/                       # REST API endpoints
│   ├── service/                          # Business logic
│   ├── config/                           # Spring configuration
│   ├── dto/                              # Data transfer objects
│   └── exception/                        # Exception handling
├── src/main/resources/
│   ├── application.properties            # Backend config
│   └── static/                          # Frontend build output (auto-generated)
├── frontend/                             # Next.js frontend
│   ├── pages/                           # Next.js pages
│   ├── public/                          # Static assets
│   ├── package.json                     # Frontend dependencies
│   └── next.config.js                   # Next.js configuration
└── build.gradle                         # Build configuration
```

**Backend Package Structure:**
- Base package: `com.openai.chatbot`
- Main class: `OpenaiChatbotApplication.java`
- Layered architecture: controllers → services → config
- Testing with Spock/Groovy

**Frontend Structure:**
- Pages Router architecture (Next.js 14.2.33)
- Static export (no SSR)
- TypeScript for type safety
- Error pages (404, 500)
- Jest & React Testing Library for testing
- Served by Spring Boot in production

**Build Integration:**
- gradle-node-plugin manages Node.js/npm
- Frontend builds automatically during Gradle build
- Static files copied to `src/main/resources/static/`
- Single JAR contains both frontend and backend

**Security Configuration:**
- **Spring Security** with security headers:
  - Content Security Policy (CSP)
  - XSS Protection
  - Frame Options (clickjacking prevention)
  - MIME type sniffing protection
  - Referrer Policy
- **CORS** configured for development mode (localhost:3000)
- **SPA routing** controller for client-side routing support

**Dependencies:**
- Spring Boot Web for REST APIs
- Spring Boot Validation for request validation
- Spring Boot Actuator for monitoring
- Spring Boot Security for security headers and CORS
- Spring Boot DevTools for development hot reload
- Lombok annotations are available for reducing boilerplate
- Next.js, React, TypeScript for frontend

## Development Notes

**Backend:**
- Uses Lombok: Enable annotation processing in your IDE
- Spring DevTools is included for automatic restart during development
- Logging: Root level is INFO, `com.openai.chatbot` package is set to DEBUG
- Health check available at: `http://localhost:8080/actuator/health`

**Frontend:**
- Node.js and npm are managed automatically by gradle-node-plugin (no manual installation needed)
- First build downloads Node.js (~2-3 minutes), subsequent builds are faster
- Frontend dev server supports hot reload
- Static export means no SSR - all rendering happens client-side
- TypeScript is configured for strict type checking
- Jest & React Testing Library configured for component testing
- Custom error pages (404, 500) for better UX

**Security:**
- Spring Security is configured with CSP, XSS protection, and frame options
- CORS is enabled for development mode (frontend dev server on port 3000)
- API endpoints at `/api/**` have CSRF protection disabled (stateless REST API)
- All static resources are publicly accessible
- SPA routing controller forwards non-API routes to index.html

**Gradle Tasks:**
- `gradlew installFrontendDependencies` - Install npm packages
- `gradlew buildFrontend` - Build Next.js static export
- `gradlew copyFrontendToStatic` - Copy frontend to backend
- `gradlew cleanFrontend` - Clean frontend artifacts
- `gradlew devFrontend` - Run Next.js dev server
- `gradlew build` - Complete build (frontend + backend)

**Frontend npm Scripts:**
- `npm run dev` - Start Next.js dev server on port 3000
- `npm run build` - Build Next.js static export
- `npm test` - Run Jest tests
- `npm run test:watch` - Run Jest in watch mode
- `npm run lint` - Lint frontend code

**Development Workflow:**
1. For backend-only work: Just run `gradlew bootRun`
2. For frontend-only work: Run `cd frontend && npm run dev`
3. For full-stack development: Run both in separate terminals
4. For production build: Run `gradlew clean build`
5. For testing: Run `gradlew test` (backend) or `cd frontend && npm test` (frontend)
