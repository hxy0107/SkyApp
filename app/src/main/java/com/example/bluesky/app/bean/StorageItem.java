package com.example.bluesky.app.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by bluesky on 16/6/17.
 */
@Table(name = "storage")
public class StorageItem implements Serializable{
    @Column(name = "_id", isId = true)
    int id;
    @Column(name = "picUrl")
    private String picUrl;
    @Column(name = "price")
    private String price;
    @Column(name = "origin_price")
    private String origin_price;
    @Column(name = "description")
    private String description;
    @Column(name = "sales")
    private String sales;
    @Column(name = "sourceId")
    private String sourceId;

    public StorageItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StorageItem(String picUrl, String price, String origin_price, String description, String sales, String sourceId) {
        this.picUrl = picUrl;
        this.price = price;
        this.origin_price = origin_price;
        this.description = description;
        this.sales = sales;
        this.sourceId = sourceId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOrigin_price() {
        return origin_price;
    }

    public void setOrigin_price(String origin_price) {
        this.origin_price = origin_price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public String toString() {
        return "StorageItem{" +
                "picUrl='" + picUrl + '\'' +
                ", price='" + price + '\'' +
                ", origin_price='" + origin_price + '\'' +
                ", description='" + description + '\'' +
                ", sales='" + sales + '\'' +
                ", sourceId='" + sourceId + '\'' +
                '}';
    }
}
