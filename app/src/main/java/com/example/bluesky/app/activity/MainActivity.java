package com.example.bluesky.app.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluesky.app.R;
import com.example.bluesky.app.ShoppingApplication;
import com.example.bluesky.app.bean.ShoppingCartItem;
import com.example.bluesky.app.event.NotifyDeleteShoppingCartEvent;
import com.example.bluesky.app.event.NotifyExitEvent;
import com.example.bluesky.app.event.NotifyLoginEvent;
import com.example.bluesky.app.fragment.ContentFragment;
import com.example.bluesky.app.fragment.ShoppingCartFragment;
import com.example.bluesky.app.fragment.StorageFragment;
import com.example.bluesky.app.utils.PerferenceKeyUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 只能QQ,腾讯微博登录
 */
public class MainActivity extends AppCompatActivity {

    String url = "http://api-v2.mall.hichao.com/category/list?ga=%2Fcategory%2Flist";

    @BindView(R.id.drawerlayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigationView)
    NavigationView navigationView;
    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    LinearLayout linearLayout;
    SimpleDraweeView img_head;
    TextView tv_name;

    List<ShoppingCartItem> shoppingCartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        toolbar.setTitle("首页");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        navigationView.setCheckedItem(R.id.navigation_home);
        final ActionBarDrawerToggle toggle
                = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        //内容面板
        getSupportFragmentManager().beginTransaction().replace(R.id.layout, new ContentFragment()).commit();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()) {
                    case android.R.id.home:
                        toolbar.setTitle("首页");
                        drawerLayout.openDrawer(Gravity.LEFT);
                        break;
                    case R.id.navigation_home:
                        toolbar.setTitle("首页");
                        MenuItem item1 = toolbar.getMenu().findItem(R.id.share);
                        item1.setIcon(android.R.drawable.ic_menu_share);
                        transaction.replace(R.id.layout, new ContentFragment());
                        break;
                    case R.id.storage:
                        toolbar.setTitle("收藏");
                        MenuItem item2 = toolbar.getMenu().findItem(R.id.share);
                        item2.setIcon(android.R.drawable.ic_menu_share);
                        transaction.replace(R.id.layout, new StorageFragment());
                        break;
                    case R.id.shopping_cart:
                        toolbar.setTitle("购物车");
                        MenuItem item3 = toolbar.getMenu().findItem(R.id.share);
                        item3.setIcon(android.R.drawable.ic_menu_delete);
                        transaction.replace(R.id.layout, new ShoppingCartFragment());
                        break;
                    case R.id.exit:
                        MenuItem item4 = toolbar.getMenu().findItem(R.id.share);
                        item4.setIcon(android.R.drawable.ic_menu_share);
                        boolean isLogin = ShoppingApplication.sp.getBoolean(PerferenceKeyUtils.ISLOGIN, false);
                        if (isLogin == true) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("是否确定退出登录？");
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            });
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String plarform = ShoppingApplication.sp.getString(PerferenceKeyUtils.PLATFORM, "");
                                    SHARE_MEDIA share_media = SHARE_MEDIA.convertToEmun(plarform);
                                    EventBus.getDefault().post(new NotifyExitEvent(share_media));
                                    //清除所有保存登录的信息
                                    ShoppingApplication.sp.edit()
                                            .putString(PerferenceKeyUtils.PLATFORM, "")
                                            .putString(PerferenceKeyUtils.USERNAME, "")
                                            .putString(PerferenceKeyUtils.USER_HEAD_IMG, "")
                                            .putString(PerferenceKeyUtils.GENDER, "")
                                            .putString(PerferenceKeyUtils.HOMETOWN, "")
                                            .putBoolean(PerferenceKeyUtils.ISLOGIN, false).commit();
                                    dialog.dismiss();
                                }
                            });
                            builder.create().show();
                        } else {
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            Toast.makeText(MainActivity.this, "还未登录", Toast.LENGTH_LONG).show();
                        }


                        break;
                }
                transaction.commit();
                return true;
            }
        });

        getNavigationViewHeadLayout();
    }

    /**
     * 获取navigationView的headLayout内容并设置
     */

    public void getNavigationViewHeadLayout() {
        View headView = navigationView.getHeaderView(0);
        linearLayout = (LinearLayout) headView.findViewById(R.id.layout_login1);
        img_head = (SimpleDraweeView) headView.findViewById(R.id.img_head);
        tv_name = (TextView) headView.findViewById(R.id.tv_name);
        isLogin(img_head, tv_name);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean isLogins = isLogin(img_head, tv_name);
                if (isLogins == true) {
                    startActivity(new Intent(MainActivity.this, PersonalInfoActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
    }

    public boolean isLogin(SimpleDraweeView img, TextView tv) {
        boolean isLogin = ShoppingApplication.sp.getBoolean(PerferenceKeyUtils.ISLOGIN, false);
        if (isLogin == true) {
            String name = ShoppingApplication.sp.getString(PerferenceKeyUtils.USERNAME, "");
            String img_head_url = ShoppingApplication.sp.getString(PerferenceKeyUtils.USER_HEAD_IMG, "");
            tv.setText(name);
            img.setImageURI(Uri.parse(img_head_url));
        } else {
            tv.setText("未登录");
            img.setImageResource(R.mipmap.ic_launcher);
        }
        return isLogin;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_share_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.share:
                if (toolbar.getTitle().equals("购物车")) {
                    //删除数据
                    EventBus.getDefault().post(new NotifyDeleteShoppingCartEvent());

                } else {
                    SHARE_MEDIA[] share_medias = new SHARE_MEDIA[]{
                            SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
                            SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT,
                            SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE
                    };
                    //开启分享编辑页
                    new ShareAction(MainActivity.this).setDisplayList(share_medias)
                            .withTitle("标题")
                            .withText("内容")
                            .withTargetUrl("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1466341405&di=f193bf8f4b62c2953783b34ea3607be2&src=http://img.hb.aicdn.com/f6e9967122f84c148b7e7d1d3909e6db4becb94563207-wvAgVy_fw580")
                            .setListenerList(new UMShareListener() {
                                @Override
                                public void onResult(SHARE_MEDIA share_media) {
                                    Log.e("====", "=成功分享===");
                                }

                                @Override
                                public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                    Log.e("====", "=分享错误===" + throwable.getMessage());
                                }

                                @Override
                                public void onCancel(SHARE_MEDIA share_media) {
                                    Log.e("====", "=取消分享===");
                                }
                            })
                            .open();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe(threadMode = ThreadMode.PostThread)
    public void onEventMainThread(NotifyExitEvent notifyExitEvent) {
        //退出登录的事件
        tv_name.setText("未登录");
        img_head.setImageResource(R.mipmap.ic_launcher);
    }

    @Subscribe(threadMode = ThreadMode.PostThread)
    public void onEventMainThread(NotifyLoginEvent notifyLoginEvent) {
        //登录成功的事件
        String name = ShoppingApplication.sp.getString(PerferenceKeyUtils.USERNAME, "");
        String img_head_url = ShoppingApplication.sp.getString(PerferenceKeyUtils.USER_HEAD_IMG, "");
        tv_name.setText(name);
        img_head.setImageURI(Uri.parse(img_head_url));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(MainActivity.this).onActivityResult(requestCode, resultCode, data);
    }
}
