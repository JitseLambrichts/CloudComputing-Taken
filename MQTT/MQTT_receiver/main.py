from mqtt_functies import *

#voorbeeld van een regelmatige publish
import time
def send_messages(client):
    for i in range(1,10):
        client.publish("my_topic", "msg " + str(i))
        time.sleep(0.25)

if __name__ == '__main__':
    print("************")
    print("**  MQTT  **")
    print("************")
    client = create_client()
    client.on_connect = report_connect_status
    # client.on_publish = on_publish

    client.on_subscribe = on_subscribe
    client.on_message = on_message

    client.subscribe("prestatie")

    # send_messages(client)   # Nodig om de service op te roepen
    client.loop_forever()

