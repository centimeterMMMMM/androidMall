package cn.techaction.mall.adapter;

import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.techaction.mall.R;
import cn.techaction.mall.config.Constant;
import cn.techaction.mall.pojo.CartItem;

public class ConfirmOrderProductAdapter extends RecyclerView.Adapter<ConfirmOrderProductAdapter.ConfirmOrderViewHolder> {
    private Context context;
    private List<CartItem> mData;

    public ConfirmOrderProductAdapter(Context context, List<CartItem> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override

    public ConfirmOrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View  view= LayoutInflater.from(context).inflate(R.layout.confirm_order_list_item,null,false);
        return new ConfirmOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfirmOrderViewHolder Holder, int i) {
                       CartItem item=mData.get(i);
                       Holder.name.setText(item.getName());
                       Holder.price.setText(item.getPrice()+"");
                       Holder.num.setText(item.getQuantity()+"");
                       Glide.with(context).load(Constant.API.BASE_URL+item.getIconUrl()).into(Holder.icon_url);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ConfirmOrderViewHolder extends RecyclerView.ViewHolder
         {
             public View itemView;
             public ImageView icon_url;
             public TextView name;
             public TextView price;
             public TextView num;
             public ConfirmOrderViewHolder(@NonNull View itemView) {
                 super(itemView);
                 this.itemView=itemView;
                 icon_url=itemView.findViewById(R.id.icon_url);
                 name=itemView.findViewById(R.id.name);
                 price=itemView.findViewById(R.id.price);
                 num=itemView.findViewById(R.id.num);
             }
         }
}
