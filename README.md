# DevFlow – Project & Issue Management REST API

DevFlow is a backend REST API application built using Java and Spring Boot to manage projects, issues, team collaboration, and sprint workflows.

It provides secure APIs for managing project tasks, tracking issue progress, assigning team members, and monitoring project activities.

## Features

### Authentication & Authorization
- User registration and login
- JWT-based authentication
- Role-based access control (Admin, Manager, Developer)
- Password encryption using BCrypt

### Project Management
- Create, view, update, and delete projects
- Manage project-related issues
- Prevent deletion of projects with linked issues

### Issue Management
- Create, update, view, and delete issues
- Manage issue status and priority
- Filter issues by status, priority, assignee, and labels
- Search issues by title
- Pagination and sorting
- Track overdue issues

### Team Collaboration
- Add and manage issue comments
- Ownership-based comment authorization
- Upload, download, and delete attachments
- Create and assign labels to issues

### Kanban Board
- Organize issues by workflow status
- Update issue status
- Track issue status changes

### Sprint Management
- Create and manage sprints
- Assign issues to sprints
- Start and complete sprints
- Track sprint progress

### Dashboard & Activity Tracking
- Project and issue summary
- Priority and assignee statistics
- Sprint progress overview
- Track issue activities with pagination

## Technology Stack

| Technology | Purpose |
|---|---|
| Java | Programming language |
| Spring Boot | Backend framework |
| Spring Security | Authentication and authorization |
| Spring Data JPA | Database operations |
| Hibernate | ORM |
| MySQL | Relational database |
| JWT | Token-based authentication |
| Maven | Build and dependency management |
| Swagger / OpenAPI | API documentation |
| Postman | API testing |

## Project Architecture

The application follows a layered architecture:

- **Controller:** Handles HTTP requests and responses.
- **Service:** Contains business logic.
- **Repository:** Handles database operations.
- **Entity:** Represents database tables.
- **DTO:** Transfers data between API layers.
- **Security:** Handles JWT authentication and authorization.
- **Exception:** Centralized exception handling.

## Database Configuration

1. Create a MySQL database:

   `CREATE DATABASE devflow_db;`

2. Configure the following environment variables before running the application:

   - `DB_PASSWORD` – MySQL database password
   - `JWT_SECRET` – Secret key used for JWT signing

The application reads these values through environment variables rather than storing credentials directly in the source code.

## Running the Application

1. Clone the repository:

   `git clone https://github.com/amisha-15/devflow-backend.git`

2. Open the project in Eclipse or another Java IDE.

3. Configure the required environment variables: `DB_PASSWORD` and `JWT_SECRET`.

4. Make sure MySQL is running and the `devflow_db` database exists.

5. Run `DevflowBackendApplication.java` as a Java application.

The application starts on:

`http://localhost:8080`

## API Documentation

Swagger UI:

`http://localhost:8080/swagger-ui/index.html`

Open Swagger UI to explore and test the available REST API endpoints.

## API Testing

The APIs were tested using Postman, covering authentication, project management, issue management, comments, attachments, labels, sprints, dashboard, and activity tracking.

## Repository

GitHub: https://github.com/amisha-15/devflow-backend

---

**DevFlow – A Project and Issue Management REST API built with Java and Spring Boot.**