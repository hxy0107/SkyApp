package com.example.bluesky.app.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bluesky.app.R;
import com.example.bluesky.app.ShoppingApplication;
import com.example.bluesky.app.activity.PayActivity;
import com.example.bluesky.app.adapter.ShoppingCartAdapter;
import com.example.bluesky.app.bean.ShoppingCartItem;
import com.example.bluesky.app.event.NotifyDeleteShoppingCartEvent;

import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingCartFragment extends Fragment {

    @BindView(R.id.shoppingCart_listView)
    ListView listView;
    ShoppingCartAdapter shoppingCartAdapter;

    List<ShoppingCartItem> mShoppingCartItems = new ArrayList<>();

    @BindView(R.id.cb_all)
    CheckBox cb_all;
    @BindView(R.id.btn_total)
    Button btn_total;
    @BindView(R.id.tv_total_money)
    TextView tv_total_money;

    public ShoppingCartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @OnClick(R.id.btn_total)
    public void btnBuy(View view) {
        Intent intent = new Intent(getActivity(), PayActivity.class);
        intent.putExtra("total_money", tv_total_money.getText().toString());
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shopping_cart, null);
        ButterKnife.bind(this, view);
        shoppingCartAdapter = new ShoppingCartAdapter(getActivity());
        listView.setAdapter(shoppingCartAdapter);
        shoppingCartAdapter.setOnAscNumListener(
                new ShoppingCartAdapter.onAscNumListener() {
                    @Override
                    public void onAscNumListener(int pos) {
                        ShoppingCartItem item = shoppingCartAdapter.getItem(pos);
                        int num1 = item.getNum();
                        num1++;
                        KeyValue keyValue = new KeyValue("num", num1);
                        try {
                            ShoppingApplication.dbManager.update(ShoppingCartItem.class,
                                    WhereBuilder.b("sourceId", "=", item.getSourceId()),
                                    new KeyValue[]{keyValue});
                        } catch (Exception e) {
                            e.getMessage();
                        }
                        QueryList();
                        ComputeTotalMoney();
                    }
                });
        shoppingCartAdapter.setOnDescNumListener(new ShoppingCartAdapter.onDescNumListener() {
            @Override
            public void onDescNumListener(int pos) {
                ShoppingCartItem item = shoppingCartAdapter.getItem(pos);
                int num2 = item.getNum();
                if (num2 > 0) {
                    num2--;
                } else {
                    num2 = 0;
                }
                KeyValue keyValue = new KeyValue("num", num2);
                try {
                    ShoppingApplication.dbManager.update(ShoppingCartItem.class,
                            WhereBuilder.b("sourceId", "=", item.getSourceId()),
                            new KeyValue[]{keyValue});
                } catch (Exception e) {
                    e.getMessage();
                }
                QueryList();
                ComputeTotalMoney();
            }
        });
        shoppingCartAdapter.setmOnSelectItemListener(new ShoppingCartAdapter.onSelectItemListener() {
            @Override
            public void onSelectItemListener(int pos, boolean isChecked) {
                if (isChecked) {
                    shoppingCartAdapter.getmChecked().put(pos, isChecked);
                    if (isAllValuesChecked()) {
                        cb_all.setChecked(isChecked);
                    } else {
                        cb_all.setChecked(false);
                    }

                } else {
                    shoppingCartAdapter.getmChecked().delete(pos);
                    cb_all.setChecked(isChecked);
                }

                ComputeTotalMoney();

            }
        });
        cb_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < shoppingCartAdapter.getCount(); i++) {
                    shoppingCartAdapter.getmChecked().put(i, cb_all.isChecked());
                }
                QueryList();
                ComputeTotalMoney();
            }
        });
        QueryList();
        return view;
    }

    /*
     * Find if all values are checked.
     */
    protected boolean isAllValuesChecked() {
        for (int i = 0; i < shoppingCartAdapter.getCount(); i++) {
            if (!shoppingCartAdapter.getmChecked().get(i)) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.PostThread)
    public void onMainThread(NotifyDeleteShoppingCartEvent event) {
        List<ShoppingCartItem> shoppingCartItems = new ArrayList<>();
        List<ShoppingCartItem> deleteItems = new ArrayList<>();
        for (int i = 0; i < shoppingCartAdapter.getCount(); i++) {
            boolean flag = shoppingCartAdapter.getmChecked().get(i);
            if (flag == false) {

            } else {
                shoppingCartAdapter.getmChecked().delete(i);
                ShoppingCartItem item2 = shoppingCartAdapter.getItem(i);
                deleteItems.add(item2);
            }
        }
        try {
            ShoppingApplication.dbManager.delete(deleteItems);
        } catch (DbException e) {
            e.printStackTrace();
        }
        QueryList();
    }

    public void QueryList() {
        try {
            List<ShoppingCartItem> shoppingCartItems = ShoppingApplication.dbManager.findAll(ShoppingCartItem.class);
            shoppingCartAdapter.setCartItemList(shoppingCartItems);
            shoppingCartAdapter.notifyDataSetChanged();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void ComputeTotalMoney() {
        float sum = 0;
        for (int i = 0; i < shoppingCartAdapter.getCount(); i++) {
            boolean flag = shoppingCartAdapter.getmChecked().get(i);
            if (flag == true) {
                int num = shoppingCartAdapter.getItem(i).getNum();
                float price = Float.parseFloat(shoppingCartAdapter.getItem(i).getPrice());
                sum += num * price;
            }
        }
        tv_total_money.setText("合计:" + String.valueOf(sum) + "元");
    }

}
