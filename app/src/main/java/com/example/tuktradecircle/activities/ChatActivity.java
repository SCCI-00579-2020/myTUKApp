package com.example.tuktradecircle.activities;

import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.tm.R;


import com.example.tuktradecircle.adapters.MessageAdapter;
import com.example.tuktradecircle.models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView chatListRecyclerView;
    private EditText messageEditText;
    private Button sendButton;
    private List<Message> messageList;
    private MessageAdapter messageAdapter;

  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //initialize firebase messaging
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseMessaging.getInstance().getToken();
        // Initialize views
        chatListRecyclerView = findViewById(R.id.chatListRecyclerView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        // Initialize message list and adapter
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);

        // Set up RecyclerView
        chatListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatListRecyclerView.setAdapter(messageAdapter);

        // Set up send button click listener
        sendButton.setOnClickListener(v -> {
            // Get message content from EditText
            String messageContent = messageEditText.getText().toString().trim();
            if (!messageContent.isEmpty()) {
                // Create a new message
                Message message = new Message(messageContent, "sender_name", System.currentTimeMillis());
                // Add message to the list
                messageList.add(message);
                // Notify adapter of new message
                messageAdapter.notifyItemInserted(messageList.size() - 1);
                // Scroll RecyclerView to the bottom
                chatListRecyclerView.scrollToPosition(messageList.size() - 1);
                // Clear EditText
                messageEditText.setText("");
            }
        });
    }
}

