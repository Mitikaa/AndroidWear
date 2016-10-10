package edu.asu.msama1.messageApiSample;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;


import java.util.ArrayList;

public class WearableActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        WearableListView.ClickListener{

    WearableListView mWearableListView;
    private ArrayList<String> items;
    Node mNode;
    GoogleApiClient mGoogleApiCLient;
    private boolean mError = false;
    public static String TAG = "WearableActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearable);

        mGoogleApiCLient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        addListItems();
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mWearableListView = (WearableListView) stub.findViewById(R.id.listView1);

                mWearableListView.setAdapter(new MyAdapter(WearableActivity.this, items));
                mWearableListView.setClickListener(WearableActivity.this);

            }
        });
    }

    private void addListItems() {
        items = new ArrayList<>();
        items.add("apple");
        items.add("milk");
        items.add("cereal");
        items.add("cheese");
        items.add("pie");
        items.add("ice-cream");
        items.add("tomatoes");
        items.add("pizza");
    }

    @Override
    protected void onStart(){
        super.onStart();
        mGoogleApiCLient.connect();
    }

    private void identifyConnectedNode(){
        Wearable.NodeApi.getConnectedNodes(mGoogleApiCLient)
                .setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                    @Override
                    public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                        for(Node node:getConnectedNodesResult.getNodes()){
                            mNode = node;
                        }
                    }
                });
    }

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {
        TextView textView = (TextView) viewHolder.itemView.findViewById(R.id.row_tv_name);
        String text = textView.getText().toString();
        Log.d(TAG, text);
        sendMesaage(text);
    }

    @Override
    public void onTopEmptyRegionClick() {
        Toast.makeText(this, "You clicked on top region", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        identifyConnectedNode();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void sendMesaage(String text){
        if(mNode!=null && mGoogleApiCLient!=null && mGoogleApiCLient.isConnected()){
            Log.d(TAG, ":"+mGoogleApiCLient.isConnected());
            Wearable.MessageApi.sendMessage(mGoogleApiCLient,mNode.getId(), "WearListClicked : " + text, null).setResultCallback(
                    new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(@NonNull MessageApi.SendMessageResult sendMessageResult) {
                            if(!sendMessageResult.getStatus().isSuccess()){
                                Log.d(TAG, "Failed to send message : "+ sendMessageResult.getStatus().getStatusCode());
                            }
                        }
                    }
            );
        }
    }


    private class MyAdapter extends WearableListView.Adapter{
        private final LayoutInflater mInflater;
        private ArrayList<String> data;

        private MyAdapter(Context context, ArrayList<String> listItems) {
            mInflater = LayoutInflater.from(context);
            data = listItems;
        }

        @Override
        public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new WearableListView.ViewHolder(
                    mInflater.inflate(R.layout.row_wear_list, null));
        }

        @Override
        public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
            TextView view = (TextView) holder.itemView.findViewById(R.id.row_tv_name);
            view.setText(data.get(position));
            holder.itemView.setTag(position);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
