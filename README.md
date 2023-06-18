# User Management System

This is a User Management System application that allows you to manage users and departments. It provides RESTful APIs for user and department operations, authentication using JWT, and statistical information about users and departments.

## Table of Contents

- [Entities](#entities)
- [Database Migration](#database-migration)
- [Technologies](#technologies)
- [Controllers](#controllers)
- [Statistics](#statistics)
- [Authentication](#authentication)

## Entities

The application includes the following entities:

- User: Represents a user with properties such as role, username, password, name, salary, and department ID.
- Department: Represents a department with properties such as name.

## Database Migration

The application uses Flyway for database migration. The migration script `V1_1__create_tables.sql` creates the necessary tables for the User and Department entities.

## Technologies

The project utilizes the following technologies:

- PostgreSQL: The chosen database management system.
- JDBC: Used to interact with the PostgreSQL database.
- Flyway: Handles database migration and versioning.
- JWT (JSON Web Token): Provides authentication and authorization functionality.

The project's `pom.xml` specifies the following libraries and versions:

- Spring Boot Starter Parent: 2.7.12
- Java: 1.8
- Spring Boot Starter Security
- Spring Boot Starter JDBC
- Spring Boot Starter Validation
- Flyway Core
- Spring Boot Starter Web
- Microsoft SQL Server JDBC Driver
- PostgreSQL JDBC Driver
- Spring Boot Starter Test
- Spring Security Test
- jjwt-api: 0.11.5
- jjwt-impl: 0.11.5
- jjwt-jackson: 0.11.5
- Jackson Datatype JSR310
- JUnit: 4.13.2

Please ensure that you have the required versions of Java and Maven installed to run the project successfully.
## Controllers

The project includes the following controllers for managing users, departments, and authentication:

- UserController: Handles user-related operations such as retrieving all users, retrieving a user by ID, deleting a user, and registering a new user.
- DepartmentController: Manages department-related operations including retrieving all departments, retrieving a department by ID, creating a new department, updating a department, and deleting a department.
- AuthController: Responsible for user authentication and generates a JSON Web Token (JWT) for authorized users.

Detailed information about the API endpoints can be found in the respective controller classes.
### UserController

The UserController provides the following endpoints:

- `GET /api/users`: Retrieves all users.
- `GET /api/users/{id}`: Retrieves a user by ID.
- `DELETE /api/users/{id}`: Deletes a user by ID.
- `GET /api/users/department/{departmentName}`: Retrieves users by department name.
- `POST /api/users/registerUser`: Registers a new user.

### DepartmentController

The DepartmentController provides the following endpoints:

- `GET /api/departments`: Retrieves all departments.
- `GET /api/departments/{id}`: Retrieves a department by ID.
- `POST /api/departments`: Creates a new department.
- `PUT /api/departments/{id}`: Updates a department by ID.
- `DELETE /api/departments/{id}`: Deletes a department by ID.

Please refer to the respective controller classes for more details on the request and response structures.

## Statistics

The project includes a StatisticsController that provides statistical information about users and departments:

- `/api/statistics/average-salary-per-department`: Retrieves the average salary per department.
- `/api/statistics/average-salary/{departmentId}`: Retrieves the average salary for a specific department.

## Authentication

The application uses JWT for authentication. The AuthController provides the `/api/auth/signing` endpoint for user authentication. Upon successful authentication, a JWT token is generated, which can be used to authorize further requests.

Note: Only users with the role of "ADMIN" are authorized to access certain endpoints.

