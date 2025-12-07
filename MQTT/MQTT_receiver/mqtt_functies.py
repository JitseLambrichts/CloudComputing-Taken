import paho.mqtt.client as mqtt
import json

auth_username = "jitsejitse"
auth_password = "Wachtwoord1"
broker_url = "04dff8ffebe94e9d9bcee24fb6e655ed.s1.eu.hivemq.cloud"

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
    print(msg.topic + " " + str(msg.qos) + " " + str(msg.payload))
    data = json.loads(msg.payload)
    print(data['hartslag'])
    # hartslag = data['ritme']
    # if hartslag < 50 :
    #     print("wakey wakey")
    # elif hartslag > 120 :
    #     print("Rustaaagggg")
