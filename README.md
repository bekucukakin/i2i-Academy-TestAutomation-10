# i2i Academy — Test Automation (Homework 10)

This repository is my submission for the **i2i Academy Test Automation** course homework. It covers three layers of test automation on top of two public demo targets, plus a small performance/stress test setup built with Docker and Apache JMeter.

## What's inside

| Layer | Tool | Target |
|---|---|---|
| UI Automation | Selenium WebDriver + JUnit 5 | [saucedemo.com](https://www.saucedemo.com) |
| API Automation | REST Assured + JUnit 5 | [jsonplaceholder.typicode.com](https://jsonplaceholder.typicode.com) |
| Performance Testing | Apache JMeter | Dummy Nginx endpoint (Docker) |

## Tech Stack

- **Language / Build:** Java 17, Maven
- **UI Testing:** Selenium 4.26, JUnit 5 (Jupiter)
- **API Testing:** REST Assured 5.5, JSON Path
- **Performance Testing:** Docker (Nginx), Apache JMeter 5.6.3

## Project Structure

```
.
├── pom.xml
├── nginx/
│   └── default.conf              # dummy JSON endpoint served by Nginx for the stress test
├── jmeter/
│   └── dummy-api-stress-test.jmx # JMeter test plan (100 users, 10 loops = 1000 requests)
└── src/test/java/com/i2iacademy/testautomation/
    ├── ui/
    │   └── LoginTest.java        # Selenium login scenarios
    └── api/
        └── PostsApiTest.java     # REST Assured API scenarios
```

## How to Run

### 1. UI tests (Selenium)

```bash
mvn test -Dtest=LoginTest
```

Covers:
- A valid login (`standard_user`) redirects to the inventory page.
- A locked-out login (`locked_out_user`) shows the correct error message.

### 2. API tests (REST Assured)

```bash
mvn test -Dtest=PostsApiTest
```

Covers:
- `GET /posts/1` returns `200` with the expected fields (`id`, `userId`, `title`).
- `GET /posts/1` responds within an acceptable time window.
- `POST /posts` returns `201` and echoes back the submitted body.

### 3. Performance test (JMeter + Docker)

Spin up the dummy Nginx endpoint:

```bash
docker run -d --name nginx-dummy-api -p 8081:80 \
  -v "$(pwd)/nginx/default.conf:/etc/nginx/conf.d/default.conf:ro" nginx:alpine
```

Verify it's running:

```bash
curl http://localhost:8081/api/status
```

Then open `jmeter/dummy-api-stress-test.jmx` in Apache JMeter and hit **Start**. The plan simulates 100 concurrent users, each looping 10 times (1000 requests total), against `GET /api/status`.

When finished, stop the container:

```bash
docker stop nginx-dummy-api
docker rm nginx-dummy-api
```
