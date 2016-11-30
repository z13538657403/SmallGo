package com.test.zhangtao.activitytest.bean;

/**
 * Created by zhangtao on 16/9/29.
 */
public class Banner extends BaseBean
{
    private String name;
    private String description;
    private String imgUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
