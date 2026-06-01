import time
import random
from datetime import datetime

# Configuration
LOG_FILE = "server.log"
IPS = ["192.168.1.10", "10.0.0.5", "172.16.0.20", "192.168.1.50"]
ATTACKER_IP = "45.77.12.100"  # The "Bad Actor" IP
ACTIONS = ["LOGIN_SUCCESS", "PAGE_VIEW", "FILE_DOWNLOAD", "LOGOUT"]

def generate_log():
    print(f"Starting log generation in {LOG_FILE}... Press Ctrl+C to stop.")
    
    while True:
        with open(LOG_FILE, "a") as f:
            # 80% chance of normal traffic
            if random.random() > 0.2:
                ip = random.choice(IPS)
                action = random.choice(ACTIONS)
                status = "INFO"
            else:
                # 20% chance of a failed login (Simulating the threat)
                # Sometimes from random IPs, sometimes from the Attacker IP
                ip = ATTACKER_IP if random.random() > 0.5 else random.choice(IPS)
                action = "LOGIN_FAILED"
                status = "WARNING"

            timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
            log_entry = f"{timestamp} - {status} - IP: {ip} - {action}\n"
            
            f.write(log_entry)
            print(f"Logged: {log_entry.strip()}")
            
        # Wait a random bit so it looks like real human activity
        time.sleep(random.uniform(0.5, 2.0))

if __name__ == "__main__":
    generate_log()