# SentinelWatch: Real-Time Intrusion Detection & Automated Mitigation System

SentinelWatch is an enterprise-inspired, full-stack Intrusion Detection System (IDS) and Automated Mitigation platform. It continuously ingests streaming infrastructure logs, analyzes connection behaviors against security rule matrices in real time, pushes event notifications across reactive WebSockets to a tailored security operations dashboard, and automatically deploys network firewall bans to drop active attackers.

---

## 🛠️ Tech Stack

### 🔹 Backend & Security Core
* **Java 21 & Spring Boot:** Powers the core infrastructure API routing, security handling, and event dispatch pipelines.
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
│   │   ├── filter/                   # Core network request interceptors
│   │   │   ├── ApiKeyAuthFilter.java # Header token authentication validator
│   │   │   └── FirewallFilter.java   # Active network firewall block filter
│   │   ├── controller/
│   │   │   └── AlertController.java  # REST API endpoints & automated mitigation routing
│   │   ├── model/
│   │   │   ├── Alert.java            # Event model schema entity
│   │   │   └── BlacklistedIp.java    # Mitigation database model
│   │   └── repository/
│   │       ├── AlertRepository.java  # Persistent storage connector interface
│   │       └── BlacklistRepository.java # Blacklist lookup engine
│   └── src/main/resources/templates/
│       └── dashboard.html            # Realtime SOC UI panel with WebGL backgrounds
│
└── security-sensor/                  # Lightweight Python Sensor Components
    ├── server.log                    # Target monitored server log stream file
    ├── log_generator.py              # Simulates live network attack data strings
    └── detector.py                   # Parsing engine & threat transmission agent
```
---
  
## 💻 How to Run and Simulate the System Locally

To see the end-to-end detection, alerting, and automated firewall mitigation loop in action, open your terminal workspace and execute the following configurations:

### 🛠️ Terminal 1: Install Dependencies & Run Python Log Generator
Open a fresh terminal window, navigate to your sensor package, install the HTTP payload agent library, and start appending dummy infrastructure logs:

```bash
# Navigate to the security sensor folder
cd ~/Desktop/"Intrusion Detection System"/security-sensor

# Install required library for network transmission
pip3 install requests

# Start the log simulation engine script
python3 log_generator.py
```
### 🛠️ Terminal 2: Run the Python Security Sensor Agent

Open a second terminal window or split panel inside VS Code to start tailing the log file and evaluating threat metrics:
Bash

Navigate to the security sensor folder

        cd ~/Desktop/"Intrusion Detection System"/security-sensor

Run the real-time detector parsing service

        python3 detector.py

### 🛠️ Terminal 3: Launch the Spring Boot Core & Filter Engine

Open a third terminal window to start the primary Java application server framework:
Bash

Navigate into your backend core directory

    cd ~/Desktop/"Intrusion Detection System"/sentinel-backend

Initialize, build, and boot up the Spring Boot engine

    ./mvnw spring-boot:run

Note: The core framework will boot up and bind its REST API and WebSocket broker endpoints locally onto port 8081.

🔍 Simulation Verification (What to Expect)
1. Monitor the Real-Time SOC Panel

Open your preferred web browser and navigate to: 
    
    http://localhost:8081/dashboard

As the log generator creates records, the Python sensor captures them and streams them to the web UI instantly over WebSockets. Standard failed entries will slide into the table using a clean cyan color theme.

2. Watch the Automated Escalation Rule

When an IP address triggers a 4th failed login attempt (failed_attempts > 3), the system escalates:
   The row injected into the dashboard will dynamically apply the .threshold-hit class and flash bright red.

    The local terminal console will output: ALERT: Potential brute-force attack detected!

3. Verify the Active Firewall Block

    Look at your Spring Boot application terminal console (Terminal 3). You will see an immediate active mitigation log trip: [MITIGATION ACTIVE] Automatically blacklisted hostile IP: 45.77.12.100.

    Testing the block on yourself: To simulate a total local block, push an alert containing your machine's loopback IP (127.0.0.1). The moment it hits the blacklist database table, refresh your browser window.

    The FirewallFilter will intercept your connection, kill the request pipeline, and display the flat security block screen:

    🛑 ACCESS DENIED: Your IP address (127.0.0.1) has been blacklisted by the SentinelWatch IDS Engine.
