package com.xmm.xbannerdemo;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.stx.xhb.xbanner.XBanner;

import java.util.List;


public class BannerUtil {
    /*
    banner_type  类型 0：无连接 1：超链接 2：图文 3：商品分类 4：菜谱分类 5：活动 6：商品 7：菜谱
    banner_type_id 商品分类id/菜谱分类id/活动id/商品id/菜谱id
     */
    public static void setData(Activity context, XBanner xbanner, List<BannerItemBean> data){
        if (data == null ||data.size()==0){
            xbanner.setVisibility(View.GONE);
            return;
        }

        xbanner.setBannerData(data);
        xbanner.setAutoPlayAble(true);
        xbanner.loadImage(((banner, model, view, position) -> {

            ((ImageView) view).setImageResource(R.mipmap.product2);
//            ImageUtil.loadNet(context, (ImageView) view, ((BannerItemBean) model).getXBannerUrl());
         }
        ));
        xbanner.setOnItemClickListener(((banner, model, view, position) -> {

        }));
    }


    public static void setPreSaleData(Activity context, XBanner xbanner, List<BannerItemBean> data){
        if (data == null ||data.size()==0){
            xbanner.setVisibility(View.GONE);
            return;
        }
        xbanner.setBannerData(R.layout.item_scaleimage_banner,data);
        xbanner.setAutoPlayAble(true);
        xbanner.loadImage(((banner, model, view, position) -> {
            ScaleImageView imageView = view.findViewById(R.id.iv_image);
            }
        ));
        xbanner.setOnItemClickListener(((banner, model, view, position) -> {


        }));
    }

}
