import paho.mqtt.client as mqtt
import json
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
from collections import deque
from datetime import datetime
import threading

auth_username = "admin"
auth_password = "Admintest1"
broker_url = "04dff8ffebe94e9d9bcee24fb6e655ed.s1.eu.hivemq.cloud"

live_plotter = None

def create_client():
    client = mqtt.Client(transport='tcp',
                        protocol=mqtt.MQTTv5)
    client.tls_set(tls_version=mqtt.ssl.PROTOCOL_TLS)
    client.username_pw_set(auth_username, auth_password)
    port_tcp = 8883
    client.connect(broker_url,
                    port=port_tcp,
                    clean_start=mqtt.MQTT_CLEAN_START_FIRST_ONLY,
                    keepalive=60);
    return client

def report_connect_status(client, userdata, flags, rc, properties=None):
    print("trying to connect")
    if rc == 0:
        print("Connected to MQTT Broker!")
    else:
        print("Failed to connect with error code %d\n", rc)

# alternative callback that returns the success code when connecting
def on_connect(client, userdata, flags, rc, properties=None):
    print("CONNACK received with code %s." % rc)

# with this callback you can see if your publish was successful
def on_publish(client, userdata, mid, properties=None):
    print("mid: " + str(mid))

# print which topic was subscribed to
def on_subscribe(client, userdata, message_id, granted_qos, properties=None):
    print("Subscribed: " + str(message_id) + " " + str(granted_qos))

# print message, useful for checking if it was successful
def on_message(client, userdata, msg):
    global live_plotter

    print(msg.topic + " " + str(msg.qos))
    data = json.loads(msg.payload)

    print("\nPrestatie:")
    print("-" * 40)

    units = {
        "hartslag": "bpm",
        "systolische_bloeddruk": "mmHg",
        "lactaat_waardes": "mmol/L",
        "zuurstof_opname": "mL/kg/min",
        "hartminuutvolume": "L/min",
        "maximale_belasting": "W",
        "anaerobe_drempel": "bpm"
    }

    for key, value in data.items():
        unit = units.get(key, "")
        clean_key = key.replace("_", " ").title()
        if unit:
            print(f" - {clean_key}: {value} {unit}")
        else:
            print(f" - {clean_key}: {value}")
    print("-" * 40 + "\n")

    if live_plotter:
        live_plotter.add_data(data)

class LivePlotter:
    def __init__(self, max_points=50):
        self.max_points = max_points
        self.data = {
            "hartslag": deque(maxlen=max_points),
            "systolische_bloeddruk": deque(maxlen=max_points),
            "lactaat_waardes": deque(maxlen=max_points),
            "zuurstof_opname": deque(maxlen=max_points)
        }
        self.minutes = deque(maxlen=max_points)
        self._minute_counter = 1
        self.minute_limit = max_points
        self.timestamps = deque(maxlen=max_points)
        self.client = None
        self._stopped = False
        # Grotere figuur en betere spacing
        self.fig, self.axes = plt.subplots(2, 2, figsize=(10, 8))
        self.fig.suptitle("Live Prestatie Data", fontsize=16, fontweight='bold')
        self.ani = None
    
    def add_data(self, data_dict):
        # als al gestopt: niets toevoegen
        if self._stopped:
            return

        # append een virtuele "minuut" waarde (1..minute_limit)
        current_minute = self._minute_counter
        self.minutes.append(current_minute)

        # timestamps en data
        self.timestamps.append(datetime.now())
        for key in self.data.keys():
            if key in data_dict:
                self.data[key].append(data_dict[key])

        # increment, maar NIET terugwrappen — stop bij bereiken van limit
        if self._minute_counter < self.minute_limit:
            self._minute_counter += 1
        else:
            # MARKER: stop verdere verwerking, disconnect MQTT-client maar sluit het venster NIET
            self._stopped = True
            try:
                if self.client:
                    print(f"Minute limit ({self.minute_limit}) reached — disconnecting MQTT client.")
                    # disconnect in background/and or stop loop. loop_forever zal terugkeren.
                    self.client.disconnect()
            except Exception as e:
                print(f"Failed to disconnect client: {e}")
            # niet sluiten van plt.fig zodat het venster open blijft
            # plt.close(self.fig)  # verwijderd
                    
    def update_plot(self, frame):
        for idx, (metric, ax) in enumerate(zip(self.data.keys(), self.axes.flat)):
            ax.clear()
            if self.data[metric]:
                x = list(self.minutes)
                y = list(self.data[metric])
                if len(x) != len(y):
                    x = x[-len(y):]
                ax.plot(x, y, marker='o', linestyle='-', linewidth=2, markersize=5)
                ax.set_title(metric.replace("_", " ").title(), fontsize=12, fontweight='bold')
                ax.set_ylabel("Waarde", fontsize=10)
                ax.set_xlabel("Minuut", fontsize=10)
                ax.set_xlim(1, self.minute_limit)
                ax.set_xticks(range(1, self.minute_limit + 1, max(1, self.minute_limit // 10)))
                # Roteer labels om overlapping te voorkomen
                ax.tick_params(axis='x', rotation=45)
                ax.grid(True, alpha=0.3)
        
    def start(self):
        self.ani = FuncAnimation(self.fig, self.update_plot, interval=100)
        # Betere spacing met subplots_adjust
        self.fig.subplots_adjust(left=0.1, right=0.95, top=0.93, bottom=0.1, hspace=0.35, wspace=0.3)
        plt.show()
