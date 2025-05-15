# podzilla-utils-lib

## Consuming the `podzilla-utils-lib` Package

To use the shared `podzilla-utils-lib` in your microservice project, add it as a dependency in your project's `pom.xml`.

This library is hosted on JitPack, so you also need to add the JitPack repository URL to your `pom.xml`.

---

### 1. **Add the Repository:**

Add the following `<repositories>` section to your microservice's `pom.xml` (under the main `<project>` element or inside an existing `<repositories>` section):

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```
---

### 2. **Add the Dependency:**

Inside your `<dependencies>` section, add:

```xml
<dependency>
  <groupId>com.github.Podzilla</groupId>
  <artifactId>podzilla-utils-lib</artifactId>
  <version>v1.1.6</version>
</dependency>
```
---

### 3. **Build Your Project:**

Once added, build your project using:

```bash
mvn clean install -U
```

> 🧠 The `-U` flag forces Maven to **update all snapshots/releases**, ensuring it downloads the latest version from JitPack.

---

### 4. **RabbitMQ Configuration**

Configure your application to connect to RabbitMQ by adding these properties to your `src/main/resources/application.properties`:

```properties
# RabbitMQ Configuration
spring.rabbitmq.host=rabbitmq
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```

**Note on RabbitMQ Host:**
* `spring.rabbitmq.host=rabbitmq` assumes your app and RabbitMQ are on the **same Docker network**.
* If RabbitMQ is on your Docker host (e.g., with Docker Desktop), use `spring.rabbitmq.host=host.docker.internal`.
* Otherwise, use the specific IP address or resolvable hostname.

---

### 5. **Spring Component Scanning**

To ensure Spring discovers the library components, include `com.podzilla` in `@ComponentScan`. If your service uses a different base package, include that as well:

```java

@SpringBootApplication
@ComponentScan(basePackages = { "com.podzilla", "com.yourcompany.myservice" }) // Add your service package if different
public class MyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

Replace `"com.yourcompany.myservice"` with your actual service package if it's different from `com.podzilla`.

---

### 6. **Publishing Events**

Publish events using the `EventPublisher` component from the library.

**Injecting the Publisher:**

```java
@Service
@RequiredArgsConstructor
public class MyService {
    private final EventPublisher eventPublisher;

    // ... your service methods
}
```

**Event Metadata & Payload:**
Use `EventMetadata` constants from `EventsConstants` (e.g., `COURIER_REGISTERED`). Event payloads are classes extending `BaseEvent` (e.g., `CourierRegisteredEvent`).

#### Example Pairs:

* `EventsConstants.COURIER_REGISTERED` → `CourierRegisteredEvent`
* `EventsConstants.ORDER_PLACED` → `OrderPlacedEvent`
* `EventsConstants.INVENTORY_UPDATED` → `InventoryUpdatedEvent`
* `EventsConstants.ORDER_DELIVERED` → `OrderDeliveredEvent`

**Example of Publishing:**

```java
@Service
@RequiredArgsConstructor
public class MyService {
    private final EventPublisher eventPublisher;

    public void registerCourier(CourierRegisteredEvent payload) {
        eventPublisher.publishEvent(EventsConstants.COURIER_REGISTERED, payload);
    }
}
```

---

### 7. **Consuming Events**

Each service can define up to **three queues**—one for each exchange: `user`, `order`, and `inventory`. These queues receive different event types, but **all events extend `BaseEvent`** from the shared library.

When consuming, use the `@RabbitListener` annotation. You can inspect the specific event type using `instanceof` or reflection.

**Queue Constants**
Queue names are defined in `EventsConstants` using the format:
`<SERVICE>_<EXCHANGE>_EVENT_QUEUE`

Examples:

* `ANALYTICS_USER_EVENT_QUEUE`
* `ORDER_ORDER_EVENT_QUEUE`
* `WAREHOUSE_INVENTORY_EVENT_QUEUE`
* `COURIER_USER_EVENT_QUEUE`

**Example Listener:**

```java
@Component
public class AnalyticsUserEventsConsumer {

    @RabbitListener(queues = EventsConstants.ANALYTICS_USER_EVENT_QUEUE)
    public void handleEvent(BaseEvent event) {
        if (event instanceof CourierRegisteredEvent) {
            CourierRegisteredEvent courierEvent = (CourierRegisteredEvent) event;
            System.out.println("Received Courier Registered Event: " + courierEvent);
            // Add your business logic here
        }
    }
}
```

---

### 🔧 8. **QueueResolver (For Development Use Only)**

The `QueueResolver` utility helps during **local development** by determining which queue a service should listen to for a given event.

> ⚠️ **Note:** This utility is **not used at runtime in production**. It is designed for test scaffolding, dev tooling, or simulating behavior in local environments.

**Usage:**

```java
String queue = QueueResolver.getQueueForServiceEvent(
    EventsConstants.SERVICE_ANALYTICS,
    EventsConstants.ORDER_PLACED
);
System.out.println("Queue to listen on: " + queue);
// Output: "ANALYTICS_ORDER_EVENT_QUEUE"
```

**Signature:**

```java
public static String getQueueForServiceEvent(String serviceName, EventsConstants.EventMetadata eventMetadata)
```

* `serviceName`: e.g., `EventsConstants.SERVICE_ANALYTICS`
* `eventMetadata`: e.g., `EventsConstants.ORDER_PLACED`
* Returns the appropriate queue name string, or `null` if no match is found.

---

## Release Notes for `podzilla-utils-lib`

* The stable code lives in the `main` branch.
* When ready to release a new version:

  1. Update the version in `pom.xml` (remove `-SNAPSHOT`, set new version).
  2. Commit the version change.
  3. Create a Git tag with the version number, e.g.:

     ```bash
     git tag -a vX.Y.Z -m "Release version X.Y.Z"
     ```
  4. Push the changes and the tags:

     ```bash
     git push origin main
     git push origin --tags
     ```
* **Note:** Pushing the tag automatically triggers the build and deployment on JitPack.
* Check the build status on GitHub Actions and your release on [JitPack](https://jitpack.io/#Podzilla/podzilla-utils-lib).
