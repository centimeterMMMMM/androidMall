package cn.techaction.mall.adapter;

import android.content.Context;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.bumptech.glide.Glide;

import java.util.List;

import cn.techaction.mall.R;
import cn.techaction.mall.config.Constant;
import cn.techaction.mall.listener.OnItemClickListener;
import cn.techaction.mall.pojo.Product;



public class HomeHotProductAdapter extends DelegateAdapter.Adapter<HomeHotProductAdapter.HotProductViewHolder> {
    private List<Product> data;
    private Context context;
    private LayoutHelper layoutHelper;
    private OnItemClickListener onItemClickListener;
    public HomeHotProductAdapter(List<Product> data, Context context, LayoutHelper layoutHelper) {
        this.data = data;
        this.context = context;
        this.layoutHelper = layoutHelper;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return this.layoutHelper;
    }

    @Override
    public HotProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_home_hot_list_item,null,false);
        return new HotProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HotProductViewHolder holder, int position) {
        if(position==0){
            holder.titleContainer.setVisibility(View.VISIBLE);
        }else{
            holder.titleContainer.setVisibility(View.GONE);
        }
        Product product = data.get(position);
        holder.name.setText(product.getName());
        holder.price.setText("价格：￥"+product.getPrice());
        holder.stock.setText("库存："+product.getStock());
        holder.contentContainer.setTag(position);
        Glide.with(context).load(Constant.API.BASE_URL+product.getIconUrl()).into(holder.icon_url);
        holder.contentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener!=null)
                {
                    onItemClickListener.onItemClick(view,(int) view.getTag());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class HotProductViewHolder extends RecyclerView.ViewHolder{
        public RelativeLayout titleContainer;
        public RelativeLayout contentContainer;
        public TextView btn_more;
        public TextView name;
        public ImageView icon_url;
        public TextView stock;
        public TextView price;
        public HotProductViewHolder(View itemView) {
            super(itemView);
            titleContainer=(RelativeLayout)itemView.findViewById(R.id.title_container);
            contentContainer=(RelativeLayout)itemView.findViewById(R.id.content);
            name = (TextView)itemView.findViewById(R.id.name);
            stock=(TextView)itemView.findViewById(R.id.stock);
            price=(TextView)itemView.findViewById(R.id.price);
            icon_url=(ImageView)itemView.findViewById(R.id.icon_url);
            btn_more=(TextView)itemView.findViewById(R.id.btn_more);
        }
    }

}
