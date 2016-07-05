package com.example.bluesky.app;

import android.app.Application;
import android.content.SharedPreferences;

import com.umeng.socialize.PlatformConfig;

import org.xutils.DbManager;
import org.xutils.x;

/**
 * Created by bluesky on 16/6/17.
 */
public class ShoppingApplication extends Application {
   public static DbManager dbManager;
    public static SharedPreferences sp;
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
        DbManager.DaoConfig config = new DbManager.DaoConfig().setDbName("sky.db")
                .setAllowTransaction(true).setDbVersion(1);
        dbManager = x.getDb(config);

        //第三方社会化初始化
        //微信
        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        //QQ空间
        PlatformConfig.setQQZone("1105481720", "8j5C3ROyQTRFEoup");
        //新浪微博
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");
        //腾讯微博
       // PlatformConfig.setTencentWB("1105481720","8j5C3ROyQTRFEoup");
        sp= getSharedPreferences("loginInfo",MODE_PRIVATE);
    }


}
