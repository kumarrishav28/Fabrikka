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

## 📸 Screenshots

Below are some visual representations of the Fabrikka platform setup and monitoring:

### Jenkins Pipeline
<img width="1918" height="996" alt="Image" src="https://github.com/user-attachments/assets/2750cfdc-efe6-46b4-ac6f-e795027e59fd" />

### Docker Desktop
<img width="1918" height="996" alt="Image" src="https://github.com/user-attachments/assets/2750cfdc-efe6-46b4-ac6f-e795027e59fd" />

<img width="1905" height="852" alt="Image" src="https://github.com/user-attachments/assets/58458384-5f3b-45ed-89a8-ccc19d36f414" />

### Environment Setup
<img width="497" height="353" alt="Image" src="https://github.com/user-attachments/assets/7612da75-57e3-4ac2-b7a0-ca58e43a17d2" />

### Service Discovery & Configuration
<img width="1905" height="1010" alt="Image" src="https://github.com/user-attachments/assets/5d4c4ab3-968c-4db4-a420-3b24b1b2e33d" />

### Fabrikka
<img width="1900" height="1022" alt="Image" src="https://github.com/user-attachments/assets/5e7a2786-e6f1-4181-b5d9-a1c931a2bebd" />

<img width="1912" height="1017" alt="Image" src="https://github.com/user-attachments/assets/50ee8405-824a-40bc-8ddb-51cd94832771" />

<img width="1913" height="1003" alt="Image" src="https://github.com/user-attachments/assets/1d038a0c-f4c5-4766-9735-f4e80d5581d8" />

<img width="1890" height="1007" alt="Image" src="https://github.com/user-attachments/assets/138dc278-cb31-4c1b-8d8f-d2bbd8ab23ef" />

<img width="1915" height="1007" alt="Image" src="https://github.com/user-attachments/assets/857547f7-2ea6-4b9e-9a2a-bd986acfdb5e" />

<img width="1912" height="1010" alt="Image" src="https://github.com/user-attachments/assets/ae09e702-d825-4963-9c5a-87b5fd1682ee" />

<img width="1892" height="1002" alt="Image" src="https://github.com/user-attachments/assets/e0199710-c7d3-4771-bcd1-d51b2188849f" />

<img width="1913" height="1012" alt="Image" src="https://github.com/user-attachments/assets/d8ea8722-8365-4ee4-b59b-1435d16d3a90" />

<img width="1903" height="1012" alt="Image" src="https://github.com/user-attachments/assets/12850fa0-72cb-4a7b-acb9-1a823b1f7921" />

<img width="1917" height="1013" alt="Image" src="https://github.com/user-attachments/assets/b0b4a564-f986-463f-8160-357d80828cb9" />

<img width="1296" height="707" alt="Image" src="https://github.com/user-attachments/assets/657e915b-c2db-4558-9b6b-7b5c2de93e9c" />

### Grafana Loki Dashboard
<img width="1917" height="1020" alt="Image" src="https://github.com/user-attachments/assets/44a6e171-10bf-48a8-8c8a-7843f134f2b6" />

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
