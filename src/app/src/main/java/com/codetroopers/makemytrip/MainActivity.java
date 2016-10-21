package com.codetroopers.makemytrip;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.AIListener;
import ai.api.AIService;
import ai.api.AIServiceException;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class MainActivity extends NavigationActivity
        implements AIListener {
    private ListView listView;
    private EditText editText;
    private List<ChatMessage> chatMessages;
    private ArrayAdapter<ChatMessage> adapter;
    private AIService aiService;
    final AIConfiguration config = new AIConfiguration("7c34ac911bf74c5c93ea580e60fe90e6",
            AIConfiguration.SupportedLanguages.English,
            AIConfiguration.RecognitionEngine.System);
    private AIDataService aiDataService;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 124;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);
        getSupportActionBar().setTitle("Tour Guide");
        aiService = AIService.getService(this, config);
        aiService.setListener(this);
        aiDataService = new AIDataService(this,config);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_voice);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
            }
        });
        FloatingActionButton fabtext = (FloatingActionButton) findViewById(R.id.fab_text);
        fabtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getResponseText();
            }
        });

        chatMessages = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list_msg);
        editText = (EditText) findViewById(R.id.msg_type);
        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    getResponseText();
                    return true;
                }
                return false;
            }
        });

        //set ListView adapter first
        adapter = new MessageAdapter(this, R.layout.item_chat_left, chatMessages);
        listView.setAdapter(adapter);
    }
    private void getResponseText()
    {
        try{
            final AIRequest aiRequest = new AIRequest();
            aiRequest.setQuery(editText.getText().toString());
            ChatMessage chatMessage = new ChatMessage(editText.getText().toString(), false);
            chatMessages.add(chatMessage);
            adapter.notifyDataSetChanged();
            editText.setText("");
            new AsyncTask<AIRequest, Void, AIResponse>() {
                @Override
                protected AIResponse doInBackground(AIRequest... requests) {
                    final AIRequest request = requests[0];
                    try {
                        final AIResponse response = aiDataService.request(aiRequest);
                        return response;
                    } catch (AIServiceException e) {
                    }
                    return null;
                }
                @Override
                protected void onPostExecute(AIResponse aiResponse) {
                    if (aiResponse != null) {
                        Result result = aiResponse.getResult();

                        // Get parameters
                        String parameterString = "";
                        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
                            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
                            }
                        }
                        ChatMessage chatMessage = new ChatMessage(result.getFulfillment().getSpeech(), true);
                        chatMessages.add(chatMessage);
                        adapter.notifyDataSetChanged();
                        editText.setText("");
                    }
                }
            }.execute(aiRequest);
        }catch(Exception e){e.printStackTrace();}
    }
    @Override
    public void onError(AIError error) {
        ChatMessage chatMessage = new ChatMessage(error.getMessage(), true);
        chatMessages.add(chatMessage);
        adapter.notifyDataSetChanged();
        editText.setText("");
    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }
    public void onResult(final AIResponse response) {
        Result result = response.getResult();
        String parameterString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
            }
        }
        ChatMessage chatMessage = new ChatMessage(result.getResolvedQuery(), false);
        chatMessages.add(chatMessage);
        adapter.notifyDataSetChanged();
        editText.setText("");
        chatMessage = new ChatMessage(result.getFulfillment().getSpeech(), true);
        chatMessages.add(chatMessage);
        adapter.notifyDataSetChanged();
        editText.setText("");
    }
    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_CODE_ASK_PERMISSIONS);
        }
        else aiService.startListening();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    aiService.startListening();
                } else {
                    Snackbar.make(editText, "RECORD_AUDIO Permission Denied!", Snackbar.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
