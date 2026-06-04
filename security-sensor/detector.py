import time
import requests
import re #regex
from datetime import datetime

# Configuration variables
LOG_FILE = "server.log"
# Custom pattern capturing Groups: (Status) - IP: (IP Address) - (Action)
LOG_PATTERN = r"- (\w+) - IP: ([\d\.]+) - ([\w_]+)"
URL = "http://localhost:8081/api/alerts"

# Internal tracking matrix state dictionary
failed_attempts = {}

def send_alert(ip_address, threat_type, timestamp, current_count):
    # Payload keys use standard camelCase to seamlessly map directly into your Spring Boot Alert fields
    payload = {
        "ipAddress": ip_address,
        "threatType": f"{threat_type} (Attempt {current_count})",
        "timestamp": timestamp,
        "attemptCount": current_count # Read by the frontend browser script to trigger the RED style
    }

    headers = {
        "X-API-Key": "SentinelWatch-IDS-Key-Tester",
        "Content-Type": "application/json"
    }

    try:
        # Fires off the payload on EVERY single logging instance update cycle
        response = requests.post(URL, json=payload, headers=headers)
        if response.status_code == 201 or response.status_code == 200:
            pass # Successfully broadcasted to pipeline
        else:
            print(f"[-] Backend rejected alert: {response.status_code} - {response.text}")
    except Exception as e:
        print(f"[-] Failed to connect to Spring Boot: {e}")

def monitor():
    print(f"--- SentinelWatch Sensor Active ---")
    print(f"Monitoring {LOG_FILE} for suspicious activity... Press Ctrl+C to stop.\n")
    
    try: 
        with open(LOG_FILE, "r") as f:
            # Move the cursor to the end of the file to capture live additions
            f.seek(0, 2)
            
            while True:
                line = f.readline()
                
                if not line:
                    time.sleep(0.1)  # Sleep briefly to avoid busy waiting
                    continue
                
                process_line(line.strip())
            
    except FileNotFoundError:
        print(f"Error: {LOG_FILE} not found. Please run the log generator first.")
    
def process_line(line):
    match = re.search(LOG_PATTERN, line)

    if match:
        status = match.group(1)   # WARNING or INFO
        ip = match.group(2)       # IP Address
        action = match.group(3)   # Action (e.g. LOGIN_FAILED)

        if status == "WARNING" and action == "LOGIN_FAILED":
            # 1. If IP is new, start at 0
            if ip not in failed_attempts:
                failed_attempts[ip] = 0
            
            # 2. ALWAYS increment, whether it was new or already existed
            failed_attempts[ip] += 1
            
            current_count = failed_attempts[ip]
            timestamp_str = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
            
            # Print to local terminal window console layout
            print(f"Suspicious activity detected from IP: {ip} (Failed attempts: {current_count})")
            
            # 3. Check threshold logic
            if current_count > 3:
                print(f"ALERT: Potential brute-force attack detected from IP: {ip}!")
            
            # Execute transmission channel payload to Spring Boot on EVERY step update loop
            send_alert(ip, "SUSPICIOUS_BRUTE_FORCE", timestamp_str, current_count)

if __name__ == "__main__":
    monitor()