package com.campray.lesswalletmerchant.db.entity;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Transient;

import com.campray.lesswalletmerchant.db.converter.BenefitAttrConverter;
import com.campray.lesswalletmerchant.db.dao.DaoSession;
import com.campray.lesswalletmerchant.db.dao.ProductDao;
import com.campray.lesswalletmerchant.db.dao.CouponDao;
import com.campray.lesswalletmerchant.util.TimeUtil;

import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;


/**
 * 卡卷订单信息
 * Created by Phills on 10/24/2017.
 */
@Entity
public class Coupon {
    @Id(autoincrement =false )
    private Long orderId;//对应的服务端订单ID
    private Long productId;//对应的商品ID
    @Transient
    private Product product;
    private String cid; //卡卷編號
    @NotNull
    private long userId; //所属用户名称
    private float orderTotal;//总支付金额
    private int paymentStatus;//支付状态: 已支付 30
    @NotNull
    private String startTime;//支付时间
    private String endTime;//过期时间
    @NotNull
    private boolean deleted=false;//是否已使用

    @Transient
    private List<UserAttrValue> userAttrValues; //用户选择或输入的属性的值

    @Transient
    private String startTimeLocal;//生效时间-显示用户时区
    @Transient
    private String endTimeLocal;//过期时间-显示用户时区
    @Transient
    private CouponStyle couponStyle;//Coupon显示样式
    @Generated(hash = 1197839015)
    public Coupon(Long orderId, Long productId, String cid, long userId, float orderTotal, int paymentStatus, @NotNull String startTime, String endTime, boolean deleted) {
        this.orderId = orderId;
        this.productId = productId;
        this.cid = cid;
        this.userId = userId;
        this.orderTotal = orderTotal;
        this.paymentStatus = paymentStatus;
        this.startTime = startTime;
        this.endTime = endTime;
        this.deleted = deleted;
    }

    @Generated(hash = 75265961)
    public Coupon() {
    }

    public String getStartTimeLocal() {
        if (!TextUtils.isEmpty(startTime)) {
            startTimeLocal = TimeUtil.dateToString(TimeUtil.stringToDate(startTime, TimeUtil.FORMAT_DATE_TIME_SECOND, TimeZone.getTimeZone("UTC")), TimeUtil.FORMAT_DATE);
        }
        return startTimeLocal;
    }

    public void setStartTimeLocal(String startTimeLocal) {
        this.startTimeLocal = startTimeLocal;
    }

    public String getEndTimeLocal() {
        if (!TextUtils.isEmpty(endTime)) {
            endTimeLocal = TimeUtil.dateToString(TimeUtil.stringToDate(endTime, TimeUtil.FORMAT_DATE_TIME_SECOND, TimeZone.getTimeZone("UTC")), TimeUtil.FORMAT_DATE);
        }
        return endTimeLocal;
    }

    public void setEndTimeLocal(String endTimeLocal) {
        this.endTimeLocal = endTimeLocal;
    }

    public CouponStyle getCouponStyle() {
        if(this.getProduct()!=null) {
            if(couponStyle==null) {
                couponStyle = new CouponStyle();
                //循环遍历当前优惠卷的产品规格属性
                for (SpecAttr specAttr : this.product.getSpecAttr()) {
                    //String selectValue = specAttr.getColorSquaresRgb();//用户通过下接选项选择的值
                    String value = specAttr.getValueRaw();//用户自已输入的值
                    //String value = (TextUtils.isEmpty(selectValue) && TextUtils.isEmpty(customValue)) ? specAttr.getSpecificationAttributeName() : (TextUtils.isEmpty(selectValue) ? customValue : selectValue);
                    if (specAttr.getSpecificationAttributeId() == 1) {
                        couponStyle.setBenefitFree(value);
                    } else if (specAttr.getSpecificationAttributeId() == 2) {
                        couponStyle.setBenefitCash(value);
                    } else if (specAttr.getSpecificationAttributeId() == 3) {
                        couponStyle.setBenefitDiscount(value);
                    } else if (specAttr.getSpecificationAttributeId() == 4) {
                        couponStyle.setBenefitCustomized(value);
                    }else if (specAttr.getSpecificationAttributeId() == 5) {//如果是背景色
                        couponStyle.setBgColor(value);
                    } else if (specAttr.getSpecificationAttributeId() == 6) {//如果是底纹
                        couponStyle.setShadingUrl(TextUtils.isEmpty(specAttr.getFileUrl()) ? value : specAttr.getFileUrl());
                    } else if (specAttr.getSpecificationAttributeId() == 7) {//如果是自定义图片
                        couponStyle.setPictureUrl(TextUtils.isEmpty(specAttr.getFileUrl()) ? value : specAttr.getFileUrl());
                    } else if (specAttr.getSpecificationAttributeId() == 8) {//如果是商用家logo
                        couponStyle.setLogoUrl(TextUtils.isEmpty(specAttr.getFileUrl()) ? value : specAttr.getFileUrl());
                    } else if (specAttr.getSpecificationAttributeId() == 9) {//如果是商用会员卡级别
                        try {
                            couponStyle.setCardLevel(Integer.parseInt(value));
                        }catch (Exception e){}
                    } else if (specAttr.getSpecificationAttributeId() == 10) {//如果是有效期（日）
                        try {
                            couponStyle.setValidityDay(Integer.parseInt(value));
                        }catch (Exception e){}
                    } else if (specAttr.getSpecificationAttributeId() == 11) {//如果是有效期（月）
                        try {
                            couponStyle.setValidityMonth(Integer.parseInt(value));
                        }catch (Exception e){}
                    } else if (specAttr.getSpecificationAttributeId() == 12) {//如果是有效期（年）
                        try {
                            couponStyle.setValidityYear(Integer.parseInt(value));
                        }catch (Exception e){}
                    } else {
                    }
                }
            }
        }
        return couponStyle;
    }

    public void setCouponStyle(CouponStyle couponStyle) {
        this.couponStyle = couponStyle;
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return this.productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getCid() {
        return this.cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public float getOrderTotal() {
        return this.orderTotal;
    }

    public void setOrderTotal(float orderTotal) {
        this.orderTotal = orderTotal;
    }

    public int getPaymentStatus() {
        return this.paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean getDeleted() {
        return this.deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    



}
