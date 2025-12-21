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
- Description: Returns user information by user ID. Access is restricted based on the authenticated user.
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
- Description: Returns a paginated list of the authenticated user’s games filtered by status.
- URL: http://localhost:8080/api/user-games
- Method: GET
- Authentication: Required
