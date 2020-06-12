package fr.intact.agencyrobot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.intact.agencyrobot.service.MqttListenerService;
import fr.intact.agencyrobot.service.SerialService;

public class MainActivity extends AppCompatActivity {

    private SerialService serialService;
    private MqttListenerService mqttListenerService;

    @BindView(R.id.connection_arduino_status) TextView connectionStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serialService = new SerialService(this);
        mqttListenerService = new MqttListenerService(this.getApplicationContext());

        ButterKnife.bind(this);
    }

    @OnClick(R.id.connect_arduino) void connectSerial()  {
        try {
            if (serialService.connect()){
                connectionStatus.setText("Connected");
            }else{
                connectionStatus.setText("Disconnected");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.disconnect_arduino) void disconnectSerial()  {
        try {
            serialService.disconnect();
            connectionStatus.setText("Disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.left_arrow) void leftArrowClicked()  {
        Toast.makeText(getApplicationContext(), R.string.app_name, Toast.LENGTH_LONG).show();
        try {
            serialService.write("L");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.top_arrow) void topArrowClicked()  {
        Toast.makeText(getApplicationContext(), R.string.app_name, Toast.LENGTH_LONG).show();
        try {
            serialService.write("F");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.right_arrow) void rightArrowClicked()  {
        Toast.makeText(getApplicationContext(), R.string.app_name, Toast.LENGTH_LONG).show();
        try {
            serialService.write("R");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.bottom_arrow) void bottomArrowClicked()  {
        Toast.makeText(getApplicationContext(), R.string.app_name, Toast.LENGTH_LONG).show();
        try {
            serialService.write("B");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.stop_arrow) void stopClicked()  {
        Toast.makeText(getApplicationContext(), R.string.app_name, Toast.LENGTH_LONG).show();
        try {
            serialService.write("S");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.connect_mqtt) void connectMqtt()  {
        try {
            IMqttToken iMqttToken = mqttListenerService.connect();
            iMqttToken.setActionCallback(new IMqttActionListener(){

                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    System.out.println("Mqtt connect success");
                    try {
                        mqttListenerService.subscribe();
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
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.disconnect_mqtt) void disconnectMqtt()  {
        try {
            mqttListenerService.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
