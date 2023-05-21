# Bookstore
* [General info](#general-info)
* [Features](#features)
* [Technologies](#technologies)
* [Setup](#setup)

## General info
<p>The project is a simple web application where users can perform CRUD operations based on books. Depending on the assigned role (CUSTOMER or EMPLOYEE), the privileges and scope of possibilities differ. The client and server communicate using the HTTP protocol.</p>
<p>The main goal of this project is to strengthen my knowledge of Spring and other java-related technologies, JavaScript/TypeScript with a focus on React, and the HTTP communication protocol in the context of REST.</p>

## Features
<ul>
  <li><b>User Registration and Login:</b> Users can register an account and log in to the application.</li>
  <li><b>Book Operations:</b> Users can perform various operations on books, including borrowing and returning them.</li>
  <li><b>Employee Privileges:</b> Employees with elevated privileges can add or remove books from the system.</li>
  <li><b>Borrowed Books Overview:</b> Users can view the list of books they have borrowed.</li>
  <li><b>User Profile Management:</b> Users can view and modify their profile information, including the password.</li>
</ul>

![bookstore-view](https://github.com/gabriela-plis/bookstore/assets/102433197/6ee5a3db-d926-4e6d-9b42-125bdddb68b3)

## Technologies

### Backend
<ul>
  <li>Spring Boot</li>
  <li>Spring Web</li>
  <li>Spring Data</li>
  <li>Spring Security</li>
  <li>Lombok</li>
  <li>Mapstruct</li>
</ul>

### Backend Testing
<ul>
  <li>Spock</li>
  <li>Testcontainers</li>
  <li>Liquibase</li>
</ul>

### Databse
<ul>
  <li>PostgreSQL</li>
</ul>

### Frontend
<ul>
  <li>Typescript</li>
  <li>React</li>
</ul>

### Other
<ul>
  <li>Docker</li>
</ul>

## Setup
To run Bookstore, follow steps:
1. Clone project

  ``` bash      
  $ git clone https://github.com/gabriela-plis/bookstore.git
  ```
2.  Run docker-compose (be sure that you are on bookstore folder)

  ``` bash
  docker-compose up -d
  ```

