package edu.asu.msama1.simplenotification;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat;
import android.app.Notification;
import android.widget.Button;


public class HandHeldActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_held);
        Button mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Notification notification = new NotificationCompat.Builder(getApplication())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Hello World")
                        .setContentText("My first Android Wear notification")
                        .extend(
                                new NotificationCompat.WearableExtender().setHintShowBackgroundOnly(true))
                        .build();
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplication());
                int notificationId = 1;
                notificationManager.notify(notificationId, notification);
            }

        });
    }
}
