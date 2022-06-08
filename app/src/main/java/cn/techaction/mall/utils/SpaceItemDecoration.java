package cn.techaction.mall.utils;

import android.graphics.Rect;
//import android.support.v7.widget.RecyclerView;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;


public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int bottomSpce;
    private int outSpec;

    public SpaceItemDecoration(int bottomSpce, int outSpec) {
        this.bottomSpce = bottomSpce;
        this.outSpec = outSpec;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = bottomSpce;
        if(parent.getChildLayoutPosition(view)%2==0){
            outRect.right = outSpec;
        }else{
            outRect.left = outSpec;
        }
    }
}
