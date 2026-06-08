## [1.9.10] - 2026-06-08
1. added Docker

## [1.9.9] - 2026-06-08
1. added cross origin in deployment 

## [1.9.8] - 2026-06-08
1.added Dockerfile

## [1.9.7] - 2026-06-08
1.changes the project structure

## [1.9.6] - 2026-06-08
1. Change mysql connection 

## [1.9.5] - 2026-06-08
1.Replaced MySQL driver with PostgreSQL driver in pom.xml
2.Added DevTools dependency in pom.xml
3.Updated application.properties — PostgreSQL datasource URL, driver and dialect

## [1.9.4] - 2026-06-08
1.Added PATCH method to CORS allowed methods in SecurityConfig.java

## [1.9.3] - 2026-06-07
1.Replaced OpenAI with Google Gemini AI (free tier) in AiService.java
2.Updated API request format to Gemini structure in AiService.java
3.Updated application.properties with Gemini API key, URL and model

## [1.9.2] - 2026-06-06
1.Remove API key from configuration and added environment variable.

## [1.9.1] - 2026-06-06

### Added
- 1. application.properties — MySQL datasource, JWT secret/expiration, OpenAI config, CORS origin
- 2. MySQL database auto-created via createDatabaseIfNotExist=true
- 3. Hibernate ddl-auto=update — tables auto-created on first run
- 4. README.md — setup instructions, API endpoints, DB schema, AI explanation

### Changed
- 1. Finalized project structure — controller → service → repository → entity layers

### Fixed
- N/A

## [1.9.0] - 2026-06-06

### Added
- 1. Created AuthController — POST /api/auth/register, POST /api/auth/login
- 2. Created TaskController — GET /api/tasks, GET /api/tasks/{id}, POST /api/tasks, PUT /api/tasks/{id}, DELETE /api/tasks/{id}, PATCH /api/tasks/{id}/status
- 3. Created AiController — POST /api/ai/generate

### Changed
- 1. All task endpoints secured with @AuthenticationPrincipal to get logged-in user
- 2. Auth endpoints return HTTP 201 Created on register, 200 OK on login

### Fixed
- N/A

## [1.8.0] - 2026-06-06

### Added
- 1. Created AiService — sends task title to OpenAI GPT-3.5-turbo
- 2. Prompt returns description, priority, estimatedTime as JSON
- 3. Graceful fallback response if OpenAI API is unavailable or key is missing

### Changed
- 1. OpenAI API URL, model, and key are configurable via application.properties
- 2. Response cleaned of markdown code fences before JSON parsing

### Fixed
- N/A

## [1.8.0] - 2026-06-06

### Added
- 1. Created TaskService — full CRUD for tasks
- 2. getAllTasks — returns all tasks belonging to logged-in user
- 3. getTaskById — returns single task only if it belongs to the user
- 4. createTask — creates task linked to logged-in user
- 5. updateTask — updates all task fields
- 6. deleteTask — deletes task only if owned by user
- 7. updateTaskStatus — updates only the status field (TODO / IN_PROGRESS / DONE)

### Changed
- 1. Task ownership enforced — users can only access their own tasks

### Fixed
- N/A

## [1.7.0] - 2026-06-06

### Added
- 1. Created AuthService — register and login logic
- 2. Register: checks duplicate username/email, hashes password, saves user, returns JWT
- 3. Login: authenticates via AuthenticationManager, returns JWT token

### Changed
- 1. Password stored as BCrypt hash — never plain text

### Fixed
- N/A

## [1.6.0] - 2026-06-06

### Added
- 1. Created ResourceNotFoundException — thrown when task or user not found (404)
- 2. Created BadRequestException — thrown for duplicate username/email or invalid input (400)
- 3. Created GlobalExceptionHandler — handles all exceptions with structured JSON responses

### Changed
- 1. Validation errors from @Valid return field-level error map
- 2. All error responses include timestamp, status, message fields

### Fixed
- N/A

## [1.5.0] - 2026-06-06

### Added
- 1. Created SecurityConfig — Spring Security filter chain
- 2. Configured CORS — allowed origins, methods, headers from properties
- 3. Configured stateless session management (JWT-based, no HTTP session)
- 4. Permitted /api/auth/** publicly, all other routes require JWT

### Changed
- 1. Password encoding using BCryptPasswordEncoder
- 2. JwtAuthFilter registered before UsernamePasswordAuthenticationFilter

### Fixed
- N/A

## [1.4.0] - 2026-06-06

### Added
- 1. Created JwtUtils — generateToken, getUsernameFromToken, validateToken
- 2. Created JwtAuthFilter — extracts Bearer token and sets SecurityContext
- 3. Created CustomUserDetailsService — loads user by username from database

### Changed
- 1. JWT signed using HMAC-SHA256 with secret from application.properties
- 2. Token expiration configurable via jwt.expiration property

### Fixed
- N/A

## [1.3.0] - 2026-06-06

### Added
- 1. Created RegisterRequest DTO — username, email, password with validation annotations
- 2. Created LoginRequest DTO — username, password
- 3. Created AuthResponse DTO — token, username, email, message
- 4. Created TaskRequest DTO — title, description, priority, dueDate, status
- 5. Created TaskResponse DTO — all task fields + username
- 6. Created AiTaskRequest DTO — title field
- 7. Created AiTaskResponse DTO — description, priority, estimatedTime, aiGenerated

### Changed
- 1. Applied @NotBlank, @Email, @Size, @NotNull validations on request DTOs

### Fixed
- N/A

## [1.2.0] - 2026-06-06

### Added
- 1. Created UserRepository — findByUsername, findByEmail, existsByUsername, existsByEmail
- 2. Created TaskRepository — findByUserId, findByUserIdAndStatus, findByIdAndUserId

### Changed
- 1. Extended JpaRepository for both repositories

### Fixed
- N/A

## [1.1.0] - 2026-06-06

### Added
- 1. Created User entity with fields: id, username, email, password, createdAt
- 2. Created Task entity with fields: id, title, description, priority, dueDate, status, createdAt, updatedAt, userId

### Changed
- 1. Defined TaskStatus enum — TODO, IN_PROGRESS, DONE
- 2. Defined Priority enum — LOW, MEDIUM, HIGH

### Fixed
- N/A

## [1.0.0] - 2026-06-06

### Added
1. Initial Spring Boot Maven project setup