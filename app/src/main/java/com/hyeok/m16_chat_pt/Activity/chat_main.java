package com.hyeok.m16_chat_pt.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyeok.m16.chat_pt.Utils.BinaryUtil;
import com.hyeok.m16_chat_pt.app.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;


public class chat_main extends ActionBarActivity implements View.OnClickListener{
    private String TAG = "M16_CHAT";
    private TextView CHAT_TEXTVIEW;
    private Button CHAT_BUTTON;
    private EditText CHAT_EDITTEXT;
    private ScrollView CHAT_SCROLL;
    private Handler CHAT_TV_HANDLER;
    private final String ID = "m16";
    private final String PW = "aa";
    private ChatOutputStream chatOutputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);
        BinaryUtil.getInstance().CheckBinaryFile(this);
        ViewInit();
        ChatInit();
        HandlerInit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == CHAT_BUTTON.getId()) {
            if(!CHAT_EDITTEXT.getText().toString().isEmpty()) {
                chatOutputStream.sendMessage(CHAT_EDITTEXT.getText().toString());
                CHAT_EDITTEXT.setText("");
            } else {
                Toast.makeText(this, "메세지를 입력하세요.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void ScrollDown() {
        CHAT_SCROLL.post(new Runnable() {
            @Override
            public void run() {
                CHAT_SCROLL.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }
    private void ChatInit() {
        try {
            String FILE_DIR = BinaryUtil.getInstance().GetBinaryPath(this);
            Log.i(TAG, "ChatInit()");
            Log.i(TAG, FILE_DIR);
            Process process = Runtime.getRuntime().exec(FILE_DIR+" -a --client=W3XP  m16.ggu.la");
            // 입력 스트림
            OutputStream outputStream = process.getOutputStream();
            chatOutputStream = new ChatOutputStream(outputStream);
            // 기본 스트림 출력.
            InputStream inputStream = process.getInputStream();
            ChatInputStream chatInputStream = new ChatInputStream(inputStream);

            // 에러 스트림 출력.
            InputStream errorinputStream = process.getErrorStream();
            ErrorStream errorStream = new ErrorStream(errorinputStream);
            // 스레드 실행.
            chatOutputStream.start();
            chatInputStream.start();
            errorStream.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ChatOutputStream extends Thread {
        private OutputStream chatoutputStream;

        public ChatOutputStream(OutputStream chatoutputStream) {
            this.chatoutputStream = chatoutputStream;
        }

        public boolean sendMessage(String msg) {
            try {
                chatoutputStream.write((msg.getBytes()));
                Enter();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        private void Enter() throws IOException {
            chatoutputStream.write("\n".getBytes());
        }

        @Override
        public void run() {
            try {
                chatoutputStream.write((ID + "\n").getBytes());
                chatoutputStream.write((PW + "\n").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    class ChatInputStream extends Thread {
        private InputStream inputStream;

        public ChatInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String temp = null;
            try {
                while((temp = in.readLine()) != null) {
                    Message str_msg = Message.obtain();
                    str_msg.obj = temp;
                    if (!temp.equals("[0m] ") && !temp.equals("] ")) {
                        str_msg.obj = ((String)str_msg.obj).replace("[0m]     ", "");
                        str_msg.obj = ((String)str_msg.obj).replace("<Info>", "");
                        str_msg.obj = ((String)str_msg.obj).replace("<Error>", "");
                        str_msg.obj = ((String)str_msg.obj).replace("[33m ", "");
                        Log.d(TAG, str_msg.obj.toString());
                        CHAT_TV_HANDLER.sendMessage(str_msg);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class ErrorStream extends Thread {
        private InputStream inputStream;

        public ErrorStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String temp = null;
            try {
                while ((temp = in.readLine()) != null) {
                    Log.i(TAG, temp);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void ViewInit() {
        CHAT_TEXTVIEW = (TextView)findViewById(R.id.CHAT_VIEW);
        CHAT_BUTTON = (Button)findViewById(R.id.CHAT_BUTTON);
        CHAT_EDITTEXT = (EditText)findViewById(R.id.CHAT_EDITTEXT);
        CHAT_BUTTON.setOnClickListener(this);
        CHAT_SCROLL = (ScrollView)findViewById(R.id.CHAT_SCROLL);
    }

    private void HandlerInit() {
        CHAT_TV_HANDLER = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                CHAT_TEXTVIEW.append((String) msg.obj);
                CHAT_TEXTVIEW.append("\n");
                ScrollDown();
            }
        };
    }
}
