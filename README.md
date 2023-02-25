# Spring Boot + Firebase Auth and JWT (Spring Boot 3+/Security 6+)

Provides example how to configure API using Spring MVC and Spring Security as resource server so that it does verify JWT using firebase public keys. It doesn't require third party libraries.

### Prerequisites

In `application.yml` file configure firebase `project-id` and `project-key` 

### Endpoints

- Register (Sign Up)
  ```
  curl --request POST \
    --url http://localhost:8080/register \
    --header 'Content-Type: application/json' \
    --data '{
      "email": "foo-bar@example.com",
      "password": "foo-bar@example.com"
    }'
  ```

- Login (Fetch token)
  ```
  curl --request POST \
    --url http://localhost:8080/token/fetch \
    --header 'Content-Type: application/json' \
    --data '{
      "email": "foo-bar@example.com",
      "password": "foo-bar@example.com"
    }'
  ```

- Refresh token
    ```
    curl --request POST \
      --url http://localhost:8080/token/refresh \
      --header 'Content-Type: application/json' \
      --data '{
        "refresh_token": "...."
    }'
    ```

- Access private endpoint
    ```
    curl --request GET \
      --url http://localhost:8080/private \
      --header 'Authorization: Bearer <token_goes_here>'
    ```