package com.campray.lesswalletmerchant.model;

import android.text.TextUtils;

import com.campray.lesswalletmerchant.LessWalletApplication;
import com.campray.lesswalletmerchant.db.entity.Currency;
import com.campray.lesswalletmerchant.db.entity.Language;
import com.campray.lesswalletmerchant.db.entity.User;

import com.campray.lesswalletmerchant.db.service.CurrencyDaoService;
import com.campray.lesswalletmerchant.db.service.LanguageDaoService;
import com.campray.lesswalletmerchant.db.service.UserDaoService;
import com.campray.lesswalletmerchant.listener.ApiHandleListener;
import com.campray.lesswalletmerchant.listener.OperationListener;
import com.campray.lesswalletmerchant.util.AppException;
import com.campray.lesswalletmerchant.util.EncryptionUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Locale;

/**
 * @author :Phills
 * @project:UserModel
 * @date :2017-08-22-18:09
 */
public class UserModel extends BaseModel {

    private static UserModel ourInstance = new UserModel();

    public static UserModel getInstance() {
        return ourInstance;
    }

    private UserModel() {
    }

    /**
     * 登录
     *
     * @param username
     * @param password
     * @param listener
     */
    public void login(final String username, final String password, final boolean remember, final OperationListener<User> listener) {
        if (TextUtils.isEmpty(username)) {
            listener.done(null, new AppException("E_2005"));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            listener.done(null, new AppException("E_2007"));
            return;
        }
        //如果用户在本机数据库存在
        User localUser=UserDaoService.getInstance(getContext()).getUserByNameOrEmail(username);
        if(localUser!=null){
            if(EncryptionUtil.getHash2(password,"MD5").equals(localUser.getPassword())) {
                listener.done(localUser, null);
            }
            else{
                listener.done(null, new AppException("E_2000"));
            }
            return;
        }
        //封装登录请求参数
        JsonObject jObj=new JsonObject();
        jObj.addProperty("device",this.getDeviceId());
        jObj.addProperty("uname",username);
        jObj.addProperty("pwd",password);
        jObj.addProperty("vender",true);
        this.httpPostAPI(UserModel.URL_API_LOGIN, jObj,new ApiHandleListener<JsonObject>() {
            @Override
            public void done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors").isJsonNull()) {
                            if (obj.get("Data").isJsonArray()) {
                                JsonArray jArr = obj.get("Data").getAsJsonArray();
                                if (jArr.size() > 0) {
                                    Gson gson = new Gson();
                                    User user = gson.fromJson(jArr.get(0), User.class);
                                    user.setPassword(EncryptionUtil.getHash2(password,"MD5"));
                                    user.setRemember(remember);
                                    Locale locale = getContext().getResources().getConfiguration().locale;
                                    String languageCode =locale.getLanguage() ;
                                    Language language=LanguageDaoService.getInstance(getContext()).getLanguageByCode(languageCode);
                                    user.setLanguage(language);
                                    Currency currency= CurrencyDaoService.getInstance(getContext()).getDefaultCurrency();
                                    user.setCurrenty(currency);
                                    try {
                                        UserDaoService.getInstance(getContext()).insertOrUpdateUser(user);
                                    }
                                    catch (Exception exe){
                                        listener.done(null, new AppException("E_1005"));
                                    }
                                    listener.done(user, null);
                                } else {
                                    listener.done(null, new AppException("E_2000"));
                                }
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
     * 新用户注册
     * @param username
     * @param email
     * @param password
     * @param listener
     */
    public void register(String username,String email, final String password,String firstname,String lastname,String address,String countryCode, final OperationListener<User> listener) {
        JsonObject jObj = new JsonObject();
        jObj.addProperty("device", this.getDeviceId());
        jObj.addProperty("uname", username);
        jObj.addProperty("email", email);
        jObj.addProperty("pwd", password);
        jObj.addProperty("firstname", firstname);
        jObj.addProperty("lastname", lastname);
        jObj.addProperty("address", address);
        jObj.addProperty("countrycode", countryCode);
        this.httpPostAPI(UserModel.URL_API_REGISTER, jObj,new ApiHandleListener<JsonObject>() {
            @Override
            public void done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors").isJsonNull()) {
                            if (obj.get("Data").isJsonArray()) {
                                JsonArray jArr = obj.get("Data").getAsJsonArray();
                                if (jArr.size() > 0) {
                                    Gson gson = new Gson();
                                    User user = gson.fromJson(jArr.get(0), User.class);
                                    user.setPassword(EncryptionUtil.getHash2(password,"MD5"));
                                    Locale locale = getContext().getResources().getConfiguration().locale;
                                    String languageCode = locale.getLanguage();
                                    Language language=LanguageDaoService.getInstance(getContext()).getLanguageByCode(languageCode);
                                    user.setLanguage(language);
                                    Currency currency=language.getCurrency();
                                    if(currency==null) {
                                        currency = CurrencyDaoService.getInstance(getContext()).getDefaultCurrency();
                                    }
                                    user.setCurrenty(currency);
                                    LessWalletApplication.INSTANCE().setAccount(user);
                                    long row = UserDaoService.getInstance(getContext()).insertOrUpdateUser(user);
                                    listener.done(user, null);
                                } else {
                                    listener.done(null, new AppException("E_1004"));
                                }
                            }

                        } else {
                            listener.done(null, new AppException("E_2010",obj.get("Errors").getAsString()));
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
     * 根据ID从服务端查找用户
     *  @param id
     * @param listener
     */
    public void searchUserById(long id, final OperationListener<User> listener) {
        if (id==0) {
            listener.done(null, new AppException("E_2011"));
        }

        //封装登录请求参数
        JsonObject jObj=new JsonObject();
        jObj.addProperty("device",this.getDeviceId());
        jObj.addProperty("id",id);
        this.httpPostAPI(UserModel.URL_API_GET_MERCHANT_USER, jObj,new ApiHandleListener<JsonObject>() {
            @Override
            public void done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors").isJsonNull()) {
                            if (obj.get("Data").isJsonArray()) {
                                JsonArray jArr = obj.get("Data").getAsJsonArray();
                                Gson gson = new Gson();
                                User user = gson.fromJson(jArr.get(0), User.class);
                                listener.done(user, null);
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
     * 修改用户积分
     *  @param uid
     *  @param points
     *  @param msg
     * @param listener
     */
    public void updateUserPoints(final long uid,final int points,final String msg, final OperationListener<User> listener) {
        //封装登录请求参数
        JsonObject jObj=new JsonObject();
        jObj.addProperty("device",this.getDeviceId());
        jObj.addProperty("uid",uid);
        jObj.addProperty("points",points);
        jObj.addProperty("msg",msg);
        this.httpPostAPI(UserModel.URL_API_MODIFY_POINTS, jObj,new ApiHandleListener<JsonObject>() {
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
     * 修改用户帐户余额
     *  @param uid
     *  @param amount
     *  @param msg
     * @param listener
     */
    public void updateUserCash(final long uid,final int amount,final String msg, final OperationListener<User> listener) {
        //封装登录请求参数
        JsonObject jObj=new JsonObject();
        jObj.addProperty("device",this.getDeviceId());
        jObj.addProperty("uid",uid);
        jObj.addProperty("value",amount+"");
        jObj.addProperty("msg",msg);
        this.httpPostAPI(UserModel.URL_API_MODIFY_CASH, jObj,new ApiHandleListener<JsonObject>() {
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
     * 退出登录
     */
    public void logout() {
        LessWalletApplication.INSTANCE().setAccount(null);
    }

    public User getUserById(long userId) {
        try {
           return UserDaoService.getInstance(getContext()).getUserById(userId);
        }
        catch (Exception e){ }
        return null;
    }

    public void updateUser(User user) {
        try {
            UserDaoService.getInstance(getContext()).insertOrUpdateUser(user);
        }
        catch (Exception e){ }
    }


    /**
     * 从服务端查找Paypal Braintree支付的Client Token
     * @param listener
     */
    public void getPaypalClientToken(final OperationListener<String> listener) {
        //封装登录请求参数
        JsonObject jObj=new JsonObject();
        jObj.addProperty("device",this.getDeviceId());
        this.httpPostAPI(UserModel.URL_API_GETCLIENTTOKEN, jObj,new ApiHandleListener<JsonObject>() {
            @Override
            public void done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors")==null) {
                            String clientToken=obj.get("Data").getAsString();
                            listener.done(clientToken,null);
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


}
