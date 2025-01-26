# Courier Tracker API

This application tracks couriers' geolocation data (time, courier, latitude, longitude) and logs the courier-store interaction when any courier enters a 100-meter radius of Migros stores. Reentries to the same store's circumference within 1 minute are not counted as "entrances." The application also provides a way to query the total distance traveled by any courier.

---

## Architecture

### Hexagonal Architecture (Ports and Adapters)

The application follows **Hexagonal Architecture**, also known as Ports and Adapters. This pattern emphasizes the separation of core business logic from external dependencies (such as databases, web services, and file systems). By using interfaces (ports), the core logic interacts with these external systems through adapters, promoting flexibility and testability.

- **Ports** are interfaces that define how the core business logic communicates with external components. For example, it uses ports to retrieve store data and log courier interactions.

- **Adapters** implement these ports and handle the actual interaction with external systems, such as the H2 relational database or JSON file storage.

### H2 Relational Database

The application uses an in-memory **H2 relational database** to store data about couriers' interactions with stores and their distances traveled.

### Singleton Pattern

The **Singleton Pattern** is used in the `StoreRepository` class to ensure that the store data (from `stores.json`) is loaded only once. This avoids redundant loading of the data and ensures consistent access throughout the application.

### Factory Pattern

The **Factory Pattern** is used to create the appropriate `DistanceCalculator` depending on the configuration. This allows for easy extensibility, enabling the addition of new distance calculation strategies (like Haversine or others) in the future.

### Port-Adapter (Hexagonal) Architecture

- **Ports** and **Adapters** enable the separation of the application's core logic from its dependencies (e.g., database, web controllers). This results in cleaner code and makes it easier to replace or mock external systems during testing.

### Request Validation

To ensure that the data provided in API requests is valid, the application performs request validation for the courier's location tracking.

### Exception Handling

The application features robust exception handling to manage various error scenarios and provide clear, consistent responses to clients. The system captures different exceptions that might occur during execution and returns structured error messages to the user.

### Global Exception Handler

A **global exception handler** is implemented using Spring’s `@RestControllerAdvice`. This handler intercepts exceptions thrown from any part of the application and ensures that the response is uniform, regardless of the exception type.

The handler logs the exception details, including a **unique Trace ID**, which helps in tracking the error for debugging purposes.

### Handled Exceptions

1. **Generic Exception (500 Internal Server Error)**:
    - Catches any unhandled exceptions in the application.
    - A generic message is returned with a status code of `500 Internal Server Error`.
    - Example:
      ```json
      {
        "traceId": "b321492f-3eed-4f1f-b991-880331e98de3",
        "detail": {
          "code": "COURIER-TRACKER-API-5000",
          "message": "Cannot invoke \"String.toString()\" because \"response\" is null"
        },
        "exception": "NullPointerException",
        "occurredAt": "2025-01-26T14:33:38.015199"
      }
      ```

2. **Courier Near Store Conflict (409 Conflict)**:
    - Handles exceptions thrown when a courier is near a store within the restricted radius and a conflict occurs (e.g., a conflict error due to repeated entrances).
    - Status code `409 Conflict` is returned.
    - Example:
      ```json
      {
        "traceId": "990c9409-1410-4e12-88ae-b9c7b5173ec0",
        "detail": {
          "code": "COURIER-TRACKER-API-4009",
          "message": "Courier cannot operate in the specified area (lat: 40.986106, lng: 29.116129) within the last minute."
        },
        "exception": "CourierNearStoreException",
        "occurredAt": "2025-01-26T14:32:10.40379"
      }
      ```

3. **Store List is Empty (204 No Content)**:
    - Handles the case when the list of stores is empty (e.g., if no store data is available in the `stores.json` file).
    - Returns a status code of `204 No Content`.

4. **Invalid Method Argument (400 Bad Request)**:
    - Handles validation errors when incoming request parameters are invalid (e.g., incorrect courier ID, invalid latitude/longitude, etc.).
    - Status code `400 Bad Request` is returned.
    - Example:
      ```json
      {
        "traceId": "5b52c615-4cde-42df-a7b3-93ffdac8383b",
        "detail": {
          "code": "COURIER-TRACKER-API-4000",
          "message": "lat: must not be null, courierId: must not be blank"
        },
        "exception": "MethodArgumentNotValidException",
        "occurredAt": "2025-01-26T13:56:04.898696"
      }
      ```

### Custom Exception Classes

The application defines custom exception classes such as:
- **`CourierNearStoreException`**: Thrown when a courier re-enters the restricted radius of a store too soon.
- **`StoreListEmptyException`**: Thrown when the store data is missing or the store list is empty.
- **`MethodArgumentNotValidException`**: Handles validation errors related to incorrect or missing request data.

### Global Error Response Structure

All exceptions are handled by returning a **structured error response** containing the following fields:
- **`traceId`**: A unique identifier for tracking the error.
- **`errorDetail`**: Contains the error code and a message describing the issue.
- **`exception`**: The type of exception that was thrown.
- **`occurredAt`**: The timestamp of when the error occurred.

