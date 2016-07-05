package com.example.bluesky.app.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by bluesky on 16/6/18.
 */
@Table(name = "shoppingCart")
public class ShoppingCartItem {
    @Column(name = "_id",isId = true)
    int id;
    @Column(name = "description")
    String description;
    @Column(name = "price")
    String price;
    @Column(name = "imgUrl")
    String imgUrl;
    @Column(name = "num")
    int num;
    @Column(name = "sourceId")
    private String sourceId;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public ShoppingCartItem() {
    }

    public ShoppingCartItem(String description, String price, String imgUrl, int num, String sourceId) {
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
        this.num = num;
        this.sourceId = sourceId;
    }

    public ShoppingCartItem(int id, String description, String price, String imgUrl, int num, String sourceId) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
        this.num = num;
        this.sourceId = sourceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "ShoppingCartItem{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", num=" + num +
                '}';
    }

}
