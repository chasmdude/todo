# Secfix TODOs Application

Purpose of the application is to expose endpoints for managing the To-do tasks of organization employees (aka User(s) on this application).

The application gives the possibility to the user to define Github repositories records (through the `/code-repository` endpoints) and then link them to Tasks.

A cron job (`CodeRepositoriesSyncCronJob.java` class) is running every hour and syncs data (e.g. url, open issues count, stars count) about the Github repositories.

**Notes**

- If a Github repository is not found when calling the Github api, its status is changed to `INVALID`.
- Users can use only link tasks to code repositories whose status = `ACTIVE`.
- At the moment the application is open to the public, there is no authentication/ authorization

**Exposes endpoints for managing:**

- Users
  - Get users
  - Get a user by id
  - Create user
  - Update user
  - Delete user
- Tasks
  - Get tasks
  - Get a task by id
  - Create task
  - Update task
  - Delete task
- Code Repositories
  - Get code respositories
  - Get a repository by id
  - Create repository
  - Update repository (name, owner, status)
  - Delete repository. (hard deletes the repository)

## Swagger

Once you start the application, you can try out the endpoints by navigating to the [swagger documentation](http://localhost:8080/todos-api/swagger-ui/index.html)

## Setup requirements

- Java 21
- Maven 3.9.9
- Springboot 3.3.4
- Postgresql database
- Export the following env variables:
  - `DATABASE_URL`
    - sample value: `jdbc:postgresql://localhost:5432/todos-app?useSSL=false`
  - `DATABASE_USERNAME`
  - `DATABASE_PASSWORD`

## Docker
### Running the Application

To run the application using Docker Compose, follow these steps:

1. **Build and start the app**:
   ```sh
   docker-compose up app --build
2. **Stop the app**:
   ```sh
   docker-compose down
   ```
### Testing 
1. **Run the tests**:
   ```sh
   docker-compose up test --build
   ```
2. **Stop running the tests**:
   ```sh
   docker-compose down
   ```
**Tip**

You can use [asdf](https://asdf-vm.com/) to run the application using specific java and maven versions.
To run we used following:

- Java: oracle-21.0.2
- Maven: 3.9.9
