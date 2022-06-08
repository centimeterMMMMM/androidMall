package cn.techaction.mall.adapter;

import android.content.Context;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.techaction.mall.R;
import cn.techaction.mall.listener.OnItemClickListener;
import cn.techaction.mall.pojo.Param;



public class CategoryLeftAdapter extends RecyclerView.Adapter<CategoryLeftAdapter.CategroyViewHolder>
        implements View.OnClickListener{
    private Context context;
    private List<Param> mData;
    private OnItemClickListener onItemClickListener;

    public CategoryLeftAdapter(Context context, List<Param> mData) {
        this.context = context;
        this.mData = mData;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public CategroyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_category_left_list_item,null,false);
        return new CategroyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(CategroyViewHolder holder, int position) {
        Param param = mData.get(position);
        holder.name.setText(param.getName());
        holder.name.setTag(position);
        if(param.isPressed()){
            holder.name.setBackgroundResource(R.color.font_color);
        }else{
            holder.name.setBackgroundResource(R.color.colorWhite);
        }
        holder.name.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int pos = (int)v.getTag();
        for(int i=0;i<mData.size();i++){
            if(i==pos){
                mData.get(i).setPressed(true);
            }else{
                mData.get(i).setPressed(false);
            }
        }
        notifyDataSetChanged();
        if(onItemClickListener!=null){
            onItemClickListener.onItemClick(v,pos);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static  class CategroyViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public CategroyViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.name);
        }
    }
}
