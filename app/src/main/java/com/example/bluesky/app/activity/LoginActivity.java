package com.example.bluesky.app.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.bluesky.app.R;
import com.example.bluesky.app.ShoppingApplication;
import com.example.bluesky.app.event.NotifyLoginEvent;
import com.example.bluesky.app.utils.PerferenceKeyUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;


public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.login_toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_qq)
    ImageView img_QQ;
    @BindView(R.id.img_wechat)
    ImageView img_Wechat;
    @BindView(R.id.img_sina)
    ImageView img_Sina;
    @BindView(R.id.img_tencent)
    ImageView img_Tencent;

    UMShareAPI umShareAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        toolbar.setTitle("登录");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
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

    @OnClick(R.id.img_qq)
    public void LogindQQ(View view) {
        login(SHARE_MEDIA.QQ);
    }

    @OnClick(R.id.img_wechat)
    public void LogindWechat(View view) {
        login(SHARE_MEDIA.WEIXIN);
    }

    @OnClick(R.id.img_sina)
    public void LogindSina(View view) {
        login(SHARE_MEDIA.SINA);
    }

    @OnClick(R.id.img_tencent)
    public void LogindTencent(View view) {
        login(SHARE_MEDIA.TENCENT);
    }


    public void login(SHARE_MEDIA share_media) {
        umShareAPI = UMShareAPI.get(this);

        umShareAPI.doOauthVerify(this, share_media, new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                umShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, new UMAuthListener() {
                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        Log.e("=====", "==onCompletewwww===" + map.toString());
                        if (share_media.equals(SHARE_MEDIA.QQ)) {
                            String url = map.get("profile_image_url");
                            String name = map.get("screen_name");
                            String gender = map.get("gender");
                            String province = map.get("province");
                            String city = map.get("city");
                            ShoppingApplication.sp.edit()
                                    .putString(PerferenceKeyUtils.PLATFORM, share_media.toString())
                                    .putString(PerferenceKeyUtils.USERNAME, name)
                                    .putString(PerferenceKeyUtils.USER_HEAD_IMG, url)
                                    .putString(PerferenceKeyUtils.GENDER,gender)
                                    .putString(PerferenceKeyUtils.HOMETOWN,province+"-"+city)
                                    .putBoolean(PerferenceKeyUtils.ISLOGIN, true).commit();
                        }
                        EventBus.getDefault().post(new NotifyLoginEvent());
                        finish();
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {

                    }
                });
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Log.e("=====", "==onError===" + throwable.getMessage());
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                Log.e("=====", "==onCancel===");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        umShareAPI.onActivityResult(requestCode, resultCode, data);
    }
}
