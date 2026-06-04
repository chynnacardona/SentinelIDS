# SentinelWatch: Real-Time Intrusion Detection & Automated Mitigation System

SentinelWatch is an enterprise-inspired, full-stack Intrusion Detection System (IDS) and Automated Mitigation platform. It continuously ingests streaming infrastructure logs, analyzes connection behaviors against security rule matrices in real time, pushes event notifications across reactive WebSockets to a tailored security operations dashboard, and automatically deploys network firewall bans to drop active attackers.

---

## 🛠️ Tech Stack

### 🔹 Backend & Security Core
* **Java 17/21 & Spring Boot:** Powers the core infrastructure API routing, security handling, and event dispatch pipelines.
* **Spring Data JPA:** Manages object-relational mapping and database communication.
* **H2 Database Engine:** An in-memory relational database utilized to store real-time logs, active security alerts, and the firewall blacklist state.
* **Spring WebSockets (STOMP / SockJS):** Drives the low-latency, bi-directional message broker topology that streams alerts to the frontend without page refreshing.

### 🔹 Security Sensor Agent
* **Python 3:** Implements a lightweight, low-overhead background logging sensor script.
* **Regular Expressions (`re`):** Utilized for fast string token parsing to monitor server event inputs.
* **Requests Library:** Drives the connection layer to transmit processed alert packets to the Spring Boot REST endpoints.

### 🔹 Frontend Operations Interface
* **HTML5 & Vanilla JavaScript (ES6):** Controls the real-time document manipulation framework and active WebSocket client listeners.
* **Thymeleaf:** Acts as the server-side template engine to pre-render historical alert registries straight out of the database on system load.
* **Custom CSS Glassmorphism:** Incorporates customized dark-mode themes, visual layout scaling, and reactive structural alert styling elements.

---

## 📁 Repository Structure

```text
├── sentinel-backend/                 # Spring Boot Java Application
│   ├── src/main/java/com/sentinelwatch/ids/
│   │   ├── IdsApplication.java       # Main entry application hook
│   │   ├── model/
│   │   │   ├── Alert.java            # Event model schema entity
│   │   │   └── BlacklistedIP.java    # Mitigation database model
│   │   ├── repository/
│   │   │   ├── AlertRepository.java  # Persistent storage connector interface
│   │   │   └── BlacklistRepository.java # Blacklist lookup engine
│   │   ├── controller/
│   │   │   └── AlertController.java  # REST API endpoints & automated mitigation routing
│   │   └── config/
│   │       └── FirewallFilter.java   # Core network firewall interceptor
│   └── src/main/resources/templates/
│       └── dashboard.html            # Realtime SOC UI panel with WebGL backgrounds
│
└── security-sensor/                  # Lightweight Python Sensor Components
    ├── server.log                    # Target monitored server log stream file
    ├── log_generator.py              # Simulates live network attack data strings
    └── detector.py                   # Parsing engine & threat transmission agent
