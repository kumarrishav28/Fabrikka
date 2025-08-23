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
```bash
# Build all microservices
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

**Kumar Rishav** - [kumarrishav28@gmail.com)

Project Link: [https://github.com/kumarrishav28/Fabrikka](https://github.com/kumarrishav28/Fabrikka)

---

⭐ **If you find this project helpful, please consider giving it a star!** ⭐
