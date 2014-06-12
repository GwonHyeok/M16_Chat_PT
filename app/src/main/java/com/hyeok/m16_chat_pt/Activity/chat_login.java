package com.hyeok.m16_chat_pt.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hyeok.m16_chat_pt.Utils.BinaryUtil;
import com.hyeok.m16_chat_pt.Utils.PreferencesControl;
import com.hyeok.m16_chat_pt.app.R;

/**
 * Created by GwonHyeok on 2014. 6. 12..
 */
public class chat_login extends ActionBarActivity implements View.OnClickListener {
    private LinearLayout loginbox_layout;
    private ImageView login_logo_image;
    private Button login_button;
    private EditText login_uname_edittext, login_pwd_edittext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_login);
        viewInit(); // View Init
        BinaryUtil.getInstance().CheckBinaryFile(this); // Check Binary File
        Handler handler_animation = new Handler();
        handler_animation.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(chat_login.this, R.anim.translate);
                assert animation != null;
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // IF Already Login
                        if(PreferencesControl.getInstance(chat_login.this).getValue(PreferencesControl.USER_DATA_PREF, PreferencesControl.USER_NAME, null) != null) {
                            finish();
                            startActivity(new Intent(chat_login.this, chat_main.class));
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // IF Not Login = Show login Box
                        Animation animation1 = AnimationUtils.loadAnimation(chat_login.this, R.anim.fade);
                        loginbox_layout.setVisibility(View.VISIBLE);
                        loginbox_layout.startAnimation(animation1);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                login_logo_image.startAnimation(animation);
            }
        }, 1000);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == login_button.getId()) {
            String uname = login_uname_edittext.getText().toString();
            String pwd = login_pwd_edittext.getText().toString();
            if(uname.isEmpty() || pwd.isEmpty()) {
                Toast.makeText(chat_login.this, "아이디나 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            } else {
                PreferencesControl.getInstance(chat_login.this).put(PreferencesControl.USER_DATA_PREF, PreferencesControl.USER_NAME, uname);
                PreferencesControl.getInstance(chat_login.this).put(PreferencesControl.USER_DATA_PREF, PreferencesControl.USER_PWD, pwd);
                finish();
                startActivity(new Intent(chat_login.this, chat_main.class));
            }
        }
    }

    private void viewInit() {
        login_logo_image = (ImageView) findViewById(R.id.image_login);
        loginbox_layout = (LinearLayout) findViewById(R.id.linear_loginbox);
        login_button = (Button) findViewById(R.id.button_login);
        login_uname_edittext = (EditText) findViewById(R.id.login_username);
        login_pwd_edittext = (EditText) findViewById(R.id.login_password);
        login_button.setOnClickListener(this);
    }
}