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

## API Endpoint Details

### Hello world

- Description: Returns a simple "Hello World!" message.
- URL: `http://localhost:8080/api/hello`
- Method: GET
- Response:

```json
"Hello World!"
```
