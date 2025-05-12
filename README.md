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
    <url>[https://jitpack.io](https://jitpack.io)</url>
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
  <version>v1.1.5</version>
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

To ensure library components are discovered, your main Spring Boot application class (with `@SpringBootApplication`) might need `@ComponentScan`.

If your service's base package is different from the library's `com.podzilla` (e.g., `com.Podzilla` or `com.yourcompany.myservice`), include both:

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.podzilla", "com.Podzilla" /* Add your service's base package here if different */ })
public class AnalyticsApplication { // Replace with your actual application class name

    public static void main(String[] args) {
        SpringApplication.run(AnalyticsApplication.class, args);
    }
}
```

---

### 6. **Publishing Events**

Publish events using the `EventPublisher` component from the library.

**Injecting the Publisher:**

```java
import com.Podzilla.mq.EventPublisher; // Adjust package if needed
import com.Podzilla.mq.EventsConstants; // Adjust package if needed
import com.Podzilla.mq.payloads.CourierRegisteredEvent; // Adjust package if needed

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyService {
    private final EventPublisher eventPublisher;

    // ... your service methods
}
```

**Event Metadata & Payload:**
Use `EventMetadata` constants from `EventsConstants` (e.g., `COURIER_REGISTERED`). Event payloads are classes implementing `EventPayload` (e.g., `CourierRegisteredEvent`).

Available `EventMetadata` constants:
* `COURIER_REGISTERED`
* `CUSTOMER_REGISTERED`
* `PRODUCT_CREATED`
* `INVENTORY_UPDATED`
* `CART_CHECKED_OUT`
* `ORDER_PLACED`
* `ORDER_CANCELLED`
* `ORDER_PACKAGED`
* `ORDER_ASSIGNED_TO_COURIER`
* `ORDER_SHIPPED`
* `ORDER_DELIVERED`
* `ORDER_FAILED`

**Example of Publishing:**

```java
import com.Podzilla.mq.EventPublisher;
import com.Podzilla.mq.EventsConstants;
import com.Podzilla.mq.payloads.CourierRegisteredEvent;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

In consuming services, after previous configurations, use the `@RabbitListener` annotation.

**Queue Constants:**
Queue constants are available in `EventsConstants`. They follow the format `SERVICE_EXCHANGE_QUEUE`.

Available queue constants:
* `ANALYTICS_USER_EVENT_QUEUE`
* `ANALYTICS_ORDER_EVENT_QUEUE`
* `ANALYTICS_INVENTORY_EVENT_QUEUE`
* `ORDER_ORDER_EVENT_QUEUE`
* `WAREHOUSE_ORDER_EVENT_QUEUE`
* `COURIER_ORDER_EVENT_QUEUE`

**Example of Consuming:**

```java
import com.Podzilla.mq.EventsConstants;
import com.Podzilla.mq.events.CourierRegisteredEvent; // Adjust package if needed

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsUserEventsConsumer {

    @RabbitListener(queues = EventsConstants.ANALYTICS_USER_EVENT_QUEUE)
    public void handleCourierRegistered(CourierRegisteredEvent payload) {
        System.out.println("Received Courier Registered Event: " + payload);
        // Implement your business logic here
    }
}
```

---

## Release Workflow for `mq-utils-lib`

### Branches

* **`dev`**: Ongoing development. Unstable.
* **`main`**: Stable release branch. Only merges for version releases.

---

### Release Workflow (For Admins / Release Managers)

1.  **Ensure `dev` is Ready:** All features/fixes completed; CI passes on `dev`.
2.  **Pull Latest Branches:**
    ```bash
    git checkout dev && git pull origin dev
    git checkout main && git pull origin main
    ```
3.  **Merge `dev` into `main`:**
    ```bash
    git merge dev
    ```
4.  **Verify `main` Build:**
    ```bash
    mvn clean verify
    ```
5.  **Update `pom.xml` Version:** Change from `*-SNAPSHOT` to `X.Y.Z`.
6.  **Commit Version Change:**
    ```bash
    git add pom.xml
    git commit -m "Release X.Y.Z"
    ```
7.  **Tag the Release:**
    ```bash
    git tag -a vX.Y.Z -m "Version X.Y.Z Release"
    ```
8.  **Push Commits and Tags:**
    ```bash
    git push origin main
    git push origin --tags
    ```
    > ⚠️ Pushing the tag triggers GitHub Actions + JitPack release.
9.  **Verify on GitHub:** Check Actions tab for success; Visit: [https://jitpack.io/#Podzilla/mq-utils-lib](https://jitpack.io/#Podzilla/mq-utils-lib)
