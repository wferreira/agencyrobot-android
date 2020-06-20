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

    private SerialService serialService;

    public MqttListenerService(Context applicationContext, SerialService serialService){
        this.serialService = serialService;
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(applicationContext, CLOUDMQTT_URL, clientId);
    }

    public void connectAndListen() throws MqttException {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(CLOUDMQTT_USER);
        options.setPassword(CLOUDMQTT_PWD.toCharArray());

        MqttListenerService self = this;

        client.connect(options).setActionCallback(new IMqttActionListener(){

            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                System.out.println("Mqtt connect success");
                try {
                    //asyncActionToken.getClient().su
                    self.subscribe();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                System.out.println("Mqtt connect failure");
                exception.printStackTrace();
            }
        });
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
        client.subscribe(CLOUDMQTT_TOPIC, 1, (topic, message) -> {
            System.out.println("MESSAGE RECEIVED : "+message.toString());
            switch (message.toString()){
                case "F":
                    serialService.forward();
                    break;
                case "B":
                    serialService.backward();
                    break;
                case "L":
                    serialService.turnLeft();
                    break;
                case "R":
                    serialService.turnRight();
                    break;
                case "S":
                    serialService.stop();
                    break;
            }

        });
    }
}
