
# Phishing Protector

## Description
Phishing Protector is a Java-based monolith application designed for managing anti-phishing subscriptions via SMS processing. It also manages the existence of newly appearing text messages (live). The safety of links contained in text messages is checked based on an external endpoint examining their content. The application checks each link contained in a given SMS message and removes it if it contains phishing, provided that the recipient has previously subscribed the anti-phishing service by sending an SMS with the content START to the number 802. The customer can disable such subscription by sending an SMS with the content STOP to the number 802, then his text messages will be deleted. After all text messages have been processed, the application listens for new messages, checking the table every minute for new text messages. After downtime, the application can start working from the last fully processed SMS, which gives us the opportunity not to process SMS messages from the beginning. It utilizes Spring Boot for backend services, integrating with databases for storing SMS data, subscription details and HTTP 2 communication with external endpoint. 

## Prerequisites
- JDK 11 or higher
- Maven for dependency management and build automation
- PostgeSQL database (configured in `application.yml` and `dbs-config.yml`)

## Configuration
Configure the application settings in `application.yml` and database connections in `dbs-config.yml`.

## Running the Application
1. Clone the repository.
2. Navigate to the project directory.
3. Create tables using script placed in src/main/resources/sql/tables.sql in your safe schema.
4. Create and fill `src/main/resources/urls.yml` file. You can find template for url configuration in `src/main/resources/yml_templates/urls.yml`
5. Create and fill `src/main/resources/dbs-config.yml file`. You can find template for url configuration in `src/main/resources/yml_templates/dbs-config.yml`
6. Run `mvn clean install` to build the project.
7. Execute `mvn spring-boot:run` to start the application.

## Testing the Application
1. Create some mock external service. Using e.g. https://beeceptor.com/ or https://mocko.dev .
2. Run app as indicated above.
3. Insert rows into sms table. You can use sample inserts from `src/main/resources/sql/sample_sms_inserts_for_test_env.sql` file.

## Key Components
- SMS Processing: Handles incoming SMS data and manages subscriptions.
- Anti-Phishing Subscription: Manages activation and deactivation of protection subscriptions.
- Database Integration: Uses JPA for data persistence.

## Logging
Configured using `log4j2.xml`. Customize as per your requirement for debugging and monitoring.