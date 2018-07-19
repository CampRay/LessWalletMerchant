package com.campray.lesswalletmerchant.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.campray.lesswalletmerchant.LessWalletApplication;
import com.campray.lesswalletmerchant.R;
import com.campray.lesswalletmerchant.db.entity.Country;
import com.campray.lesswalletmerchant.db.entity.Currency;
import com.campray.lesswalletmerchant.db.entity.Language;
import com.campray.lesswalletmerchant.db.entity.ProductAttribute;
import com.campray.lesswalletmerchant.db.entity.Slider;
import com.campray.lesswalletmerchant.db.entity.User;
import com.campray.lesswalletmerchant.listener.OperationListener;
import com.campray.lesswalletmerchant.model.CountryModel;
import com.campray.lesswalletmerchant.model.CurrencyModel;
import com.campray.lesswalletmerchant.model.LanguageModel;
import com.campray.lesswalletmerchant.model.ProductAttributeModel;
import com.campray.lesswalletmerchant.model.SliderModel;
import com.campray.lesswalletmerchant.service.MsgPushService;
import com.campray.lesswalletmerchant.ui.base.BaseActivity;
import com.campray.lesswalletmerchant.util.AppException;

import java.util.List;

/**启动界面
 * @author :smile
 * @project:SplashActivity
 * @date :2018-01-15-18:23
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //运行后台服务
        startService();
        Handler handler =new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                User user= LessWalletApplication.INSTANCE().getAccount();
                //第一次启用App时下载所有初始数据
                if(user == null){
                    //下载国家数据
                    CountryModel.getInstance().getAllCountriesFromServer(new OperationListener<List<Country>>(){
                        @Override
                        public void done(List<Country> obj, AppException exception) {}
                    });
                    //下载语言数据
                    LanguageModel.getInstance().getAllLanguagesFromServer(new OperationListener<List<Language>>(){
                        @Override
                        public void done(List<Language> obj, AppException exception) {}
                    });
                    //下载货币数据
                    CurrencyModel.getInstance().getAllCurrenciesFromServer(new OperationListener<List<Currency>>(){
                        @Override
                        public void done(List<Currency> obj, AppException exception) {}
                    });
//                    //下载商品属性数据
//                    ProductAttributeModel.getInstance().getAllProdAttrFromServer(new OperationListener<List<ProductAttribute>>(){
//                        @Override
//                        public void done(List<ProductAttribute> obj, AppException exception) {}
//                    });

                }
                startActivity(LoginActivity.class,null,true);
            }
        },1000);

    }

    /**
     * 启动消息同步Service
     */
    private void startService(){
        Intent intent = new Intent(this, MsgPushService.class);
        startService(intent);
    }
}
