package com.bawei.todayheadline.bean;

/**
 * 类的用途：
 * Created by ：杨珺达
 * date：2017/3/21
 */

public class Shou {
    private String image;
    private String imagea;
    private String imageb;
    private String title;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImagea() {
        return imagea;
    }

    public void setImagea(String imagea) {
        this.imagea = imagea;
    }

    public String getImageb() {
        return imageb;
    }

    public void setImageb(String imageb) {
        this.imageb = imageb;
    }

    @Override
    public String toString() {
        return "Shou{" +
                "image='" + image + '\'' +
                ", imagea='" + imagea + '\'' +
                ", imageb='" + imageb + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
