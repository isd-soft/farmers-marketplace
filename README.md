# Farmers Marketplace
<img src="https://github.com/user-attachments/assets/387defaf-eb11-4c2a-a275-c047322d408c" width="150" />

## Intro
**Farmers Marketplace** is a web-based application designed to connect local farmers and consumers, allowing them to interact efficiently and directly. The platform facilitates the buying and selling of fresh farm produce, while ensuring reliable order management and smooth communication.

---

## Development
The backend is developed using the **Spring Boot** framework, which provides a powerful, scalable, and easy-to-maintain platform. The frontend is built with **PrimeVue** for UI components and **Node.js** for backend functionality. **Docker** is used to containerize the application for easy deployment.

- **Backend**: Spring Boot
- **Frontend**: Node.js with PrimeVue
- **Database**: PostgreSQL

---

## Features
The application offers the following key features:

- **User Roles**: Three main roles are supported:
  - **Admin**: Can manage users, products, and orders across the system.
  - **Farmer**: Can add their products, manage orders, and track sales.
  - **Customer**: Can browse and search products, place orders, and view order history.

- **Category Management**: Admins can create, update and remove categories.

- **Product Management**: Farmers can create, update and remove products. List their products with details such as price, quantity, and description.  

- **Order Management**: Customers can place orders for products, and Farmers can accept or reject them. Farmers and Admins can update order statuses.

- **Notifications**: Automated email notifications inform users about order updates.

---

## Scope and Mission
The goal of **Farmers Marketplace** is to improve the connection between farmers and consumers, simplifying the process of buying and selling fresh produce. The platform aims to automate and streamline the management of farmers' markets by solving the following problems:

- **Product Management**: Easy addition, updating, and removal of products by farmers.
- **Order Tracking**: Real-time tracking of orders and product availability.
- **Admin Dashboard**: A comprehensive admin panel to monitor and manage users, orders, and products.
- **Notifications**: Keep users informed through email alerts about order statuses.

---

## Technologies Used

- [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F.svg?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot) For building the backend REST API.
- [![PrimeVue](https://img.shields.io/badge/PrimeVue-42b983.svg?style=for-the-badge&logo=vue.js&logoColor=white)](https://primevue.org/) For building responsive frontend UI.
- [![Node.js](https://img.shields.io/badge/Node.js-339933.svg?style=for-the-badge&logo=node.js&logoColor=white)](https://nodejs.org/) For backend functionalities supporting the frontend.
- [![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791.svg?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/) For relational data storage (user, category, product, and order data).
- [![Docker](https://img.shields.io/badge/Docker-2496ED.svg?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/) For containerizing the app.
- [![Maven](https://img.shields.io/badge/Maven-C71A36.svg?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org/) For project build and dependency management.
- [![Flyway](https://img.shields.io/badge/Flyway-4B7BEC.svg?style=for-the-badge&logo=flyway&logoColor=white)](https://flywaydb.org/) For database versioning and migrations.
- [![Swagger](https://img.shields.io/badge/Swagger-4CAF50.svg?style=for-the-badge&logo=swagger&logoColor=white)](https://swagger.io/) For API documentation and interactive testing.

---

## Setup Instructions

### Prerequisites
1. Install **Java 21** or higher.
2. Install **Node.js**.
3. Install **PostgreSQL** 
   Configure the Database
   - Ensure you have PostgreSQL running.
   - Create a database named farmer_market_database.
   - Update application.properties with your database credentials.
4. Install **Maven** to manage dependencies and build the backend.
5. Clone this repository:
```bash
 git clone git@github.com:isd-soft/farmers-marketplace.git
 cd farmers-marketplace
 ```

### Backend Setup (Folder: isd-farmers-market)
1. Navigate to the Backend Directory
```bash
 cd isd-farmers-market
 ```
2. Configure the application.properties file for your local database setup (if needed).
3. Build the project using Maven
```bash
 mvn clean install
 ```
4. Start the Spring Boot application
```bash
 mvn spring-boot:run
 ```

 ### Frontend Setup (Folder: farmers-market-frontend)  
 1. Navigate to the Frontend Directory
 ```bash
 cd farmers-market-frontend
  ```
 2. Install Dependencies
```bash
 npm install
  ```
 3. Run the Frontend
```bash
 npm run dev
  ```

 ### 
 For more detailed documentation and usage instructions, visit the [Wiki Page](https://github.com/isd-soft/farmers-marketplace/wiki).
