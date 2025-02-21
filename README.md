# Time Management Backend

## Overview
This is a Spring Boot application for managing time schedules. It includes authentication, timetable generation, and reservation functionalities. The application uses MySQL as the primary database and Redis for caching.

## Prerequisites
Ensure you have the following installed:
- Java 21
- Maven 3+
- MySQL
- Redis(Optional for dev env)

## Configuration
### Environment Variables
This project uses environment variables for configuration. You can define them in an `.env` file or set them directly in your environment.

### `application.properties`
By default, the application uses `application.properties.default`. Rename it to `application.properties` and update it with your database credentials if needed.

Example:
```properties
spring.application.name=time-management-backend
server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3307/pfm_time_management?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.sql.init.mode=always

springdoc.swagger-ui.path=/swagger-ui.html

spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.password=your_redis_password
```

## Security & Authentication
- The application uses JWT-based authentication.
- Each issued token has an **access validity** of 15 minutes.
- A **refresh token** is valid for **7 days**.
- There is a **session tied to the tokens**, allowing users to remain logged in until the session expires.
- A **sliding window mechanism** is implemented: each time a **refresh token** is used, the session is extended for another **7 days**.
- Users can log out using the `POST /logout` endpoint, which invalidates the session.
- Default admin user : username : admin@email.com, password : 123Aze456789@

## API Endpoints
### Authentication
- `POST /login` - Authenticate user and retrieve an access token.
- `POST /logout` - Logout the authenticated user.

### TimeTables
- `GET /api/v1/time-tables` - List all timetables.
- `POST /api/v1/time-tables/generate` - Generate a new timetable.
- `PUT /api/v1/time-tables/{id}` - Update a timetable.
- `DELETE /api/v1/time-tables/{id}` - Delete a timetable.

### Semesters
- `GET /api/v1/semesters` - List all semesters.
- `POST /api/v1/semesters` - Create a new semester.
- `POST /api/v1/semesters/{id}/generate-time-tables` - Generate timetables for a semester.

### Reservations
- `GET /api/v1/reservations` - List all reservations.
- `POST /api/v1/reservations` - Create a new reservation.
- `PUT /api/v1/reservations/{id}` - Update a reservation.

### Groups
- `GET /api/v1/groups` - List all groups.
- `POST /api/v1/groups` - Create a new group.

## Data Structures and Entities
### Essential Entities:
1. **User** - Represents users in the system (students, teachers, admins).
2. **Course** - Stores details about courses.
3. **Classroom** - Physical or virtual rooms where courses take place.
4. **Timetable** - Holds scheduling details for courses, classrooms, and teachers.
5. **Reservation** - Manages bookings for classrooms and time slots.
6. **Semester** - Represents academic terms in which courses are scheduled.
7. **Group** - A collection of students who share the same courses.
8. **Academic Class** - Links courses, groups, and teachers within a semester.

## Timetable Generation
1. **Input Data**: Courses, classrooms, teachers, and available time slots.
2. **Algorithm**: Uses a genetic algorithm to find optimal schedule arrangements.
3. **Validation**: Ensures no conflicts exist (e.g., teacher double-booking, room availability).
4. **Finalization**: The best timetable is selected and stored in the database.

## Reservation System
- Users can request time slots for courses.
- The system checks for conflicts and confirms availability.
- Reservations are stored in the database and cached in Redis for quick retrieval.

## Entity Relationships
- **Users** can be students, teachers, or admins.
- **Courses** are taught by one or multiple **teachers**.
- **Groups** contain multiple **students** and follow a **timetable**.
- **Semesters** consist of multiple **academic classes**.
- **Academic Classes** link **courses, teachers, and groups** within a **semester**.
- **Timetables** structure the scheduling of courses and are assigned to groups.
- **Classrooms** are assigned to courses in a timetable.
- **Reservations** ensure classrooms are properly booked.




