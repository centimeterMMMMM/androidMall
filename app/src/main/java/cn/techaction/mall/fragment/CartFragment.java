package cn.techaction.mall.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.content.LocalBroadcastManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.techaction.mall.R;
import cn.techaction.mall.adapter.CartAdapter;
import cn.techaction.mall.config.Constant;
import cn.techaction.mall.listener.OnItemClickListener;
import cn.techaction.mall.pojo.Cart;
import cn.techaction.mall.pojo.CartItem;
import cn.techaction.mall.pojo.ResponeCode;
import cn.techaction.mall.pojo.SverResponse;
import cn.techaction.mall.ui.ConfirmOrderActivity;
import cn.techaction.mall.ui.DetailActivity;
import cn.techaction.mall.ui.LoginActivity;
import cn.techaction.mall.utils.JSONUtils;
import okhttp3.Call;


public class CartFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<CartItem> mData;
    private CartAdapter cartAdapter;

    private TextView total;
    private TextView btn_buy;

    private  boolean isEdit =false;


    //本地广播
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;
    private BroadcastReceiver broadcastReceiver;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_cart, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //注册广播
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION.LOAD_CART_ACTION);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //加载购物车数据
                loadCartData();
            }
        };
        localBroadcastManager.registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            loadCartData();
        }
    }

    public void initView(View view){
        recyclerView = (RecyclerView)view.findViewById(R.id.cart_rv);
        total = (TextView)view.findViewById(R.id.total);
        btn_buy = (TextView)view.findViewById(R.id.buy_btn);

        mData = new ArrayList<>();
        cartAdapter = new CartAdapter(getActivity(),mData);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(cartAdapter);
        cartAdapter.setOnCartOptListener(new CartAdapter.OnCartOptListener() {
            @Override
            public void updateProductCount(int productId, int count) {
                updateProduct(productId,count);
            }

            @Override
            public void delProductFromCart(int productId) {
                delProductById(productId);
            }
        });

        view.findViewById(R.id.edit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEdit){
                    isEdit =false;
                    for(CartItem item : mData){
                        item.setEdit(true);
                    }
                }else {
                    isEdit = true;
                    for(CartItem item : mData){
                        item.setEdit(false);
                    }
                }

                cartAdapter.notifyDataSetChanged();
            }
        });
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到确定订单页面
                Intent intent=new Intent(getActivity(), ConfirmOrderActivity.class);
                startActivity(intent);

            }
        });
        cartAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                //跳转到详情页面
                String id=mData.get(pos).getId()+"";
                Intent intent=new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
    }


    //加载购物车数据
    private void loadCartData(){
        OkHttpUtils.get()
                .url(Constant.API.CART_LIST_URL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Type type = new TypeToken<SverResponse<Cart>>(){}.getType();
                        SverResponse<Cart> result = JSONUtils.fromJson(response,type);
                        if(result.getStatus()== ResponeCode.SUCESS.getCode()){
                            if(result.getData().getLists()!=null){
                                mData.clear();
                                mData.addAll(result.getData().getLists());
                                cartAdapter.notifyDataSetChanged();
                            }
                            total.setText("合计：￥"+result.getData().getTotalPrice());
                        }else {
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }
    private void updateProduct(int productId,int count){
        OkHttpUtils.get()
                .url(Constant.API.CART_UPDATE_URL)
                .addParams("productId",productId+"")
                .addParams("count",count+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Type type = new TypeToken<SverResponse<Cart>>(){}.getType();
                        SverResponse<Cart> result = JSONUtils.fromJson(response,type);
                        if(result.getStatus()== ResponeCode.SUCESS.getCode()){
                            if(result.getData().getLists()!=null){
                                mData.clear();
                                mData.addAll(result.getData().getLists());
                                cartAdapter.notifyDataSetChanged();
                            }
                            total.setText("合计：￥"+result.getData().getTotalPrice());
                        }
                    }
                });
    }
    //删除商品
    private void delProductById(int productId){
        OkHttpUtils.get()
                .url(Constant.API.CART_DEL_URL)
                .addParams("productId",productId+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Type type = new TypeToken<SverResponse<Cart>>(){}.getType();
                        SverResponse<Cart> result = JSONUtils.fromJson(response,type);
                        if(result.getStatus()== ResponeCode.SUCESS.getCode()){
                            if(result.getData().getLists()!=null){
                                mData.clear();
                                mData.addAll(result.getData().getLists());
                                cartAdapter.notifyDataSetChanged();
                            }
                            total.setText("合计：￥"+result.getData().getTotalPrice());
                        }
                    }
                });
    }

}
