package com.campray.lesswalletmerchant.db.entity;

import android.text.TextUtils;

import com.campray.lesswalletmerchant.model.CurrencyModel;

/**
 * Coupon显示样式属性
 * Created by Phills on 12/1/2017.
 */

public class CouponStyle {
    private String benefitOne;

    private String benefitPrepaidCash;

    private String benefitPrepaidService;

    private String benefitBuyNGetOne;


    private String bgColor="#00000000";

    private String shadingUrl;

    private String pictureUrl;

    private String logoUrl;

    //会员级别：数字越高，级别越高
    private int cardLevel=0;

    private int validityDay=0;
    private int validityMonth=0;
    private int validityYear=0;


    public String getBenefitOne() {
        return benefitOne;
    }

    public void setBenefitOne(String benefitOne) {
        this.benefitOne = benefitOne;
    }

    public String getBenefitPrepaidCash() {
        if(!TextUtils.isEmpty(benefitPrepaidCash)) {
            Currency currency = CurrencyModel.getInstance().getDefaultCurrency();
            String fmtStr = currency.getCustomFormatting().replace("0.00", "");
            return fmtStr+benefitPrepaidCash;
        }
        return benefitPrepaidCash;
    }

    public void setBenefitPrepaidCash(String benefitPrepaidCash) {
        this.benefitPrepaidCash = benefitPrepaidCash;
    }

    public String getBenefitPrepaidService() {
        return benefitPrepaidService;
    }

    public void setBenefitPrepaidService(String benefitPrepaidService) {
        this.benefitPrepaidService = benefitPrepaidService;
    }

    public String getBenefitBuyNGetOne() {
        return benefitBuyNGetOne;
    }

    public void setBenefitBuyNGetOne(String benefitBuyNGetOne) {
        this.benefitBuyNGetOne = benefitBuyNGetOne;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getShadingUrl() {
        return shadingUrl;
    }

    public void setShadingUrl(String shadingUrl) {
        this.shadingUrl = shadingUrl;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public int getCardLevel() {
        return cardLevel;
    }

    public void setCardLevel(int cardLevel) {
        this.cardLevel = cardLevel;
    }

    public int getValidityDay() {
        return validityDay;
    }

    public void setValidityDay(int validityDay) {
        this.validityDay = validityDay;
    }

    public int getValidityMonth() {
        return validityMonth;
    }

    public void setValidityMonth(int validityMonth) {
        this.validityMonth = validityMonth;
    }

    public int getValidityYear() {
        return validityYear;
    }

    public void setValidityYear(int validityYear) {
        this.validityYear = validityYear;
    }
}
