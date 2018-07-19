package com.campray.lesswalletmerchant.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.campray.lesswalletmerchant.LessWalletApplication;
import com.campray.lesswalletmerchant.R;
import com.campray.lesswalletmerchant.db.entity.Coupon;
import com.campray.lesswalletmerchant.db.entity.CouponStyle;
import com.campray.lesswalletmerchant.db.entity.Product;
import com.campray.lesswalletmerchant.model.CouponModel;
import com.campray.lesswalletmerchant.model.ProductModel;
import com.campray.lesswalletmerchant.qrcode.encode.QRCodeEncoder;
import com.campray.lesswalletmerchant.ui.base.MenuActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Phills on 6/2/2018.
 */

public class CardUseActivity extends MenuActivity {
    @BindView(R.id.iv_left)
    ImageView iv_left;
    @BindView(R.id.tv_navi_title)
    TextView tv_navi_title;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.tv_validity)
    TextView tv_validity;
    @BindView(R.id.tv_shortdesc)
    TextView tv_shortdesc;
    @BindView(R.id.tv_desc)
    TextView tv_desc;
    @BindView(R.id.tv_expired)
    TextView tv_expired;
    @BindView(R.id.tv_benefit)
    TextView tv_benefit;
    @BindView(R.id.tv_benefit_value)
    TextView tv_benefit_value;
    @BindView(R.id.ll_benefit)
    LinearLayout ll_benefit;


    private String cash="";
    private String service="";
    private String buy="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_use);
        tv_navi_title.setText("Card Information");
        long productId=this.getIntent().getLongExtra("id",0);
        String expired=this.getIntent().getStringExtra("expired");
        cash=this.getIntent().getStringExtra("cash");
        service=this.getIntent().getStringExtra("service");
        buy=this.getIntent().getStringExtra("buy");
        loadData(productId,expired);
    }

    /**
     * 点击iv_left按钮的事件方法
     * @param view
     */
    @OnClick(R.id.iv_left)
    public void onBackClick(View view){
        this.finish();
    }

    private void loadData(long productId,String expired){
        Product product= ProductModel.getInstance().getProductById(productId);
        CouponStyle couponStyle=product.getCouponStyle();
        if (couponStyle != null) {
            if(!TextUtils.isEmpty(couponStyle.getBenefitOne())) {
                tv_benefit.setText(R.string.coupon_benefit_onetime);
                tv_benefit_value.setText(couponStyle.getBenefitOne());
            }else if(!TextUtils.isEmpty(couponStyle.getBenefitPrepaidCash())) {
                tv_benefit.setText(R.string.coupon_benefit_precash);
                if(TextUtils.isEmpty(cash)) {
                    tv_benefit_value.setText(couponStyle.getBenefitPrepaidCash());
                }
                else{
                    tv_benefit_value.setText(couponStyle.getBenefitPrepaidCash()+"/"+cash);
                }
            }else if(!TextUtils.isEmpty(couponStyle.getBenefitPrepaidService())) {
                tv_benefit.setText(R.string.coupon_benefit_preservice);
                if(TextUtils.isEmpty(service)) {
                    tv_benefit_value.setText(couponStyle.getBenefitPrepaidService());
                }
                else{
                    tv_benefit_value.setText(couponStyle.getBenefitPrepaidService()+"/"+service);
                }
            }else if(!TextUtils.isEmpty(couponStyle.getBenefitBuyNGetOne())) {
                tv_benefit.setText(R.string.coupon_benefit_buyngetone);
                if(TextUtils.isEmpty(service)) {
                    tv_benefit_value.setText(couponStyle.getBenefitBuyNGetOne());
                }
                else{
                    tv_benefit_value.setText(couponStyle.getBenefitBuyNGetOne()+"/"+buy);
                }
            }
            else{
                ll_benefit.setVisibility(View.GONE);
            }

            if (couponStyle.getValidityDay() > 0) {
                tv_validity.setText(couponStyle.getValidityDay() + getResources().getString(R.string.coupon_validity_day));
            } else if (couponStyle.getValidityMonth() > 0) {
                tv_validity.setText(couponStyle.getValidityMonth() + getResources().getString(R.string.coupon_validity_month));
            } else if (couponStyle.getValidityYear() > 0) {
                tv_validity.setText(couponStyle.getValidityYear() + getResources().getString(R.string.coupon_validity_year));
            }
            else{
                tv_validity.setText(getResources().getString(R.string.coupon_validity_nolimit));
            }
        }
        tv_title.setText(product.getTitle());
        tv_price.setText(product.getPriceStr());
        tv_shortdesc.setText(product.getShortDesc());
        tv_expired.setText(expired);

        //显示html文本（只有html样式,不带图片）
        if(!TextUtils.isEmpty(product.getFullDesc())) {
            tv_desc.setText(Html.fromHtml(product.getFullDesc()));
        }

    }

}
