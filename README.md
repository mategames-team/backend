# VideoGames Catalogue Backend

## Running the backend locally
1. Before running the backend, make sure the following tools are installed:
### Java 17 or higher
Check if installed:
```bash
java -version
```
Expected:
```bash
openjdk version "17.x.x"
```
If Java is missing or the version is lower than 17, download it:
https://www.oracle.com/java/technologies/downloads/

### Maven
Maven is used to build and run the application.

Check if installed:
```bash
mvn -version
```

Expected:
```bash
Apache Maven 3.x.x
```

If Maven is missing, install it:
https://maven.apache.org/download.cgi

### Git

Used to clone the project.

Check if installed:
```bash
git --version
```

Expected:
```bash
git version 2.x.x
```

If Git is not installed:
https://git-scm.com/


2. Clone the repository:
   ```bash
   git clone https://github.com/mategames-team/backend.git
   cd backend
   git checkout dev
   ```
   
3. Build and run the project using Maven:
    ```bash
   mvn spring-boot:run
   ```
## Swagger documentation
To test endpoints use Swagger documentation by link (when running the app locally, instructions below):
http://localhost:8080/api/swagger-ui/index.html
### How to Use the API Documentation (Swagger UI)
Important! To run manual update of the database using AdminGameController endpoints, you need to log in as ADMIN. 
This API uses JWT authentication.
To access protected endpoints, you must register, log in, and authorise Swagger with your token.

1️⃣ Register a New User
- Open Swagger UI
- Find the Authentication section
- Click POST /auth/register
- Click “Try it out”
- Fill in the request body
- Click Execute 
✅ If registration is successful, the user is created.

2️⃣ Log In and Get JWT Token
- In the Authentication section
- Open POST /auth/login
- Click “Try it out”
- Enter your credentials
- Click Execute
📌 The response will contain a JWT token, for example:
{
"token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
👉 Copy this token

3️⃣ Authorise Swagger with JWT Token
- At the top-right corner of Swagger UI, click the 🔒 Authorize button
- In the popup window:
- Paste your token in this format:
- Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
⚠️ Important: The word Bearer and a space must be included
- Click Authorize
- Click Close
✅ Swagger is now authorised

4️⃣ Call Protected Endpoints
Once authorised, you can access endpoints that require authentication.
📌 Swagger will now automatically attach the JWT token to each authorised request.

5️⃣ Logging Out / Token Expiry
If you refresh the page or your token expires:
- Click 🔒 Authorize again
- Paste a new token

## API Endpoint Details. 
## GameController

### getAllGamesFromDb
- Description: Returns a page of games from DB.
- URL: `http://localhost:8080/api/games/local`
- Method: GET
- Authentication: Not required

### getByApiId

- Description: Returns a game from db by its apiId if the game is in db. Fetches the game from API if there is no game by the provided apiId.
- URL: `http://localhost:8080/api/games/{gameApiId}`
- Method: GET
- Authentication: Not required

### getAllGamesFromApi
- Description: Returns a paginated list of games fetched from the external API.
- URL: http://localhost:8080/api/games
- Method: GET
- Authentication: Not required

### search
- Description: Searches local database games using search parameters.
- URL: http://localhost:8080/api/games/local/search
- Method: GET
- Authentication: Not required

### apiSearch
- Description: Searches games directly from the external API.
- URL: http://localhost:8080/api/games/search
- Method: GET
- Authentication: Not required

## AuthenticationController
### registerUser

- Description: Registers a new user.
- URL: http://localhost:8080/api/auth/registration
- Method: POST

### login
_ Description: Authenticates a user and returns JWT token.
- URL: http://localhost:8080/api/auth/login
- Method: POST

## CommentController
### createComment
- Description: Creates a new comment for a specific game.
- URL: http://localhost:8080/api/comments/games/{gameApiId}
- Method: POST
- Authentication: Required

### getCommentsForGame
- Description: Returns a paginated list of comments for a specific game.
- URL: http://localhost:8080/api/games/{gameApiId}/comments
- Method: GET
- Authentication: Not required

### getUserComments
- Description: Returns a paginated list of comments created by the authenticated user.
- URL: http://localhost:8080/api/comments
- Method: GET
- Authentication: Required

### updateComment
- Description: Updates an existing comment owned by the authenticated user.
- URL: http://localhost:8080/api/comments/{commentId}
- Method: PATCH
- Authentication: Required

### deleteComment
- Description: Deletes a comment owned by the authenticated user.
- URL: http://localhost:8080/api/comments/{commentId}
- Method: DELETE
- Authentication: Required

## UserController
### getUserInfo
- Description: Returns user information by user ID. Any authenticated user can see other user's profile info.
- URL: http://localhost:8080/api/users/{id}
- Method: GET
- Authentication: Required

### updateUserInfo
- Description: Updates the authenticated user’s profile information (profileName, email, about, location).
- URL: http://localhost:8080/api/users/me
- Method: PATCH
- Authentication: Required

### changePassword
- Description: Changes the authenticated user’s password.
- URL: http://localhost:8080/api/users/me/password
- Method: PATCH
- Authentication: Required

## UserGameController
### createOrUpdateUserGame
- Description: Creates or updates a game associated with the authenticated user.
- URL: http://localhost:8080/api/user-games
- Method: POST
- Authentication: Required

### deleteUserGame
- Description: Removes a game from the authenticated user’s list.
- URL: http://localhost:8080/api/user-games/{id}
- Method: DELETE
- Authentication: Required

### getUserGamesByStatus
- Description: Returns a paginated list of any user’s games filtered by status.
- URL: http://localhost:8080/api/user-games
- Method: GET
- Authentication: Required

## AdminGameController
Provides administrative endpoints for managing game data fetched from an external API. These endpoints are intended only for administrators and are protected by role-based security.
Important! To run manual update of the database using AdminGameController endpoints, you need to log in as ADMIN.

### fetchBestGamesManually
- Description: Fetches the current list of best games from the external API and saves only games that do not already exist in the database.
- URL: http://localhost:8080/api/admin/fetch-best-games
- Method: POST
- Authentication: Required

### fetchAndUpdateAllGamesManually
- Description: Fetches the best games from the external API and updates existing records, while also saving any newly discovered games.
- URL: http://localhost:8080/api/admin/fetch-update-best-games
- Method: POST
- Authentication: Required

## Automatic Game Fetch on Application Startup
The application is configured to automatically fetch best games from the external API when the backend starts.
# How It Works
- The main Spring Boot application class implements CommandLineRunner
- On startup, Spring executes the run() method
- This triggers an automatic call to gameService
- As a result, the application ensures that the database is initially populated with best games without requiring any manual admin action.

## Scheduled Daily Game Fetch
The application includes a scheduled task that automatically fetches best games once per day.
- Time: Every day at 06:00 AM
- Time zone: Europe/Kyiv
- Trigger mechanism: Spring’s @Scheduled with a cron expression
- The scheduler runs automatically without any manual intervention
- Only new games are saved
