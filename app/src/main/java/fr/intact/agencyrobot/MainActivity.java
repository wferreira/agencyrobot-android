package fr.intact.agencyrobot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

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
        mqttListenerService = new MqttListenerService(this.getApplicationContext(), serialService);

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
        serialService.turnLeft();
    }

    @OnClick(R.id.top_arrow) void topArrowClicked()  {
        Toast.makeText(getApplicationContext(), R.string.app_name, Toast.LENGTH_LONG).show();
        serialService.forward();
    }

    @OnClick(R.id.right_arrow) void rightArrowClicked()  {
        Toast.makeText(getApplicationContext(), R.string.app_name, Toast.LENGTH_LONG).show();
        serialService.turnRight();
    }

    @OnClick(R.id.bottom_arrow) void bottomArrowClicked()  {
        Toast.makeText(getApplicationContext(), R.string.app_name, Toast.LENGTH_LONG).show();
        serialService.backward();
    }

    @OnClick(R.id.stop_arrow) void stopClicked()  {
        Toast.makeText(getApplicationContext(), R.string.app_name, Toast.LENGTH_LONG).show();
        serialService.stop();
    }

    @OnClick(R.id.connect_mqtt) void connectMqtt()  {
        try {
            mqttListenerService.connectAndListen();
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

    @OnClick(R.id.open_chat_button) void openChatActivity()  {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://meet.jit.si/agency-robot-paris"),this, ChatActivity.class);
        startActivity(intent);
    }
}
