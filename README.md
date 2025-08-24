# Fabrikka - Microservices E-commerce Platform

Fabrikka is a modern, cloud-native e-commerce platform built using microservices architecture with Spring Boot and containerized deployment using Docker. The platform provides comprehensive functionality for online retail operations including product management, user management, order processing, inventory control, and real-time notifications.

## 🏗️ Architecture Overview

The application follows a **microservices architecture pattern** with the following core components:

- **API Gateway** - Central entry point for all client requests
- **Discovery Server** (Eureka) - Service registration and discovery
- **Config Server** - Centralized configuration management
- **Multiple Business Services** - Domain-specific microservices
- **Monitoring & Observability** - Grafana, Prometheus, Loki stack
- **Message Queue** - RabbitMQ for asynchronous communication

## 🚀 Microservices

### Core Services
- **`api-gateway`** (Port: 8084) - Routes requests to appropriate microservices
- **`discovery-server`** (Port: 8761) - Eureka server for service discovery
- **`config-server`** (Port: 8888) - Centralized configuration management

### Business Services
- **`product-service`** (Port: 8085) - Product catalog management with CRUD operations, category management, search and filtering
- **`user-service`** (Port: 9080) - User authentication and profile management
- **`order-service`** (Port: 9001) - Order processing and management
- **`cart-service`** (Port: 9000/9002) - Shopping cart functionality
- **`inventory-service`** (Port: 8086) - Stock and inventory management
- **`notification-service`** (Port: 8082) - Email notifications and messaging
- **`load-product`** (Port: 9191) - Bulk product data loading service

## 🛠️ Technology Stack

### Backend
- **Java 17** - Programming language
- **Spring Boot 3.4.5** - Application framework
- **Spring Cloud 2024.0.1** - Microservices framework
- **Spring Data JPA** - Data persistence
- **H2 Database** - In-memory database for development
- **Maven** - Build automation

### Infrastructure & DevOps
- **Docker & Docker Compose** - Containerization
- **Jenkins** - CI/CD pipeline (Jenkinsfile included)
- **Nginx** - Load balancer and reverse proxy
- **RabbitMQ** - Message broker for asynchronous communication

### Monitoring & Observability
- **Prometheus** - Metrics collection
- **Grafana** - Monitoring dashboards
- **Loki** - Log aggregation
- **Micrometer** - Application metrics

### Build & Deployment
- **Jib Maven Plugin** - Containerless container builds
- **Spring Boot Actuator** - Production-ready features
- **OpenFeign** - Declarative REST client

## 📸 Screenshots

Below are some visual representations of the Fabrikka platform setup and monitoring:

### Jenkins Pipeline
![Jenkins Pipeline](./b0136687945a2b9bc91f901896ba8f6b7b5c4c0f.png)

![Jenkins Pipeline Status](./310276ccf8aa7c6f0873b0226677282037774665.png)

### Docker Desktop
![Docker Desktop](./d6b4fd01aced1b97bd3c8752efe783b5f8e3778a.png)

![Docker Services](./dfb3168110ce3b7708660f191f2812580f9abd9e.png)

### Environment Setup
![Environment Variables](./634437b5d6623a4ee1ccf8b9fc139e1084565efd.png)

### Service Discovery & Configuration
![Eureka Dashboard](./675eb483b6f6ef4bc9e628a4a5f67fa8942186eb.png)

![Config Server](./798311919dde7d5e20094929e9480c1e2bf04be0.png)

### Microservices Health Status
![Product Service](./50934ba5f49cde9e240f4fae42aa409f5b9e3f95.png)

![Order Service](./5d90ef64dd2751f47e52b23038f389efe6417897.png)

![Cart Service](./f23b8657151b368d5e0acfbf7efdfa1e95f001de.png)

![Inventory Service](./b73b9e3570c387410c81d6e7c13a34e385a86a19.png)

![Notification Service](./0eaca62df2aedbc602a6efae20cf940e4fa73ae3.png)

![User Service](./4b8fab6ccc20e216e462e380cd9f352cd8f9d010.png)

