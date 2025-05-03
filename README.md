# Movie Recommendation Service

This is a Spring Boot-based application for managing and retrieving movie recommendations. It
provides a RESTful API to perform various operations such as fetching movies by genre, rating, or
title.

## Features

- Retrieve all movies.
- Fetch movies by title, genre, or rating range.
- Filter movies by genre and rating range.
- API key-based authentication for secure access.

## Technologies Used

- **Java**: Programming language.
- **Spring Boot**: Framework for building the application.
- **Maven**: Dependency management and build tool.
- **JUnit 5**: Testing framework.
- **MockMvc**: For testing the REST API.

## Prerequisites

- Java 17 or higher
- Maven 3.8 or higher
- IDE (e.g., IntelliJ IDEA)
- Database (e.g., H2, MySQL, or PostgreSQL)

## Setup Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/vinay-vishwkarma_atsolera/movie-recommendation-service.git
2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

4. Access the API at `http://localhost:8080/mra`.

## API Endpoints

### Movies

- **GET /v1/movies**  
  Retrieve all movies.  
  **Headers**: `X-API-KEY` (required)

- **GET /v1/movies/{title}**  
  Retrieve a movie by title.  
  **Headers**: `X-API-KEY` (required)

- **GET /v1/movies/genre/{genre}**  
  Retrieve movies by genre.  
  **Headers**: `X-API-KEY` (required)

- **GET /v1/movies/rating/{minRating}/{maxRating}**  
  Retrieve movies within a rating range.  
  **Headers**: `X-API-KEY` (required)

- **GET /v1/movies/byGenreAndRating**  
  Retrieve movies by genre and rating range.  
  **Parameters**: `genre`, `minRating`, `maxRating`  
  **Headers**: `X-API-KEY` (required)
