# Includes:
- Service Discovery.
- Centralized Configuration.
- Distributed Tracing.
- Event Driven Architecture.
- Centralized Logging.
- Circuit Breaker.
- Securing with Keycloak.

### Spring Cloud
Is a project under the Spring Projects Ecosystem that concentrates on build microservices and connect each one of them.

### Services
- Product Service: Create and view Products, acts as a Product Catalog.
- Order Service: Can Order Products.
- Inventory Service: Can check if product is in stock or not.
- Notification Service: Can send notifications, after order is placed.

>[!NOTE]
>**Order**, **Inventory** and **Notification Service** will interact with each other.

### Model Solution Architecture
1. Product Service will connect to MongoDB
2. Order Service will connect to MySQL
3. Inventory Service will connect to PostgreSQL
4. User Service will connect to MySQL
5. Notification Service won't connect any database, but have the responsibility to send out notifications to the users via RabbitMQ.

The Order Service communicates with Inventory one via Synchronous Communication and when an Order is placed it will communicates with Notification one via Asynchronous Calling using a Message Queues.

There is an API Gateway who send the request from the user to respective service.
