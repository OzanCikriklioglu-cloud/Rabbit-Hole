Personal Secure Vault




This is a Spring Boot web application designed to store private notes securely. I built it with a heavy focus on security best practices, including protection against common web vulnerabilities, user authentication, and data isolation.

Key Features
Secure Authentication: User registration and login using Spring Security.

Role-Based Access: Standard users can manage their own notes, while Admins have access to a user management panel.

Google reCAPTCHA: Integrated into the login flow to prevent brute-force and bot attacks.

Data Isolation: Users can only see, add, or delete their own notes. I've implemented server-side checks to prevent users from deleting notes belonging to others by manipulating IDs.

Security Headers: Uses CSP (Content Security Policy), HSTS, and Frame Options to prevent XSS, Clickjacking, and Sniffing attacks.

Session Management: Strictly limited to one session per user with a 15-minute timeout.

Tech Stack
Java 21 & Spring Boot 3

Spring Security (Authentication & Authorization)

PostgreSQL (Database)

Flyway (Database Migrations)

Thymeleaf (Frontend Templates)

Lombok (To keep the code clean)

Project Structure
config/: Security filters and general configuration.

controller/: Routes for Auth, Admin panel, and User Dashboard.

service/: Business logic (Captcha verification, user registration, note management).

repository/: Database communication (using JpaRepository).

entity/: Database models for Users and Notes.

Setup Instructions
Database: Make sure you have a PostgreSQL database ready.

Environment Variables: Create a .env.properties file in the root directory (or update application.properties) with the following keys:

DB_URL: Your Postgres connection string.

DB_USERNAME: Database username.

DB_PASSWORD: Database password.

CHAPTHA_API: Your Google reCAPTCHA Secret Key.         #you dont need to do  and please disable chaptafilter from SecurityConfig.java

CHAPTHA_HTML_API: Your Google reCAPTCHA Site Key.      #you dont need to do  and please disable chaptafilter from SecurityConfig.java

Run: Execute ./mvnw spring-boot:run or run the SecurityApplication.java file from your IDE.

Security Measures implemented
Password Hashing: Uses BCryptPasswordEncoder (never stores plain-text passwords).

SQL Injection Protection: Uses Spring Data JPA and parameterized queries.

CSRF Protection: Enabled by default via Spring Security.

Information Leakage: A GlobalErrorController is used to catch exceptions and show a generic error page instead of leaking system stack traces.

Secure Cookies: HttpOnly, Secure, and SameSite=Strict flags are set to protect session cookies.
