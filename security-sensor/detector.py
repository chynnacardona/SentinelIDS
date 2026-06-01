import time
import re #regex
import requests

def send_alert(ip_address, threat_type, timestamp):
    url = "http://localhost:8081/api/alerts"

    payload = {
        "ip_address": ip_address,
        "threat_type": threat_type,
        "timestamp": timestamp
    }

    headers = {
        "X-API-Key": "SentinelWatch-IDS-Key-Tester",
        "Content-Type": "application/json"
    }

    try:
        response = requests.post(url, json=payload, headers=headers)
        if response.status_code == 201:
            print(f"[+] Successfully logged threat from {ip_address} to backend.")
        else:
            print(f"[-] Backend rejected alert: {response.status_code} - {response.text}")
    except Exception as e:
        print(f"[-] Failed to connect to Spring Boot: {e}")

LOG_FILE = "server.log"
LOG_PATTERN = r"- (\w+) - IP: ([\d\.]+) - ([\w_]+)"
# Groups: (Status) - IP: (IP) - (Action)
failed_attempts = {}

def monitor():
    print(f"--- SentinelWatch Sensor Active ---")
    print(f"Monitoring {LOG_FILE} for suspicious activity... Press Ctrl+C to stop.")
    
    try: 
        with open(LOG_FILE, "r") as f:
            # Move the cursor to the end of the file
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
        status = match.group(1) # WARNING or INFO
        ip = match.group(2) # IP Address
        action = match.group(3) # Action (e.g. LOGIN_FAILED)

        if status == "WARNING" and action == "LOGIN_FAILED":
            # 1. If IP is new, start at 0
            if ip not in failed_attempts:
                failed_attempts[ip] = 0
            
            # 2. ALWAYS increment, whether it was new or already existed
            failed_attempts[ip] += 1
            
            print(f"Suspicious activity detected from IP: {ip} (Failed attempts: {failed_attempts[ip]})")
            
            # 3. Check threshold
            # If we see more than 3 failed attempts from the same IP, we can flag it
            if failed_attempts[ip] > 3:
                print(f"ALERT: Potential brute-force attack detected from IP: {ip}!")

if __name__ == "__main__":
    monitor()  