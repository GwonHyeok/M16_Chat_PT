package com.hyeok.m16_chat_pt.Activity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.hyeok.m16_chat_pt.CustomView.ChatViewpager;
import com.hyeok.m16_chat_pt.CustomView.PagerSlidingTabStrip;
import com.hyeok.m16_chat_pt.CustomView.SpinnerArrayAdapter;
import com.hyeok.m16_chat_pt.CustomView.ViewPagerAdapter;
import com.hyeok.m16_chat_pt.Utils.BinaryUtil;
import com.hyeok.m16_chat_pt.Utils.PreferencesControl;
import com.hyeok.m16_chat_pt.app.R;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;


public class chat_main extends ActionBarActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private String TAG = "M16_CHAT";
    private PagerSlidingTabStrip pagerSlidingTabStrip;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private Button CHAT_BUTTON;
    private EditText CHAT_EDITTEXT;
    private Spinner CHAT_SPINNER;
    private Handler CHAT_TV_HANDLER, CHAT_TOAST_HANDLER;
    private static ChatOutputStream chatOutputStream;
    private ChatInputStream chatInputStream;
    private ErrorStream errorStream;
    private Process process;
    private boolean SHOW_CHAT_START = false, SHOW_FRIEND_LIST = false, SHOW_CLAN_LIST = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);
        ViewInit();
        ChatInit();
        HandlerInit();
        NotificationInit();
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
    public void onDestroy() {
        super.onDestroy();
        InterrupThread();
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == CHAT_BUTTON.getId()) {
            if(!CHAT_EDITTEXT.getText().toString().isEmpty()) {
                // 스피너조건 확인 모두에게 클랜에게 친구에게...
                switch (CHAT_SPINNER.getSelectedItemPosition()) {
                    case 0: // 모두에게...
                        chatOutputStream.sendMessage(CHAT_EDITTEXT.getText().toString());
                        break;
                    case 1: // 친구에게...
                        if(CHAT_EDITTEXT.getText().toString().substring(0,1).equals("/")) {
                            chatOutputStream.sendMessage(CHAT_EDITTEXT.getText().toString());
                            break;
                        }
                        chatOutputStream.sendMessage("/f msg "+CHAT_EDITTEXT.getText().toString());
                        break;
                    case 2: // 클랜에게...
                        if(CHAT_EDITTEXT.getText().toString().substring(0,1).equals("/")) {
                            chatOutputStream.sendMessage(CHAT_EDITTEXT.getText().toString());
                            break;
                        }
                        chatOutputStream.sendMessage("/c msg "+CHAT_EDITTEXT.getText().toString());
                        break;
                }
                CHAT_EDITTEXT.setText("");
            } else {
                Toast.makeText(this, "메세지를 입력하세요.", Toast.LENGTH_SHORT).show();
            }
        } else if(v.getId() == CHAT_EDITTEXT.getId()) {
                ScrollDown();
        }
    }

    @SuppressLint("NewApi")
    private void NotificationInit() {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, chat_main.class), PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Builder mBuilder = new Notification.Builder(this);
            mBuilder.setSmallIcon(R.drawable.ic_launcher);
            mBuilder.setWhen(System.currentTimeMillis());
            mBuilder.setNumber(0);
            mBuilder.setContentTitle("M16채팅이 실행중 입니다...");
            mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
            mBuilder.setContentIntent(pendingIntent);
            mBuilder.setOngoing(true);
            mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
            nm.notify(0, mBuilder.build());
        } else {
            Notification notification = new Notification(R.drawable.ic_launcher, "M16채팅이 실행중 입니다...", System.currentTimeMillis());
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE ;
            notification.number = 0;
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, chat_main.class), PendingIntent.FLAG_UPDATE_CURRENT);
            notification.setLatestEventInfo(this, "M16채팅이 실행중 입니다...", "", pendingIntent);
            nm.notify(0, notification);
        }
    }

    private void ScrollDown() {
        ChatViewpager.CHAT_SCROLL.post(new Runnable() {
            @Override
            public void run() {
                ChatViewpager.CHAT_SCROLL.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    private void ChatInit() {
        try {
            String FILE_DIR = BinaryUtil.getInstance().GetBinaryPath(this);
            Log.i(TAG, "ChatInit()");
            Log.i(TAG, FILE_DIR);
            process = Runtime.getRuntime().exec(FILE_DIR+" -a --client=W3XP  m16-chat.ggu.la");
            // 입력 스트림
            OutputStream outputStream = process.getOutputStream();
            chatOutputStream = new ChatOutputStream(outputStream);
            // 기본 스트림 출력.
            InputStream inputStream = process.getInputStream();
            chatInputStream = new ChatInputStream(inputStream);
            // 에러 스트림 출력.
            InputStream errorinputStream = process.getErrorStream();
            errorStream = new ErrorStream(errorinputStream);
            // 스레드 실행.
            chatOutputStream.start();
            chatInputStream.start();
            errorStream.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(position == 1) {
            ChatViewpager.CHAT_FRIEND_LIST.clear();
            ChatViewpager.CHAT_FRIEND_LISTVIEW.invalidateViews();
            GetFrientList();
        } else if(position == 2) {
            ChatViewpager.CHAT_CLAN_LIST.clear();
            ChatViewpager.CHAT_CLAN_LISTVIEW.invalidateViews();
            GetClanList();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
                Toast.makeText(chat_main.this, "연결이 끊겼습니다.", Toast.LENGTH_SHORT).show();
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
                chatoutputStream.write((PreferencesControl.getInstance(chat_main.this).getValue(PreferencesControl.USER_DATA_PREF, PreferencesControl.USER_NAME, null) + "\n").getBytes());
                chatoutputStream.write((PreferencesControl.getInstance(chat_main.this).getValue(PreferencesControl.USER_DATA_PREF, PreferencesControl.USER_PWD, null) + "\n").getBytes());
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
            Message msg = new Message();
            try {
                while ((temp = in.readLine()) != null) {
                    Log.i(TAG, temp);
                    // Login Fail Check
                    if(temp.equals("Login incorrect.")) {
                        PreferencesControl.getInstance(chat_main.this).clearAll(PreferencesControl.USER_DATA_PREF);
                        overridePendingTransition(R.anim.fade, R.anim.fade);
                        finish();
                        startActivity(new Intent(chat_main.this, chat_login.class));
                        msg.obj = "로그인에 실패하였습니다.";
                        CHAT_TOAST_HANDLER.sendMessage(msg);
                        InterrupThread();
                    } else if(temp.equals("server closed connection")) {
                        msg.obj = "서버와의 연결이 끊겼습니다.";
                        finish();
                        CHAT_TOAST_HANDLER.sendMessage(msg);
                        InterrupThread();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void ViewInit() {
//        CHAT_TEXTVIEW = (AnsiTextView)findViewById(R.id.CHAT_VIEW);
        CHAT_BUTTON = (Button)findViewById(R.id.CHAT_BUTTON);
        CHAT_EDITTEXT = (EditText)findViewById(R.id.CHAT_EDITTEXT);
        CHAT_BUTTON.setOnClickListener(this);
        CHAT_EDITTEXT.setOnClickListener(this);
//        CHAT_SCROLL = (ScrollView)findViewById(R.id.CHAT_SCROLL);
        CHAT_SPINNER = (Spinner)findViewById(R.id.CHAT_SPINNER);
        CHAT_SPINNER.setAdapter(new SpinnerArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.CHAT_SPINNER_ITEM)));

        // Tab, pager Initiallize
        pagerSlidingTabStrip = (PagerSlidingTabStrip)findViewById(R.id.CHAT_TABS);
        viewPager = (ViewPager)findViewById(R.id.CHAT_VIEWPAGER);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        pagerSlidingTabStrip.setViewPager(viewPager);
        pagerSlidingTabStrip.setOnPageChangeListener(this);

    }

    private void GetClanList() {
        chatOutputStream.sendMessage("/c apilist");
    }

    private void GetFrientList() {
        chatOutputStream.sendMessage("/f apilist");
    }

    private void InterrupThread() {
        chatOutputStream.interrupt();
        chatInputStream.interrupt();
        errorStream.interrupt();
        process.destroy();
    }

    private void HandlerInit() {
        CHAT_TOAST_HANDLER = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Toast.makeText(chat_main.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
            }
        };

        CHAT_TV_HANDLER = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (!SHOW_FRIEND_LIST && !SHOW_CLAN_LIST) {
                    // Not Showing during Login Work
                    assert ((String) msg.obj) != null;
                    if (((String) msg.obj).contains("Joining")) {
                        SHOW_CHAT_START = true;
                    } else if (((String) msg.obj).contains("{@startfriend}")) {
                        SHOW_CHAT_START = false;
                        SHOW_FRIEND_LIST = true;
                    } else if (((String) msg.obj).contains("{@startclan}")) {
                        SHOW_CHAT_START = false;
                        SHOW_CLAN_LIST = true;
                    }
                    if (SHOW_CHAT_START) {
                        ChatViewpager.CHAT_TEXTVIEW.append((String) msg.obj);
                        ChatViewpager.CHAT_TEXTVIEW.append("\n");
                        ScrollDown();
                    }
                } else {
                    if (SHOW_FRIEND_LIST) {
                        //친구 정보 json 파싱.
                        try {
                            JSONParser parser = new JSONParser();
                            assert ((String) msg.obj) != null;
                            String json = ((String) msg.obj).substring(9, ((String) msg.obj).length());
                            if (((String) msg.obj).contains("{@end}")) {
                                SHOW_FRIEND_LIST = false;
                                SHOW_CHAT_START = true;
                                return;
                            }
                            JSONObject obj = (JSONObject) parser.parse(json);
                            ChatViewpager.CHAT_FRIEND_LIST.add(obj.get("id").toString());
                            ChatViewpager.CHAT_FRIEND_LISTVIEW.invalidateViews();
                            Log.d("FParser", "no : " + (String) obj.get("no"));
                            Log.d("FParser", "id : " + (String) obj.get("id"));
                            Log.d("FParser", "channel : " + (String) obj.get("channel"));
                            Log.d("FParser", "game_name : " + (String) obj.get("game_name"));
                            Log.d("FParser", "time : " + (String) obj.get("time"));
                        } catch (ParseException e) {
                            SHOW_FRIEND_LIST = false;
                            SHOW_CHAT_START = true;
                            e.printStackTrace();
                        }
                    } else if (SHOW_CLAN_LIST) {
                        //클랜 정보 json 파싱.
                        try {
                            JSONParser parser = new JSONParser();
                            assert ((String) msg.obj) != null;
                            String json = ((String) msg.obj).substring(9, ((String) msg.obj).length());
                            if (((String) msg.obj).contains("{@end}")) {
                                SHOW_CLAN_LIST = false;
                                SHOW_CHAT_START = true;
                                return;
                            }
                            JSONObject obj = (JSONObject) parser.parse(json);
                            ChatViewpager.CHAT_CLAN_LIST.add(obj.get("id").toString());
                            ChatViewpager.CHAT_CLAN_LISTVIEW.invalidateViews();
                            Log.d("FParser", "no : "+ (String) obj.get("no"));
                            Log.d("FParser", "id : "+ (String) obj.get("id"));
                            Log.d("FParser", "level : "+ (String) obj.get("level"));
                            Log.d("FParser", "status : "+ (String) obj.get("status"));
                            Log.d("FPatser", "game_name : "+ (String) obj.get("game_name"));
                        } catch (ParseException e) {
                            SHOW_FRIEND_LIST = false;
                            SHOW_CHAT_START = true;
                            e.printStackTrace();
                        }
                    }

                }
            }
        };
    }
}
