# MyKurir - Shipping Management System

A modern shipping management system with integrated tracking capabilities.

## Features
- Package tracking system
- Shipment management
- User authentication and authorization
- RESTful API integration

## Technology Stack

### Frontend
- Angular 15
- Porto Admin Template
- Node.js & npm

### Backend
- Spring Boot 3
- Java JDK 21
- MySQL Database

## Installation Guide

### Frontend Setup
1. Install dependencies:
    ```bash
      npm install
    ```
2. Start development server:
    ```bash
      ng serve
    ```
The application will be available at `http://localhost:4200`

### Backend Setup
1. Create an empty MySQL database named `mykurir`

2. Configure database credentials in `src/main/resources/application.properties`:
    ```bash
      spring.datasource.username=your_username
      spring.datasource.password=your_password
    ```
3. Start the Spring Boot application:
    ```bash
      bash mvn spring-boot:run
    ```
The server will start at `http://localhost:8082`

### Initial Admin Setup
1. Create an admin account using the registration API `http://localhost:8082/v1/api/auth/register` method POST:
    ```bash
      {
        "fullName": "Admin Name",
        "email": "admin@example.com",
        "password": "your_password"
      }
    ```
2. Update the user role to ADMIN in the database
<img src="https://github.com/alexistdev/mykurir/blob/main/IMAGES/gambar4.png?raw=true">

### API Authentication
- The API uses Basic Authentication
- Configure your API client (Insomnia/Postman) with the registered email and password
- Include the authentication header in all API requests

<img src="https://github.com/alexistdev/mykurir/blob/main/IMAGES/gambar5.png?raw=true">

## API Documentation
The following endpoints are available:
- Authentication APIs
- Shipping Management APIs
- Tracking APIs
- User Management APIs

For detailed API documentation, please refer to the API specification in your Insomnia/Postman client.

## License
[License Type] - See LICENSE.md for details

## Contact
For support or queries, please open an issue in this repository.
    
<img src="https://github.com/alexistdev/mykurir/blob/main/IMAGES/gambar1.png?raw=true" width="1200px">

<img src="https://github.com/alexistdev/mykurir/blob/main/IMAGES/gambar3.png?raw=true" />

