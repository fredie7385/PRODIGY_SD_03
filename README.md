# JavaFX Contact Management Application

## Overview

This JavaFX application provides a user management system that allows users to register, view, update, and delete user
information. It connects to a MySQL database to store user data and implements basic CRUD (Create, Read, Update, Delete)
operations. The application features a user-friendly interface built with JavaFX and FXML.

## Features

* User registration with input validation
* Display a list of registered users
* Update user information
* Delete users with confirmation
* Search and filter users by name or email
* Error handling and user feedback

## Prerequisites

Before running the application, ensure you have the following installed:

* **Java Development Kit (JDK)**: Version 8 or higher
* **Apache Maven** (optional): For managing dependencies
* **MySQL Server**: Version 5.7 or higher
* **JavaFX SDK**: Ensure the JavaFX SDK is configured in your IDE or build tool.

## Installation

1. **Clone the Repository:**
   ```bash
   https://github.com/fredie7385/PRODIGY_SD_03.git
   cd contact-management-app    


2. Set Up MySQL Database:

    * Create a new database (e.g., contact_management).
    * Run the following SQL script to create the necessary table:

`  CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  firstname VARCHAR(50) NOT NULL,
  lastname VARCHAR(50) NOT NULL,
  phoneNo VARCHAR(15),
  email VARCHAR(100) NOT NULL UNIQUE
  );`

3. Configure Database Connection:

    * Update the database connection details in the DbConnection class:

      `public static final String DB_URL = "jdbc:mysql://localhost:3306/user_management";
      public static final String USER = "your_db_username";
      public static final String PASS = "your_db_password";`
4. Build the Project:

    * If using Maven, run:
      `mvn clean install`

## Contributing

Contributions are welcome! If you have suggestions for improvements or features, please create an issue or submit a pull
request.

Fork the repository.
Create your feature branch:
`git checkout -b feature/YourFeature`
