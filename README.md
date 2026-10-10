# Real-Time Order Notification System
### Java · Spring Boot · Apache Kafka · MySQL · Docker
### ⚠️ Zero Lombok — works out of the box in STS / IntelliJ / VS Code

---

## Architecture flow

```
Client (Postman)
      │  REST POST /api/v1/orders
      ▼
┌─────────────────────┐     publishes      ┌──────────────────────┐
│   Order Service     │ ─────────────────▶ │   Kafka Topic        │
│   (Port 8081)       │                    │   "order-events"     │
│                     │                    │   (3 partitions)     │
│  REST API           │                    └──────────┬───────────┘
│  Kafka Producer     │                               │ consumes
│  MySQL (orderdb)    │                    ┌──────────▼───────────┐
└─────────────────────┘                    │ Notification Service │
                                           │ (Port 8082)          │
                                           │                      │
                                           │ Kafka Consumer       │
                                           │ Idempotency Check    │
                                           │ MySQL (notificationdb│
                                           └──────────────────────┘
```

---

## Tech Stack
| Technology      | Usage                              |
|-----------------|------------------------------------|
| Java 17         | Core language (no Lombok)          |
| Spring Boot 3.2 | Application framework              |
| Spring MVC      | REST API                           |
| Apache Kafka    | Event streaming                    |
| MySQL 8.0       | Persistent storage                 |
| Docker Compose  | Local infra setup                  |
| JUnit + Mockito | Unit testing                       |

---

## How to Test

### Step 1 — Start Infrastructure
```bash
docker-compose up -d
```
Starts: Kafka + Zookeeper + MySQL + Kafka UI

### Step 2 — Run Order Service
```bash
cd order-service
mvn spring-boot:run
```
→ http://localhost:8081

### Step 3 — Run Notification Service
```bash
cd notification-service
mvn spring-boot:run
```
→ http://localhost:8082

### Step 4 — Test APIs
Import `OrderNotificationSystem.postman_collection.json` into Postman.

### Step 5 — Watch Kafka Messages
Open Kafka UI → http://localhost:8080 → Topics → order-events → Messages

---

## REST Endpoints

| Method | Endpoint                          | Description         |
|--------|-----------------------------------|---------------------|
| POST   | `/api/v1/orders`                  | Place a new order   |
| GET    | `/api/v1/orders`                  | Get all orders      |
| GET    | `/api/v1/orders/{orderId}`        | Get order by ID     |
| GET    | `/api/v1/orders?email={email}`    | Get orders by email |
| PATCH  | `/api/v1/orders/{orderId}/status` | Update order status |

### Sample Request
```json
POST http://localhost:8081/api/v1/orders
{
  "customerName": "Rohan Tibile",
  "customerEmail": "rohan@test.com",
  "productName": "Laptop",
  "quantity": 1,
  "price": 75000.00
}
```

---

## Key Design Decisions

**1. Kafka message key = orderId**
All events for the same order go to the same partition → ordering guaranteed per order.

**2. Manual offset acknowledgment**
Offset committed ONLY after successful processing. If consumer crashes, Kafka redelivers.

**3. Idempotency pattern**
Unique key = `orderId_status`. Duplicates detected and skipped safely.

**4. Constructor injection (no @Autowired)**
All dependencies injected via constructor — testable and explicit.

---

## Interview Talking Points

**"Why Kafka over REST between services?"**
Kafka decouples the services. Order service doesn't wait for notification to complete.
If notification service is down, messages queue in Kafka and process when it recovers.

**"How do you handle duplicates?"**
Kafka delivers at-least-once. I store an idempotency key (orderId + status) in DB.
Before processing, I check if this key exists — if yes, I skip it safely.

**"What if the consumer crashes mid-processing?"**
Manual ack (AckMode.MANUAL_IMMEDIATE) means offset is only committed after success.
Kafka redelivers to another consumer in the group — no data loss.

**"How would you scale this?"**
Increase partitions to match consumer instances. Deploy on Kubernetes with HPA.
3 partitions = 3 parallel consumer threads processing simultaneously.

---

## Author
**Rohan Tibile** | Java Developer | Spring Boot · Kafka · Microservices
LinkedIn: https://linkedin.com/in/rohantibile

---