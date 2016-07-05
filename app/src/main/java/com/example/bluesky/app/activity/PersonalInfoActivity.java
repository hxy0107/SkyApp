package com.example.bluesky.app.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bluesky.app.R;
import com.example.bluesky.app.ShoppingApplication;
import com.example.bluesky.app.event.NotifyExitEvent;
import com.example.bluesky.app.utils.PerferenceKeyUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class PersonalInfoActivity extends AppCompatActivity {

    @BindView(R.id.btn_exit)
    Button btn_exit;
    @BindView(R.id.loginInfo_toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_info_head)
    SimpleDraweeView img_head;
    @BindView(R.id.tv_info_name)
    TextView tv_name;
    @BindView(R.id.tv_info_gender)
    TextView tv_gender;
    @BindView(R.id.tv_info_hometown)
    TextView tv_hometown;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_personal_info);
        ButterKnife.bind(this);
        //注册事件
        EventBus.getDefault().register(this);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setTitle("个人信息");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        sharedPreferences = ShoppingApplication.sp;
        String name = sharedPreferences.getString(PerferenceKeyUtils.USERNAME, "");
        String img = sharedPreferences.getString(PerferenceKeyUtils.USER_HEAD_IMG, "");
        String gender = sharedPreferences.getString(PerferenceKeyUtils.GENDER, "");
        String hometown = sharedPreferences.getString(PerferenceKeyUtils.HOMETOWN, "");
        tv_name.setText(name);
        tv_gender.setText(gender);
        tv_hometown.setText(hometown);
        img_head.setImageURI(img);
    }

    @OnClick(R.id.btn_exit)
    public void exit(View view) {
        //退出登录
        String plarform = sharedPreferences.getString(PerferenceKeyUtils.PLATFORM, "");
        SHARE_MEDIA share_media = SHARE_MEDIA.convertToEmun(plarform);
        //通知事件
        EventBus.getDefault().post(new NotifyExitEvent(share_media));


    }

    @Subscribe(threadMode = ThreadMode.PostThread)
    public void onEvent(NotifyExitEvent notifyExitEvent) {
        UMShareAPI.get(this).deleteOauth(this, notifyExitEvent.getShare_media(), new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {


            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {

            }
        });
        //清除所有保存登录的信息
        ShoppingApplication.sp.edit()
                .putString(PerferenceKeyUtils.PLATFORM, "")
                .putString(PerferenceKeyUtils.USERNAME, "")
                .putString(PerferenceKeyUtils.USER_HEAD_IMG, "")
                .putString(PerferenceKeyUtils.GENDER, "")
                .putString(PerferenceKeyUtils.HOMETOWN, "")
                .putBoolean(PerferenceKeyUtils.ISLOGIN, false).commit();

        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除注册监听
        EventBus.getDefault().unregister(this);
    }
}
