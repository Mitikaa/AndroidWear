package edu.asu.msama1.messageApiSample;

import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by Mitikaa on 10/8/16.
 */

public class WearableListListener extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent){
        super.onMessageReceived(messageEvent);
        String event = messageEvent.getPath();
        Log.d("List item clicked", event);
        String[] message = event.split(":");
        if(message[0].equals("WearListClicked")){
            //do nothing
        }
    }
}
