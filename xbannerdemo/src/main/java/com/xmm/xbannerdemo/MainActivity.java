package com.xmm.xbannerdemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.stx.xhb.xbanner.XBanner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取控件
        XBanner mXBanner = findViewById(R.id.xbanner);
        List<BannerItemBean> data = new ArrayList<>();
        data.add(new BannerItemBean());
        data.add(new BannerItemBean());
        data.add(new BannerItemBean());

        BannerUtil.setPreSaleData(this,mXBanner,data);




    }
}
