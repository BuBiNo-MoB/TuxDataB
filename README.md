TuxDataB
TuxDataB is a comprehensive web application designed to manage Linux distributions, user comments, and related data. It leverages Spring Boot for backend development and integrates with PostgreSQL for data persistence. The application supports functionalities such as user authentication, Linux distribution management, and user comments on distributions.

Features
User authentication and authorization
CRUD operations for Linux distributions
User comments on Linux distributions
Integration with PostgreSQL
RESTful API endpoints


Installation
Prerequisites
Java 17 or higher
Maven 3.6.0 or higher
PostgreSQL


Clone the Repository
git clone https://github.com/yourusername/TuxDataB.git

Configure PostgreSQL
Create a database named tuxdatabase and configure your application.properties file with your PostgreSQL credentials.

spring.datasource.url=jdbc:postgresql://localhost:5432/tuxdatabase
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password
spring.jpa.hibernate.ddl-auto=update

Populating Initial Data
The application comes with a runner to populate initial data for Linux distributions. You can modify the data in LinuxDistributionRunner if needed.


Contributing
Contributions are welcome! Please fork the repository and submit a pull request for any enhancements or bug fixes.

Contact
For any inquiries or questions, please contact emanuel.incarnato@gmail.com
