# EventHub Backend

A Spring Boot REST API for the EventHub application.

## Tech Stack

* Java 21
* Spring Boot 4
* Spring Data JPA
* Spring Security with JWT Authentication
* PostgreSQL
* Lombok

## Prerequisites

* Java Development Kit (JDK) 21
* Maven
* PostgreSQL database

## Setup and Running

1. Navigate to the project directory.
2. Update database connection settings in `src/main/resources/application.properties` or `application.yml` to match your local PostgreSQL setup.
3. Build the project using Maven:
   ```bash
   ./mvnw clean install
   ```
4. Start the application:
   ```bash
   ./mvnw spring-boot:run
   ```

The application will start on the default port (typically 8080).

## Project Structure

* `src/main/java`: Contains the application source code (controllers, services, repositories, models).
* `src/main/resources`: Contains configuration files.
* `pom.xml`: Maven dependencies and build configuration.

## API Endpoints

### Authentication
* `POST /auth/register`: Register a new user.
* `POST /auth/login`: Authenticate a user and receive a JWT token.

### Events
* `GET /api/event/allEvent`: Fetch a list of all events.
* `GET /api/event/{eventId}`: Fetch details of a specific event by its ID.
* `POST /api/event/saveEvent`: Create a new event.
* `GET /api/event/organizerEvent`: Fetch all events created by the logged-in organizer.

### Registrations
* `POST /api/registrations/{eventId}`: Register the logged-in user for a specific event.
* `GET /api/registrations/userRegistrations`: Get all events the logged-in user is registered for.
* `GET /api/registrations/event/{eventId}`: Get all users registered for a specific event.

### Admin
* `GET /api/admin/pending-organizers`: Get a list of organizers pending approval.
* `POST /api/admin/approve/{userId}`: Approve an organizer's account.
