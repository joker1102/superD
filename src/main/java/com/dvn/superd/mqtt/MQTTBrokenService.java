package com.dvn.superd.mqtt;

import io.vertx.core.Vertx;
import io.vertx.mqtt.MqttServer;
import org.springframework.stereotype.Component;

@Component
public class MQTTBrokenService {

    public MQTTBrokenService() {
        MqttServer mqttServer = MqttServer.create(Vertx.vertx());
        mqttServer.endpointHandler(endpoint -> {
            // shows main connect info
            System.out.println("MQTT client [" + endpoint.clientIdentifier() + "] request to connect, clean session = " + endpoint.isCleanSession());

            if (endpoint.auth() != null) {
                System.out.println("[username = " + endpoint.auth().getUsername() + ", password = " + endpoint.auth().getPassword() + "]");
            }
            System.out.println("[properties = " + endpoint.connectProperties() + "]");
            if (endpoint.will() != null) {
                System.out.println("[will topic = " + endpoint.will().getWillTopic() + " msg = " + new String(endpoint.will().getWillMessageBytes()) +
                        " QoS = " + endpoint.will().getWillQos() + " isRetain = " + endpoint.will().isWillRetain() + "]");
            }

            System.out.println("[keep alive timeout = " + endpoint.keepAliveTimeSeconds() + "]");

            // accept connection from the remote client
            endpoint.accept(false);

//            endpoint.publishHandler(message ->{
//
//            });

            endpoint.disconnectMessageHandler(disconnectMessage -> {

                System.out.println("Received disconnect from client, reason code = " + disconnectMessage.code());
            });
        }).listen(ar -> {

            if (ar.succeeded()) {

                System.out.println("MQTT server is listening on port " + ar.result().actualPort());
            } else {

                System.out.println("Error on starting the server");
                ar.cause().printStackTrace();
            }
        });
    }
}
