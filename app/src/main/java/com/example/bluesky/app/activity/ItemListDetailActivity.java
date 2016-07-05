package com.example.bluesky.app.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluesky.app.R;
import com.example.bluesky.app.ShoppingApplication;
import com.example.bluesky.app.bean.CategoryList;
import com.example.bluesky.app.bean.ShoppingCartItem;
import com.example.bluesky.app.bean.StorageItem;
import com.example.bluesky.app.constants.Constants;

import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ItemListDetailActivity extends AppCompatActivity {
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.btn_storage)
    Button btn_storage;
    @BindView(R.id.btn_add_shopping_cart)
    Button btn_add_shopping_cart;
    @BindView(R.id.btn_total)
    Button btn_buy;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_shopping_cart_num)
    TextView tv_shopping_cart_num1;
    CategoryList.DataBean.ItemsBean itemsBeans;
    String id;
    List<StorageItem> storageItems;
    StorageItem storageItem;
    String type;
    List<ShoppingCartItem> shoppingCartItems = null;

    private static boolean ISQUERYED = false;
    private static final int QUERY_YES = 0x100;
    private static final int QUERY_NO = 0x101;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == QUERY_YES) {
                int num = msg.arg1;
                String sourceId = (String) msg.obj;
                UpdatetData(num, sourceId);

            } else if (msg.what == QUERY_NO) {
                insertData();
            }
            queryShoppingCart();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list_detail);
        ButterKnife.bind(this);
        type = getIntent().getStringExtra("type");
        if (type.equals("0")) {
            //从首页跳转过来的
            id = getIntent().getStringExtra("id");
            itemsBeans = (CategoryList.DataBean.ItemsBean) getIntent().getSerializableExtra("itembean");
        } else if (type.equals("1")) {
            //收藏页面跳转过来的
            storageItem = (StorageItem) getIntent().getSerializableExtra("storageItem");
            id = storageItem.getSourceId();
            btn_storage.setVisibility(View.GONE);
        }
        toolbar.setTitle("详情");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        queryStorage();
        queryShoppingCart();
        webView.loadUrl(String.format(Constants.CATEGORY_DETAIL_BASE, id));
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public void queryStorage() {
        //查询收藏内容
        try {
            storageItems = ShoppingApplication.dbManager.findAll(StorageItem.class);
            if (storageItems != null && storageItems.size() > 0) {
                for (int i = 0; i < storageItems.size(); i++) {
                    if (storageItems.get(i).getSourceId().equals(id)) {
                        btn_storage.setEnabled(false);
                    } else {
                        btn_storage.setEnabled(true);
                    }
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void queryShoppingCart() {
        //查询购物车内容
        try {
            shoppingCartItems = ShoppingApplication.dbManager.findAll(ShoppingCartItem.class);
            if (shoppingCartItems != null && shoppingCartItems.size() > 0) {
                tv_shopping_cart_num1.setText(String.valueOf(shoppingCartItems.size()));
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_storage)
    public void storage(View view) {
        try {
            if (storageItems != null && storageItems.size() > 0) {
                for (int i = 0; i < storageItems.size(); i++) {
                    if (storageItems.get(i).getSourceId().equals(id)) {
                        btn_storage.setEnabled(false);
                        Toast.makeText(this, "已收藏", Toast.LENGTH_LONG).show();
                    } else {
                        StorageItem item = null;
                        if (type.equals("0")) {
                            item = new StorageItem(
                                    itemsBeans.getComponent().getPicUrl(),
                                    itemsBeans.getComponent().getPrice(),
                                    itemsBeans.getComponent().getOrigin_price(),
                                    itemsBeans.getComponent().getDescription(),
                                    itemsBeans.getComponent().getSales(),
                                    id);
                            ShoppingApplication.dbManager.save(item);
                        }
                        btn_storage.setEnabled(false);
                        Toast.makeText(this, "收藏成功", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                StorageItem item = null;
                if (type.equals("0")) {
                    item = new StorageItem(
                            itemsBeans.getComponent().getPicUrl(),
                            itemsBeans.getComponent().getPrice(),
                            itemsBeans.getComponent().getOrigin_price(),
                            itemsBeans.getComponent().getDescription(),
                            itemsBeans.getComponent().getSales(),
                            id);
                    ShoppingApplication.dbManager.save(item);
                }
                btn_storage.setEnabled(false);
                Toast.makeText(this, "收藏成功", Toast.LENGTH_LONG).show();
            }


        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_add_shopping_cart)
    public void addShoopingCart(View view) {
        queryData();
    }

    @OnClick(R.id.btn_total)
    public void buy(View view) {

    }

    public void queryData() {
        ISQUERYED = false;
        try {
            String ids = null;
            if (type.equals("0")) {
                ids = itemsBeans.getComponent().getAction().getSourceId();
            } else if (type.equals("1")) {
                ids = storageItem.getSourceId();
            }
            //查询数据中所有的数据
            List<ShoppingCartItem> shoppingCartItems = ShoppingApplication.dbManager.findAll(ShoppingCartItem.class);
            //数据库中有值的情况
            if (shoppingCartItems != null && shoppingCartItems.size() > 0) {
                for (int i = 0; i < shoppingCartItems.size(); i++) {
                    //取出其中的一项
                    ShoppingCartItem cartItem = shoppingCartItems.get(i);
                    //数据库中存在该条数据
                    if (cartItem.getSourceId().equals(ids)) {
                        ISQUERYED = true;
                        Message message = Message.obtain();
                        message.what = QUERY_YES;
                        message.arg1 = cartItem.getNum();
                        message.obj = cartItem.getSourceId();
                        handler.sendMessage(message);
                    }
                }
            }
            if (!ISQUERYED) {
                Message message = Message.obtain();
                message.what = QUERY_NO;
                handler.sendMessage(message);
            }
        } catch (Exception e) {
            e.getMessage();

        }

    }

    public void insertData() {
        //购物车数据为空的操作
        String picUrl = null, price = null, description = null, sourceId = null;
        if (type.equals("0")) {
            picUrl = itemsBeans.getComponent().getPicUrl();
            price = itemsBeans.getComponent().getPrice();
            description = itemsBeans.getComponent().getDescription();
            sourceId = itemsBeans.getComponent().getAction().getSourceId();
        } else if (type.equals("1")) {

            picUrl = storageItem.getPicUrl();
            price = storageItem.getPrice();
            description = storageItem.getDescription();
            sourceId = storageItem.getSourceId();
        }
        ShoppingCartItem cartItem = new ShoppingCartItem(
                description, price, picUrl, 1, sourceId);
        try {
            ShoppingApplication.dbManager.save(cartItem);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void UpdatetData(int num, String sourceId) {
        int  nums=num+1;
        KeyValue keyValue = new KeyValue("num", nums);
        try {
            ShoppingApplication.dbManager.update(ShoppingCartItem.class,
                    WhereBuilder.b("sourceId", "=", sourceId), new KeyValue[]{keyValue});
        } catch (Exception e) {
            e.getMessage();
        }
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

}
