package com.campray.lesswalletmerchant.model;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.campray.lesswalletmerchant.LessWalletApplication;
import com.campray.lesswalletmerchant.db.entity.User;
import com.campray.lesswalletmerchant.listener.ApiHandleListener;
import com.campray.lesswalletmerchant.util.AppException;
import com.campray.lesswalletmerchant.util.DeviceUuidFactory;
import com.campray.lesswalletmerchant.util.NetWorkUtils;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import static com.campray.lesswalletmerchant.LessWalletApplication.INSTANCE;

/**
 * 业务模型基类（业务模型主要处理所有数据Bean的业务逻辑）
 * @author :Phills
 * @project:BaseModel
 * @date :2017-08-23-10:37
 */
public abstract class BaseModel {
    public String deviceId=null;
    public static final String HOST="http://192.168.2.3:15536";
    public static final String URL_API_LOGIN=HOST+"/Plugins/API/Login";
    public static final String URL_API_REGISTER=HOST+"/Plugins/API/Register";
    public static final String URL_API_GETPRODUCT=HOST+"/Plugins/API/GetProduct";
    public static final String URL_API_CREATEORDER=HOST+"/Plugins/API/CreateOrder";
    public static final String URL_API_GETCOUPON=HOST+"/Plugins/API/GetCoupon";
    public static final String URL_API_GETALLCOUPONS=HOST+"/Plugins/API/GetAllCoupons";
    public static final String URL_API_DELCOUPONS=HOST+"/Plugins/API/DeleteCoupons";
    public static final String URL_API_GETALLCOUNTRIES=HOST+"/Plugins/API/GetAllCountries";
    public static final String URL_API_GETALLLANGUAGES=HOST+"/Plugins/API/GetAllLanguages";
    public static final String URL_API_GETALLCURRENCIES=HOST+"/Plugins/API/GetAllCurrencyes";
    public static final String URL_API_GETALLUSERATTRS=HOST+"/Plugins/API/GetAllUserAttrs";
    public static final String URL_API_GETMERCHANT=HOST+"/Plugins/API/GetMerchant";
    public static final String URL_API_GETALLHISTORIES=HOST+"/Plugins/API/GetAllLogs";

    public static final String URL_API_GET_USER_BYID=HOST+"/Plugins/API/GeUserById";
    public static final String URL_API_SEARCH_USER=HOST+"/Plugins/API/SearchUsers";
    public static final String URL_API_GET_FRIENDS=HOST+"/Plugins/API/GetAllFriends";
    public static final String URL_API_ADD_FRIEND=HOST+"/Plugins/API/AddFriend";
    public static final String URL_API_DEL_FRIEND=HOST+"/Plugins/API/DelFriend";
    public static final String URL_API_SENT_COUPON=HOST+"/Plugins/API/SentCoupon";

    public static final String URL_API_MSGSYNC=HOST+"/Plugins/API/MsgSync";
    public static final String URL_API_DEL_HISTORIES=HOST+"/Plugins/API/DelLogs";

    public static final String URL_API_GETALLSLIDERS=HOST+"/Plugins/API/GetSliderData";
    public static final String URL_API_GETCLIENTTOKEN=HOST+"/Plugins/PaymentPayPalStandard/GetClientToken";
    public static final String URL_API_PAYPALCOUPON=HOST+"/Plugins/PaymentPayPalStandard/CreateTransaction";

    public static final String URL_API_GETALL_MERCHANT_PRODUCTS=HOST+"/Plugins/API/GetAllMerchantProducts";
    public static final String URL_API_GETALL_MERCHANT_PRODUCTIDS=HOST+"/Plugins/API/GetAllMerchantProductIds";
    public static final String URL_API_MODIFY_POINTS=HOST+"/Plugins/API/ModifyPoints";
    public static final String URL_API_MODIFY_BENEFIT=HOST+"/Plugins/API/ModifyBenefit";
    public static final String URL_API_SENT_PRODUCT=HOST+"/Plugins/API/SendProductToUser";

    private Handler apiHandler;
    public Context getContext(){
        return INSTANCE();
    }

    public String getDeviceId(){
        if(TextUtils.isEmpty(deviceId)) {
            deviceId= new DeviceUuidFactory(getContext()).getDeviceUuid().toString();
        }
        return deviceId;
    }

    //带参数的http请求
    public void httpPostAPI(String url, JsonObject json, final ApiHandleListener<JsonObject> apiListener){
        if(!NetWorkUtils.isNetworkConnected(getContext())){
            apiListener.done(null,new AppException("E_1001"));
        }
        else{
            User user= LessWalletApplication.INSTANCE().getAccount();
            String token="";
            if(user!=null){
                token=user.getToken();
            }
            if(json==null) {
                json = new JsonObject();
            }
            Ion.with(getContext()).load(url)
                .addHeader("Authorization",token)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception exe, JsonObject result) {
                        if (exe == null) {
                            apiListener.done(result, null);
                        } else {
                            apiListener.done(result, new AppException("E_1003", exe));
                        }
                    }
                });
        }
    }

}
