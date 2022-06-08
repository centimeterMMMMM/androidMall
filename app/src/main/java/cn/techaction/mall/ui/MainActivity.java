package cn.techaction.mall.ui;

//import android.support.annotation.IdRes;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import cn.techaction.mall.R;
import cn.techaction.mall.fragment.CartFragment;
import cn.techaction.mall.fragment.CategoryFragment;
import cn.techaction.mall.fragment.HomeFragment;
import cn.techaction.mall.fragment.UserFragment;

public class MainActivity extends AppCompatActivity {

    private RadioGroup mRadioGroup;


    private Fragment homeFragment;
    private Fragment categoryFragment;
    private Fragment cartFragment;
    private Fragment userFragmeng;

    private RadioButton mRadioButtonHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFagment();
        bindEvent();
    }
    private void initFagment(){
        Log.i("tag","main");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        homeFragment = new HomeFragment();
        ft.add(R.id.container,homeFragment,"home");
        categoryFragment = new CategoryFragment();
        ft.add(R.id.container,categoryFragment,"category");
        cartFragment = new CartFragment();
        ft.add(R.id.container,cartFragment,"cart");
        userFragmeng=new UserFragment();
        ft.add(R.id.container,userFragmeng,"user");
        ft.show(homeFragment).hide(categoryFragment).hide(cartFragment).hide(userFragmeng).commit();
    }

    /**
     * 监听change事件
     */
    private void bindEvent(){
        //查找控件
        mRadioGroup = (RadioGroup)findViewById(R.id.radio_group_button);
        mRadioButtonHome = (RadioButton)findViewById(R.id.radio_button_home);
        //监听change事件，切换fragment
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                FragmentTransaction ft =  getSupportFragmentManager().beginTransaction();
                switch (checkedId){
                    case R.id.radio_button_home:
                        ft.show(homeFragment).hide(categoryFragment).hide(cartFragment).hide(userFragmeng).commit();
                        break;
                    case R.id.radio_button_category:
                        ft.show(categoryFragment).hide(homeFragment).hide(cartFragment).hide(userFragmeng).commit();
                        break;
                    case R.id.radio_button_cart:
                        ft.show(cartFragment).hide(homeFragment).hide(categoryFragment).hide(userFragmeng).commit();
                        break;
                    case R.id.radio_button_user:
                        ft.show(userFragmeng).hide(homeFragment).hide(categoryFragment).hide(cartFragment).commit();
                        break;
                }
            }
        });
       //默认选中
        mRadioButtonHome.setChecked(true);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        for(int i=0;i<mRadioGroup.getChildCount();i++){
            RadioButton mTab = (RadioButton)mRadioGroup.getChildAt(i);
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentByTag((String)mTab.getTag());
            FragmentTransaction ft = fm.beginTransaction();
            if(fragment!=null){
                if(!mTab.isChecked()){
                    ft.hide(fragment);
                }
            }
            ft.commit();
        }
    }
}