![API Gateway](./2e81babbdafd3e7b43e5df31e96b94dc217350ba.png)

![Load Product Service](./26f95bb33f097d22cb9726cc3da977def9babbb9.png)

![Discovery Server](./b3d801dc378ed57dd4300087ee2b7ece4aea0359.png)

### Grafana Loki Dashboard
![Loki Dashboard](./16ea322570c7ad022228aefccbbaf82785ee1c9b.png)

![Loki Logs Aggregation](./c36542485e7d3dae92926d47a365bddd021b1dea.png)

## 📋 Prerequisites

Before running this application, ensure you have the following installed:

- **Java 17** or higher
- **Maven 3.6+**
- **Docker** and **Docker Compose**
- **Git**
- **Docker Hub Account** (for pushing images)

## 🚀 Quick Start

### 1. Clone the Repository
```bash
git clone https://github.com/kumarrishav28/Fabrikka.git
cd Fabrikka
```

### 2. Environment Setup
Create a `.env` file in the `docker-compose` directory with the following variables:
```bash
DOCKER_USER=your-docker-username
SERVICE_VERSION=latest
MINIO_ROOT_PASSWORD=your-minio-password
RABBITMQ_USER=your-rabbitmq-user
RABBITMQ_PASS=your-rabbitmq-password
GMAIL_USER=your-gmail-username
GMAIL_APP_PASSWORD=your-gmail-app-password
```

### 3. Build All Services

#### Option A: Build JARs Only (Local Development)
```bash
# Build all microservices JARs
mvn clean install -f api-gateway/pom.xml
mvn clean install -f discovery-server/pom.xml
mvn clean install -f config-server/pom.xml
mvn clean install -f product-service/pom.xml
mvn clean install -f user-service/pom.xml
mvn clean install -f order-service/pom.xml
mvn clean install -f cart-service/pom.xml
mvn clean install -f inventory-service/pom.xml
mvn clean install -f notification-service/pom.xml
mvn clean install -f load-product/pom.xml
```

#### Option B: Build and Push Docker Images with Jib

Before using this option, ensure your `pom.xml` files have the Jib plugin configured with proper Docker Hub credentials. Example configuration:

```xml
<plugin>
    <groupId>com.google.cloud.tools</groupId>
    <artifactId>jib-maven-plugin</artifactId>
    <version>3.4.0</version>
    <configuration>
        <from>
            <image>eclipse-temurin:17-jdk</image>
        </from>
        <to>
            <image>${docker.username}/service-name:${project.version}</image>
            <auth>
                <username>${docker.username}</username>
                <password>${docker.password}</password>
            </auth>
        </to>
        <container>
            <ports>
                <port>8080</port>
            </ports>
            <environment>
                <SPRING_PROFILES_ACTIVE>${spring.profiles.active}</SPRING_PROFILES_ACTIVE>
            </environment>
        </container>
    </configuration>
</plugin>
```

Build and push Docker images to Docker Hub:
```bash
# Set Docker Hub credentials (or use ~/.m2/settings.xml)
export DOCKER_USERNAME=your-dockerhub-username
export DOCKER_PASSWORD=your-dockerhub-password

# Build and push each service
mvn compile jib:build -f api-gateway/pom.xml -Ddocker.username=$DOCKER_USERNAME -Ddocker.password=$DOCKER_PASSWORD
mvn compile jib:build -f discovery-server/pom.xml -Ddocker.username=$DOCKER_USERNAME -Ddocker.password=$DOCKER_PASSWORD
mvn compile jib:build -f config-server/pom.xml -Ddocker.username=$DOCKER_USERNAME -Ddocker.password=$DOCKER_PASSWORD
mvn compile jib:build -f product-service/pom.xml -Ddocker.username=$DOCKER_USERNAME -Ddocker.password=$DOCKER_PASSWORD
mvn compile jib:build -f user-service/pom.xml -Ddocker.username=$DOCKER_USERNAME -Ddocker.password=$DOCKER_PASSWORD
mvn compile jib:build -f order-service/pom.xml -Ddocker.username=$DOCKER_USERNAME -Ddocker.password=$DOCKER_PASSWORD
mvn compile jib:build -f cart-service/pom.xml -Ddocker.username=$DOCKER_USERNAME -Ddocker.password=$DOCKER_PASSWORD
mvn compile jib:build -f inventory-service/pom.xml -Ddocker.username=$DOCKER_USERNAME -Ddocker.password=$DOCKER_PASSWORD
mvn compile jib:build -f notification-service/pom.xml -Ddocker.username=$DOCKER_USERNAME -Ddocker.password=$DOCKER_PASSWORD
mvn compile jib:build -f load-product/pom.xml -Ddocker.username=$DOCKER_USERNAME -Ddocker.password=$DOCKER_PASSWORD
```

