package com.example.socialnetwork.View.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.example.socialnetwork.Base.BaseActivity;
import com.example.socialnetwork.Context.RealmContext;
import com.example.socialnetwork.Model.Response.Message;
import com.example.socialnetwork.Model.Response.UserInfo;
import com.example.socialnetwork.Network.API;
import com.example.socialnetwork.Network.RetrofitService;
import com.example.socialnetwork.Network.RetrofitUtils;
import com.example.socialnetwork.R;
import com.example.socialnetwork.View.Chat.Adapter.MessageAdapter;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends BaseActivity {
    public static final String GROUP_ID = "group_id";
    public static final int REQ_CODE_SPEECH_INPUT = 1;
    @BindView(R.id.view_flipper)
    ViewFlipper viewFlipper;
    @BindView(R.id.rcv_messages)
    RecyclerView rcvMessage;
    @BindView(R.id.edt_message)
    EditText edtMessage;
    @BindView(R.id.imv_send)
    ImageView imvSend;
    @BindView(R.id.imv_mic)
    ImageView imvMic;

    private String groupId;
    private RetrofitService retrofitService = RetrofitUtils.getInstance().createService();

    private ArrayList<Message> messageList;
    private MessageAdapter messageAdapter;

    private UserInfo userInfo;

   private Socket socket;

    {
        try {
            socket = IO.socket(API.ROOT);
        } catch (URISyntaxException e) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        init();
        addListener();
        getAllMessage(groupId);
    }

    private void init() {
        groupId = (String) getIntent().getSerializableExtra(GROUP_ID);
        userInfo = RealmContext.getInstance().getUser();

        socket.connect();
        socket.emit("join_chat", groupId, userInfo.getId());
        socket.on("new_message", onNewMessageListener);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        rcvMessage.setAdapter(messageAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        rcvMessage.setLayoutManager(layoutManager);
    }

    private Emitter.Listener onNewMessageListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (args != null) {
                        String data = args[0].toString();
                        Message message = new Gson().fromJson(data, Message.class);
                        messageList.add(message);
                        messageAdapter.notifyDataSetChanged();
                        if (!messageList.isEmpty()) {
                            rcvMessage.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                        }
                    }
                }
            });
        }
    };

    private void addListener() {
        imvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edtMessage.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    showToast("Vui lòng nhập tin nhắn!");
                } else {
                    sendMessage(content);
                    edtMessage.setText("");
                }
            }
        });

        imvMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startInputSpeech();
            }
        });
    }

    private void getAllMessage(String groupId) {
        retrofitService.getAllMessage(groupId).enqueue(new Callback<ArrayList<Message>>() {
            @Override
            public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
                ArrayList<Message> list = response.body();
                if (response.code() == 200 && list != null && !list.isEmpty()) {
                    messageList.clear();
                    messageList.addAll(list);
                    messageAdapter.notifyDataSetChanged();
                }
                viewFlipper.setDisplayedChild(1);
            }

            @Override
            public void onFailure(Call<ArrayList<Message>> call, Throwable t) {
                viewFlipper.setDisplayedChild(1);
            }
        });
    }

    private void sendMessage(String content) {
        socket.emit("create_messge", groupId, userInfo.getId(), content);
    }

    /**
     * Showing google speech input dialog
     */
    private void startInputSpeech() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something...");

        startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                sendMessage(result.get(0));
            }
        }
    }
}