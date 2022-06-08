package cn.techaction.mall.adapter;

import android.content.Context;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;

import java.util.List;

import cn.techaction.mall.R;
import cn.techaction.mall.pojo.Param;



public class HomeTopBannerAndParamAdapter extends DelegateAdapter.Adapter<HomeTopBannerAndParamAdapter.BannerAndParamViewHolder> {
    public static final int TYPE_HEADER=0;
    public static final int TYPE_NORMAL=1;

    private View mHeadView ;   //头部Banner
    private List<Param> mData;
    private Context context;
    private LayoutHelper layoutHelper;

    /**
     * 构造函数
     * @param mData
     * @param context
     * @param layoutHelper
     */
    public HomeTopBannerAndParamAdapter(List<Param> mData, Context context, LayoutHelper layoutHelper) {
        this.mData = mData;
        this.context = context;
        this.layoutHelper = layoutHelper;
    }

    public void setHeadView(View headView){
        this.mHeadView = headView;
    }
    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return this.layoutHelper;
    }

    /**
     * 根据位置返回不同的视图
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if(this.mHeadView==null)
            return TYPE_NORMAL;
        if(position==0){
            return TYPE_HEADER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public BannerAndParamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeadView!=null && viewType==TYPE_HEADER)
            return new BannerAndParamViewHolder(mHeadView);
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_home_params_list_item,null,false);
        return new BannerAndParamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BannerAndParamViewHolder holder, int position) {
        if(getItemViewType(position)==TYPE_HEADER){
            return;
        }
        final  int pos =getRealPosition(holder);
        holder.tv.setText(mData.get(pos).getName());
    }

    /**
     * 读取数据的位置
     * @param holder
     * @return
     */
    private int getRealPosition(RecyclerView.ViewHolder holder){
        int pos = holder.getLayoutPosition();
        return mHeadView==null?pos:pos-1;
    }

    @Override
    public int getItemCount() {
        return mHeadView==null?mData.size():mData.size()+1;
    }


    public static class BannerAndParamViewHolder extends RecyclerView.ViewHolder{
        public TextView tv;
        public BannerAndParamViewHolder(View itemView) {
            super(itemView);
            tv = (TextView)itemView.findViewById(R.id.item_tv);
        }
    }
}
