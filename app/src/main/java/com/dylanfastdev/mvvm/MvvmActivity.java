package com.dylanfastdev.mvvm;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.dylanfastdev.BR;
import com.dylanfastdev.R;


/**
 * @author YDL
 * @version 1.0
 * @date 2020/06/12/15:30
 */
public class MvvmActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.ac_mvvm);
        User user = new User("Test", "User");
        viewDataBinding.setVariable(BR.user, user);
        ImageView iv_image=findViewById(R.id.iv_image);
        iv_image.setImageResource(R.mipmap.ac_icon_loading);
    }
}
