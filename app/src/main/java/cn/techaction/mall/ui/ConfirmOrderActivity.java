package cn.techaction.mall.ui;

import android.content.Intent;
import android.os.Bundle;
//import android.support.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.techaction.mall.R;
import cn.techaction.mall.adapter.ConfirmOrderProductAdapter;
import cn.techaction.mall.config.Constant;
import cn.techaction.mall.pojo.Cart;
import cn.techaction.mall.pojo.CartItem;
import cn.techaction.mall.pojo.Order;
import cn.techaction.mall.pojo.ResponeCode;
import cn.techaction.mall.pojo.SverResponse;
import cn.techaction.mall.pojo.Address;
import cn.techaction.mall.utils.JSONUtils;
import okhttp3.Call;

public class ConfirmOrderActivity extends AppCompatActivity {
   private TextView name;
   private TextView mobile;
   private TextView addr_detail;
   private TextView total;
   private RecyclerView recyclerView;
   private ConfirmOrderProductAdapter confirmOrderProductAdapter;
   private List<CartItem> cartItems;
   private Address defaultAddr;

   private static final int REQ_ADDR_CODE=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
       initView();
       initDefaultAddr();
       initCartProducts();
    }
   private void initView()
   {
      name=findViewById(R.id.name);
      mobile=findViewById(R.id.mobile);
      addr_detail=findViewById(R.id.addr_detail);
      recyclerView=findViewById(R.id.cart_rv);
      total=findViewById(R.id.total);
      Toolbar toolbar=findViewById(R.id.toolbar);
       toolbar.setTitle("确认订单信息");
       setSupportActionBar(toolbar);
       toolbar.setNavigationOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finish();
           }
       });
      cartItems=new ArrayList<>();
      confirmOrderProductAdapter=new ConfirmOrderProductAdapter(this,cartItems);
       LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
       recyclerView.setLayoutManager(linearLayoutManager);
       recyclerView.setAdapter(confirmOrderProductAdapter);

       //提交订单
      findViewById(R.id.buy_btn).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
                submitOrder();
          }
      });
      //选择地址
      findViewById(R.id.address_container).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent=new Intent(ConfirmOrderActivity.this,AddressListActivity.class);
              startActivityForResult(intent,REQ_ADDR_CODE);
          }
      });
   }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==REQ_ADDR_CODE){
            if (resultCode==RESULT_OK){
                defaultAddr=(Address)data.getSerializableExtra("address");
                displayInfo();
            }
        }
    }

    /*显示地址信息*/
    private void displayInfo(){
        name.setText(defaultAddr.getName());
        mobile.setText(defaultAddr.getMobile());
        addr_detail.setText(
                defaultAddr.getProvince()+" "
                        +defaultAddr.getCity() +" "
                        +defaultAddr.getDistrict()+" "
                        +defaultAddr.getAddr()
        );
    }


    /*加载默认地址*/
   private void initDefaultAddr(){
       OkHttpUtils.get().url(Constant.API.USER_ADDR_LIST_URL)
               .build().execute(new StringCallback() {
           @Override
           public void onError(Call call, Exception e, int id) {

           }

           @Override
           public void onResponse(String response, int id) {
                  Type type=new TypeToken<SverResponse<List <Address>>>(){}.getType();
                  SverResponse<List<Address>> result=new JSONUtils().fromJson(response,type);
                  if(result.getStatus()== ResponeCode.SUCESS.getCode())
                  {
                      if(result.getData()!=null)
                      {
                         for(Address adr: result.getData() )
                         {
                            defaultAddr=adr;
                            break;
                         }
                      }
                      if(defaultAddr==null)
                      {
                          defaultAddr=result.getData().get(0);
                      }
                      displayInfo();
                  }
                  else {
                      name.setText(defaultAddr.getName());
                      addr_detail.setText("");
                      mobile.setText("请选择收件地址");
                  }
            }
       });

   }

   /*加载购物车数据*/
   private void initCartProducts()
   {
       OkHttpUtils.get().url(Constant.API.CART_LIST_URL).build().execute(new StringCallback() {
           @Override
           public void onError(Call call, Exception e, int id) {

           }

           @Override
           public void onResponse(String response, int id) {
                      Type type=new TypeToken<SverResponse<Cart>>(){}.getType();
                      SverResponse<Cart> result=new JSONUtils().fromJson(response,type);
                      if(result.getStatus()==ResponeCode.SUCESS.getCode())
                      {
                          if(result.getData().getLists()!=null)
                          {
                              cartItems.clear();
                              cartItems.addAll(result.getData().getLists());
                              confirmOrderProductAdapter.notifyDataSetChanged();
                          }
                          total.setText("总计"+result.getData().getTotalPrice());
                      }
           }
       });
   }
   private void submitOrder(){
        if(defaultAddr==null)
        {
            Toast.makeText(this, "请选择收货地址", Toast.LENGTH_SHORT).show();
            return ;
        }
        OkHttpUtils.get()
                .url(Constant.API.ORDER_CREATED_URL)
                .addParams("addrId",defaultAddr.getId()+"")
                .build()
                .execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                     Type type=new TypeToken<SverResponse<Order>>(){}.getType();
                     SverResponse<Order> result=JSONUtils.fromJson(response,type);
                     if(result.getStatus()==ResponeCode.SUCESS.getCode())
                     {
                         //跳转到订单详情
                     }
                     else
                     {
                         Toast.makeText(ConfirmOrderActivity.this,result.getStatus(),Toast.LENGTH_LONG).show();
                     }

            }
        });
   }
}
