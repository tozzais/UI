package com.xmm.xbannerdemo;

import com.stx.xhb.xbanner.entity.SimpleBannerInfo;


public class BannerItemBean extends SimpleBannerInfo {
    
    



    public int banner_id;//轮播图id
    public int category;//轮播图显示位置  1：系统首页  2：商城首页
    public int banner_type;//类型    0：无连接 1：超链接 2：图文 3：分类 4：活动
    public String title;//轮播图标题
    public String logo;//轮播图logo
    public String url;//轮播图url
    public int banner_type_id;//分类id/活动id/商品id
    public int status;//状态  0：正常  1：隐藏
    public int sort;//排序
    public String createtime;//创建时间
    public String content;//内容




    @Override
    public String getXBannerUrl() {
        return logo;
    }


}
