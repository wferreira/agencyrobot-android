package fr.intact.agencyrobot.service;

import android.content.Context;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttListenerService {

    private static final String CLOUDMQTT_URL = "";
    private static final String CLOUDMQTT_TOPIC = "";
    private static final String CLOUDMQTT_USER = "";
    private static final String CLOUDMQTT_PWD = "";

    private MqttAndroidClient client;

    public MqttListenerService(Context applicationContext){
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(applicationContext, CLOUDMQTT_URL, clientId);
    }

    public IMqttToken connect() throws MqttException {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(CLOUDMQTT_USER);
        options.setPassword(CLOUDMQTT_PWD.toCharArray());

        return client.connect(options);
    }

    public void disconnect() throws MqttException {
        client.disconnect().setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                System.out.println("Mqtt disconnect success");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                System.out.println("Mqtt disconnect failure");
            }
        });
    }

    public void subscribe() throws MqttException {
        IMqttToken subToken = client.subscribe(CLOUDMQTT_TOPIC, 1, (topic, message) -> System.out.println("MESSAGE RECEIVED : "+message.toString()));
    }
}