### 4. Start the Application Stack
```bash
cd docker-compose
docker-compose up -d
```

### 5. Verify Services
Check that all services are running:
```bash
docker-compose ps
```

## 🌐 Service Endpoints

| Service | URL | Description |
|---------|-----|-------------|
| API Gateway | http://localhost:8084 | Main entry point |
| Eureka Dashboard | http://localhost:8761 | Service discovery |
| Grafana | http://localhost:3000 | Monitoring dashboards |
| Prometheus | http://localhost:9090 | Metrics collection |
| RabbitMQ Management | http://localhost:15672 | Message queue management |

## 📦 API Documentation

### Product Service Endpoints
- `GET /products/all` - Get all products
- `GET /products/{id}` - Get product by ID
- `POST /products/add` - Add new product
- `PUT /products/update/{id}` - Update product
- `DELETE /products/delete/{id}` - Delete product
- `GET /products/search` - Search products with pagination and filters
- `GET /products/category` - Get all categories
- `POST /products/addAll` - Bulk add products

### Search Parameters
The product search endpoint supports the following query parameters:
- `page` - Page number (required)
- `size` - Page size (required)
- `categories` - Filter by categories (optional)
- `minPrice` - Minimum price filter (optional)
- `maxPrice` - Maximum price filter (optional)
- `sort` - Sort criteria (optional)

## 🔧 Development

### Running Individual Services
Each service can be run independently for development:

```bash
cd product-service
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### Available Profiles
- **local** - For local development
- **dev** - For development environment
- **prod** - For production environment

### Adding New Products
Sample curl command to add a product:
```bash
curl -X POST http://localhost:8084/products/add \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sample Product",
    "description": "Product description",
    "price": 99.99,
    "imageUrl": "https://example.com/image.jpg",
    "category": {
      "name": "Electronics"
    }
  }'
```

## 📊 Monitoring

The application includes comprehensive monitoring with:

- **Health Checks** - All services expose `/actuator/health` endpoints
- **Metrics** - Prometheus metrics at `/actuator/prometheus`
- **Logs** - Structured logging with Loki aggregation
- **Dashboards** - Pre-configured Grafana dashboards

Access Grafana at http://localhost:3000 (admin/admin) to view system metrics and logs.

## 🐳 Docker Support

Each microservice includes:
- Multi-stage Dockerfile for optimized builds
- Jib Maven plugin for containerless builds
- Docker Compose configuration for local development
- Health check configurations
- Environment-specific configurations

## 🔐 Security Considerations

- Services communicate through the API Gateway
- Environment variables for sensitive configuration
- Spring Security integration ready
- Network isolation through Docker networks

## 📈 Scalability Features

- Horizontal scaling support
- Load balancing with Nginx
- Service discovery with Eureka
- Asynchronous processing with RabbitMQ
- Database per service pattern

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is open source and available under the [MIT License](LICENSE).

## 📞 Contact

**Kumar Rishav** - [@kumarrishav28](https://github.com/kumarrishav28)

Project Link: [https://github.com/kumarrishav28/Fabrikka](https://github.com/kumarrishav28/Fabrikka)

---

⭐ **If you find this project helpful, please consider giving it a star!** ⭐
