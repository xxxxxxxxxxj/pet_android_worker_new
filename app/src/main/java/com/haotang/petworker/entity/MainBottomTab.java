package com.haotang.petworker.entity;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2017/12/26 13:11
 */
public class MainBottomTab {
    private String title;
    private String pic;
    private String picRed;
    private String integralUrl;
    private String integralIntroduce;

    public MainBottomTab() {
    }

    public MainBottomTab(String title, String pic, String picRed, String integralUrl, String integralIntroduce) {
        this.title = title;
        this.pic = pic;
        this.picRed = picRed;
        this.integralUrl = integralUrl;
        this.integralIntroduce = integralIntroduce;
    }

    public MainBottomTab(String title, String pic, String picRed) {
        this.title = title;
        this.pic = pic;
        this.picRed = picRed;
    }

    public String getIntegralUrl() {
        return integralUrl;
    }

    public void setIntegralUrl(String integralUrl) {
        this.integralUrl = integralUrl;
    }

    public String getIntegralIntroduce() {
        return integralIntroduce;
    }

    public void setIntegralIntroduce(String integralIntroduce) {
        this.integralIntroduce = integralIntroduce;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPicRed() {
        return picRed;
    }

    public void setPicRed(String picRed) {
        this.picRed = picRed;
    }
}
