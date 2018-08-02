package com.campray.lesswalletmerchant.model;

import android.net.Uri;
import android.text.TextUtils;

import com.campray.lesswalletmerchant.db.entity.Coupon;
import com.campray.lesswalletmerchant.db.entity.CouponStyle;
import com.campray.lesswalletmerchant.db.entity.Product;
import com.campray.lesswalletmerchant.db.entity.SpecAttr;
import com.campray.lesswalletmerchant.db.service.CouponDaoService;
import com.campray.lesswalletmerchant.db.service.ProductDaoService;
import com.campray.lesswalletmerchant.listener.ApiHandleListener;
import com.campray.lesswalletmerchant.listener.OperationListener;
import com.campray.lesswalletmerchant.util.AppException;
import com.campray.lesswalletmerchant.util.ImageUtil;
import com.campray.lesswalletmerchant.util.TimeUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.TimeZone;

/**
 * 卡卷对象业务处理模型
 * @author :Phills
 * @project:CouponModel
 * @date :2017-08-22-18:09
 */
public class CouponModel extends BaseModel {

    private static CouponModel ourInstance = new CouponModel();

    public static CouponModel getInstance() {
        return ourInstance;
    }

    private CouponModel() {
    }

    /**
     * 根据订单ID从服务端获取到卡卷
     * @param oid 订单ID
     * @param listener
     */
    public void getCouponFromServer(long oid,final OperationListener<Coupon> listener){
        //封装登录请求参数
        JsonObject jObj=new JsonObject();
        jObj.addProperty("orderId",oid);
        jObj.addProperty("device",this.getDeviceId());
        this.httpPostAPI(CouponModel.URL_API_GETCOUPON, jObj,new ApiHandleListener<JsonObject>() {
            @Override
            public void done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors").isJsonNull()) {
                            if (obj.get("Data").isJsonArray()) {
                                JsonArray jArr = obj.get("Data").getAsJsonArray();
                                Gson gson = new Gson();
                                for(JsonElement item:jArr){
                                    final Coupon coupon = gson.fromJson(item, Coupon.class);
                                    final Product product=ProductModel.getInstance().getProductById(coupon.getProductId());
                                    if(product==null) {
                                        ProductModel.getInstance().getProductFromServer(coupon.getProductId(), new OperationListener<Product>() {
                                            @Override
                                            public void done(Product obj, AppException exception) {
                                                if(obj!=null) {
                                                    coupon.setProduct(obj);
                                                    ProductModel.getInstance().insertOrUpdateProduct(obj);
                                                    CouponStyle couponStyle = obj.getCouponStyle();
                                                    if (couponStyle.getValidityYear() > 0 || couponStyle.getValidityMonth() > 0 || couponStyle.getValidityDay() > 0) {
                                                        String endDateStr = TimeUtil.modifyDateStr(coupon.getStartTime(), TimeUtil.FORMAT_DATE_TIME_SECOND, TimeZone.getTimeZone("UTC"), couponStyle.getValidityYear(), couponStyle.getValidityMonth(), couponStyle.getValidityDay());
                                                        coupon.setEndTime(endDateStr);
                                                    }
                                                    listener.done(coupon, null);
                                                }
                                                else{
                                                    listener.done(null, exception);
                                                }
                                            }
                                        });
                                    }
                                    else {
                                        coupon.setProduct(product);
                                        CouponStyle couponStyle = product.getCouponStyle();
                                        if (couponStyle.getValidityYear() > 0 || couponStyle.getValidityMonth() > 0 || couponStyle.getValidityDay() > 0) {
                                            String endDateStr = TimeUtil.modifyDateStr(coupon.getStartTime(), TimeUtil.FORMAT_DATE_TIME_SECOND, TimeZone.getTimeZone("UTC"), couponStyle.getValidityYear(), couponStyle.getValidityMonth(), couponStyle.getValidityDay());
                                            coupon.setEndTime(endDateStr);
                                        }
                                        listener.done(coupon, null);
                                    }
                                    break;
                                }
                            }
                            else {
                                listener.done(null, new AppException("E_3020"));
                            }
                        } else {
                            listener.done(null, new AppException(obj.get("Errors").getAsString()));
                        }
                    }
                    catch (Exception e){
                        listener.done(null, new AppException("E_1004"));
                    }
                } else {
                    listener.done(null, exception);
                }
            }
        });
    }

    /**
     * 从服务器删除指定的卡卷数据
     * @param ids 要删除的卡卷ID字符串
     * @param listener
     */
    public void delCouponsFromServer(String ids, final OperationListener<Coupon> listener) {
        //封装登录请求参数
        JsonObject jObj=new JsonObject();
        jObj.addProperty("device",this.getDeviceId());
        jObj.addProperty("orderIds",ids);
        jObj.addProperty("isvendor",true);
        this.httpPostAPI(CouponModel.URL_API_DELCOUPONS, jObj,new ApiHandleListener<JsonObject>() {
            @Override
            public void done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //在本地数据库删除服务器已经被移除的记录
                        JsonArray jArr = obj.get("Data").getAsJsonArray();
                        for(JsonElement item:jArr){
                            int deletedCouponId=item.getAsInt();
                            deleteCouponById(deletedCouponId);
                        }

                        //如果返回结果没有异常
                        if (obj.get("Errors").isJsonNull()) {
                            listener.done(null, null);
                        } else {
                            listener.done(null, new AppException(obj.get("Errors").getAsString()));
                        }
                    }
                    catch (Exception e){
                        listener.done(null, new AppException("E_1004"));
                    }
                } else {
                    listener.done(null, exception);
                }
            }
        });
    }

    /**
     * 转送卡卷给用户
     * @param productId 卡卷ID
     * @param userId 用户ID
     * @param listener
     */
    public void sentCouponsToUser(final long productId,final long userId, final OperationListener<Coupon> listener) {
        //封装登录请求参数
        JsonObject jObj=new JsonObject();
        jObj.addProperty("device",this.getDeviceId());
        jObj.addProperty("productId",productId);
        jObj.addProperty("customerId",userId);
        this.httpPostAPI(CouponModel.URL_API_SENT_PRODUCT, jObj,new ApiHandleListener<JsonObject>() {
            @Override
            public void done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors").isJsonNull()) {
                            listener.done(null, null);
                        } else {
                            listener.done(null, new AppException(obj.get("Errors").getAsString()));
                        }
                    }
                    catch (Exception e){
                        listener.done(null, new AppException("E_1004"));
                    }
                } else {
                    listener.done(null, exception);
                }
            }
        });
    }


    /**
     * 查询所有卡卷数据对象
     * @return
     */
    public List<Coupon> getAllCoupons() {
        try {
            return CouponDaoService.getInstance(getContext()).getAllCoupons();
        }
        catch (Exception e){ }
        return null;
    }

    /**
     * 根据类型查询所有的卡卷对象
     * @param typeId
     * @return
     */
    public List<Coupon> getAllCouponByType(int typeId) {
        try {
            return CouponDaoService.getInstance(getContext()).getAllCouponByType(typeId);
        }
        catch (Exception e){ }
        return null;
    }

    /**
     * 分页显示信息
     * @param typeId          卡卷类别
     * @param pageNum         当前页数
     * @param pageSize        每页显示数
     * @return                信息列表
     */
    public List<Coupon> getCouponsPageByType(int typeId,int pageNum,int pageSize)
    {
        try {
            return CouponDaoService.getInstance(getContext()).getPageCouponByType(typeId,pageNum,pageSize);
        }
        catch (Exception e){ }
        return null;
    }

    /**
     * 根据记录ID查询卡卷数据对象
     * @param couponId
     * @return
     */
    public Coupon getCouponById(long couponId) {
        try {
            return CouponDaoService.getInstance(getContext()).getCoupon(couponId);
        }
        catch (Exception e){ }
        return null;
    }



    /**
     * 修改卡卷对象
     * @param coupon
     * @return
     */
    public boolean updateCoupon(Coupon coupon) {
        try {
            CouponDaoService.getInstance(getContext()).updateCoupon(coupon);
            return true;
        }
        catch (Exception e){ return false;}
    }

    /**
     * 删除卡卷对象
     * @param coupon
     * @return
     */
    public boolean deleteCoupon(Coupon coupon) {
        try {
           CouponDaoService.getInstance(getContext()).deleteCoupon(coupon);
            return true;
        }
        catch (Exception e){ return false;}
    }

    /**
     * 删除卡卷对象
     * @param couponId
     * @return
     */
    public boolean deleteCouponById(long couponId) {
        try {
            CouponDaoService.getInstance(getContext()).deleteCoupon(couponId);
            return true;
        }
        catch (Exception exe){
            return false;
        }
    }

}
