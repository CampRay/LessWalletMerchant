package com.campray.lesswalletmerchant.db.entity;

/**
 * 电子票的自定义用户属性值
 * Created by Phills on 10/25/2017.
 */
public class UserAttrValue {
    private long Id;
    private String Name;//属性值

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
