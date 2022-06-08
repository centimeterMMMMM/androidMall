package cn.techaction.mall.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;

import cn.techaction.mall.R;
import cn.techaction.mall.config.Constant;
import cn.techaction.mall.pojo.ResponeCode;
import cn.techaction.mall.pojo.SverResponse;
import cn.techaction.mall.pojo.User;
import cn.techaction.mall.ui.LoginActivity;
import cn.techaction.mall.utils.JSONUtils;
import okhttp3.Call;


public class UserFragment extends Fragment {
    private TextView user;

    //本地广播
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;
    private BroadcastReceiver broadcastReceiver;

    public UserFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_user, container, false);
        initView(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION.LOAD_CART_ACTION);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                initUserInfo();
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
            initUserInfo();
        }
    }

    //初始化界面组件
    private void initView(View view){
        user = (TextView)view.findViewById(R.id.user);
        view.findViewById(R.id.btn_addr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转
            }
        });
        view.findViewById(R.id.btn_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转
            }
        });
    }

    private void initUserInfo(){
        OkHttpUtils.get()
                .url(Constant.API.USER_INFO_URL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Type type = new TypeToken<SverResponse<User>>(){}.getType();
                        SverResponse<User> result = JSONUtils.fromJson(response,type);
                        if(result.getStatus()== ResponeCode.SUCESS.getCode()){
                            user.setText(result.getData().getAccount());
                        }else{
                            //跳转登录界面
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }
}