---

## Prerequisites

Before running the application, make sure you have the following installed:

- **Java 21+** (JDK)
- **Maven** (for building the project)
- **Spring Boot 3.x**

## StoreController Overview

The `StoreController` is a Spring `@RestController` that defines an API endpoint for retrieving store data. It serves as the entry point for external requests related to store information and delegates business logic to the associated use case handler.

### Key Components:

- **`@RestController`**:
    - This annotation marks the class as a Spring MVC controller, meaning each method can handle HTTP requests and return responses. It also automatically converts return values to JSON format and handles HTTP responses.

- **`@RequiredArgsConstructor`**:
    - This Lombok annotation generates a constructor that automatically injects required dependencies (final fields). It simplifies the process of setting up constructor-based dependency injection.

- **`@RequestMapping("/v1/store")`**:
    - This annotation defines the base path for all endpoints within this controller. All API endpoints in this controller will be prefixed with `/v1/store`.

---

### Endpoint Details

#### `GET /v1/store/all`

- **Purpose**: Fetch a list of all stores.
- **Method**: `GET`
- **Response Status**: `200 OK`
- **Response Body**: A JSON array containing a list of `StoreModel` objects.

## CourierController Overview

The `CourierController` is a Spring `@RestController` that provides endpoints for managing courier location data and calculating total travel distance. It exposes two primary functionalities: logging courier location and retrieving the total distance traveled by a courier.

### Key Components:

- **`@RestController`**:
    - Marks the class as a Spring controller that handles HTTP requests and responses.

- **`@Validated`**:
    - Enables validation of request bodies based on annotations like `@Valid` or `@NotBlank` in the controller.

- **`@RequiredArgsConstructor`**:
    - Lombok annotation that generates a constructor to automatically inject the required dependencies (final fields).

- **`VoidUseCaseHandler`** and **`UseCaseHandler`**:
    - These are generic handlers for use cases, providing abstraction for handling specific use cases like logging location and calculating total distance.

---

### Endpoints

#### `POST /v1/courier/log-instant-location`

- **Purpose**: Logs a courier’s instant location.
- **Request Body**:
    - A JSON object with the following fields:
        - `courierId`: The ID of the courier (required).
        - `lat`: The latitude of the courier's location (required).
        - `lng`: The longitude of the courier's location (required).
        - `time`: The timestamp of the location (required).
- **Response**:
    - Status code `201 Created` if the location is successfully logged.
- **Action**:
    - The method creates a `CourierLogLocationUseCase` from the request body and passes it to the `courierLogLocationVoidUseCaseHandler` for processing.

#### `GET /v1/courier/{courierId}/totalDistance`

- **Purpose**: Retrieves the total travel distance of a courier.
- **Path Variable**:
    - `courierId`: The unique identifier of the courier whose total distance is to be calculated (required).
- **Request Parameters**:
    - `distanceUnit`: Optional parameter to specify the unit for the distance. Defaults to `"km"`. Accepts values like `"km"` (kilometers).
- **Response**:
    - Status code `200 OK` with the total distance traveled by the courier in the requested unit.
    - Returns a `Double` representing the total distance.
- **Action**:
    - The method first converts the `distanceUnit` request parameter into a `DistanceUnit` enum using `DistanceUnit.fromString()`.
    - Then, a `CourierTotalDistanceUseCase` object is created with the courier ID and distance unit, and passed to the `courierTotalDistanceUseCaseHandler` for handling the logic.
    - The result (total distance) is returned in the response body.

## Build and Run the Application

1. **Build the Docker Image**
   ```bash
   docker build -t courier-tracker-api .
   ```

2. **Run the Docker Container**
   ```bash
   docker run -p 8081:8081 courier-tracker-api
   ```

3. **Access the Swagger UI**
    - Open your browser and navigate to:
        - `http://localhost:8081/swagger-ui/index.html`

## Usage Examples

### 1. **Get All Stores**
Retrieve a list of all stores:
```bash
curl -X 'GET' \
  'http://localhost:8081/courier-tracker-api/v1/store/all' \
  -H 'accept: */*'
```

### 2. **Log Courier Instant Location**
Log the current location of a courier:
```bash
curl -X 'POST' \
  'http://localhost:8081/courier-tracker-api/v1/courier/log-instant-location' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "courierId": "3f059f53-ffe3-494b-835f-43dc6890bd61",
  "lat": 40.9927797,
  "lng": 29.1248079,
  "time": "2025-01-26T21:59:22.615Z"
}'
```

### 3. **Get Courier's Total Distance**
Query the total distance traveled by a specific courier:
```bash
curl -X 'GET' \
  'http://localhost:8081/courier-tracker-api/v1/courier/3f059f53-ffe3-494b-835f-43dc6890bd61/totalDistance?distanceUnit=km' \
  -H 'accept: */*'
```

---
