package com.campray.lesswalletmerchant.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.campray.lesswalletmerchant.R;
import com.campray.lesswalletmerchant.adapter.ProductAdapter;
import com.campray.lesswalletmerchant.db.entity.Country;
import com.campray.lesswalletmerchant.db.entity.Coupon;
import com.campray.lesswalletmerchant.db.entity.CouponStyle;
import com.campray.lesswalletmerchant.db.entity.Friend;
import com.campray.lesswalletmerchant.db.entity.Product;
import com.campray.lesswalletmerchant.db.entity.User;
import com.campray.lesswalletmerchant.db.service.FriendDaoService;
import com.campray.lesswalletmerchant.listener.OperationListener;
import com.campray.lesswalletmerchant.model.CouponModel;
import com.campray.lesswalletmerchant.model.ProductModel;
import com.campray.lesswalletmerchant.model.UserModel;
import com.campray.lesswalletmerchant.ui.base.MenuActivity;
import com.campray.lesswalletmerchant.util.AppException;
import com.campray.lesswalletmerchant.util.ResourcesUtils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Phills on 11/2/2017.
 */

public class CouponSendActivity extends MenuActivity {

    @BindView(R.id.iv_left)
    ImageView iv_left;
    @BindView(R.id.tv_navi_title)
    TextView tv_navi_title;
    @BindView(R.id.gl_coupon_top)
    GridLayout gl_coupon_top;
    @BindView(R.id.iv_coupon_shading)
    ImageView iv_coupon_shading;
    @BindView(R.id.iv_coupon_logo)
    ImageView iv_coupon_logo;
    @BindView(R.id.iv_coupon_img)
    ImageView iv_coupon_img;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_shortdesc)
    TextView tv_shortdesc;
    @BindView(R.id.tv_expired)
    TextView tv_expired;
    @BindView(R.id.tv_number)
    TextView tv_number;
    @BindView(R.id.ll_desc)
    LinearLayout ll_desc;

    @BindView(R.id.tv_benefit)
    TextView tv_benefit;
    @BindView(R.id.tv_benefit_value)
    TextView tv_benefit_value;
    @BindView(R.id.tv_validity)
    TextView tv_validity;

    @BindView(R.id.iv_avatar)
    ImageView iv_avatar;
    @BindView(R.id.tv_points)
    TextView tv_points;
    @BindView(R.id.tv_firstname)
    TextView tv_firstname;
    @BindView(R.id.tv_lastname)
    TextView tv_lastname;
    @BindView(R.id.tv_phone)
    TextView tv_phone;
    @BindView(R.id.tv_give)
    TextView tv_give;
    @BindView(R.id.tv_more)
    TextView tv_more;



    //要送出的卡卷ID
    private long productId;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_send);
        tv_navi_title.setText("Coupon Send");
        productId=this.getBundle().getLong("productId");
        userId=this.getBundle().getLong("userId");
        showProduct(productId);
        showUser(userId);
    }

    /**
     * 点击iv_left按钮的事件方法
     * @param view
     */
    @OnClick(R.id.iv_left)
    public void onBackClick(View view){
        this.finish();
    }

    /**
     * 点击tv_give送出按钮的事件方法
     * @param view
     */
    @OnClick(R.id.tv_confirm)
    public void onGiveClick(View view){
        //调用服务接口送出coupon给用户
        CouponModel.getInstance().sentCouponsToUser(productId,userId,new OperationListener<Coupon>() {
            @Override
            public void done(Coupon coupon, AppException exe) {
                if (exe == null) {
                    toast(getResources().getString(ResourcesUtils.getStringId(getApplicationContext(),"coupon_sent_success")));
                    startActivity(MainActivity.class, null, true);
                } else {
                    toast(exe.toString(getApplicationContext()));
                }
            }
        });
    }

    private void showProduct(long pid){
        Product product= ProductModel.getInstance().getProductById(pid);
        if(product!=null) {
            CouponStyle couponStyle = product.getCouponStyle();
            if (couponStyle != null) {
                //tv_price.setText(coupon.getCouponStyle().getBenefit());
                gl_coupon_top.setBackgroundColor(Color.parseColor(product.getCouponStyle().getBgColor()));
                if (!TextUtils.isEmpty(couponStyle.getShadingUrl())) {
                    Picasso.with(this).load(couponStyle.getShadingUrl()).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(iv_coupon_shading);
                } else {
                    iv_coupon_shading.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(couponStyle.getPictureUrl())) {
                    Picasso.with(this).load(couponStyle.getPictureUrl()).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(iv_coupon_img);
                } else {
                    iv_coupon_img.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(couponStyle.getLogoUrl())) {
                    Picasso.with(this).load(couponStyle.getLogoUrl()).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(iv_coupon_logo);
                } else {
                    iv_coupon_logo.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(couponStyle.getBenefitOne())) {
                    tv_benefit.setText(R.string.coupon_benefit_onetime);
                    tv_benefit_value.setText(couponStyle.getBenefitOne());
                } else if (!TextUtils.isEmpty(couponStyle.getBenefitPrepaidCash())) {
                    tv_benefit.setText(R.string.coupon_benefit_precash);
                    tv_benefit_value.setText(couponStyle.getBenefitPrepaidCash());
                } else if (!TextUtils.isEmpty(couponStyle.getBenefitPrepaidService())) {
                    tv_benefit.setText(R.string.coupon_benefit_preservice);
                    tv_benefit_value.setText(couponStyle.getBenefitPrepaidService());
                } else if (!TextUtils.isEmpty(couponStyle.getBenefitBuyNGetOne())) {
                    tv_benefit.setText(R.string.coupon_benefit_buyngetone);
                    tv_benefit_value.setText(couponStyle.getBenefitBuyNGetOne());
                }

                if(couponStyle.getValidityDay()>0) {
                    tv_validity.setText(couponStyle.getValidityDay()+getResources().getString(R.string.coupon_validity_day));
                }else if(couponStyle.getValidityMonth()>0) {
                    tv_validity.setText(couponStyle.getValidityMonth()+getResources().getString(R.string.coupon_validity_month));
                }else if(couponStyle.getValidityYear()>0) {
                    tv_validity.setText(couponStyle.getValidityYear()+getResources().getString(R.string.coupon_validity_year));
                }
                else{
                    tv_validity.setText(getResources().getString(R.string.coupon_validity_nolimit));
                }
            }
            tv_title.setText(product.getTitle());
            tv_price.setText(product.getPriceStr());
            tv_shortdesc.setText(product.getShortDesc());
            String expired = (TextUtils.isEmpty(product.getStartTimeLocal()) ? "" : product.getStartTimeLocal()) + (TextUtils.isEmpty(product.getEndTimeLocal()) ? "" : " - " + product.getEndTimeLocal());
            tv_expired.setText(expired);
            tv_number.setText(product.getStockQuantity()+"");
            tv_give.setVisibility(View.GONE);
            gl_coupon_top.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ll_desc.isShown()){
                        ll_desc.setVisibility(View.GONE);
                    }
                    else {
                        ll_desc.setVisibility(View.VISIBLE);
                    }
                }
            });
            tv_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(CouponSendActivity.this, ProductDetailActivity.class);
                    intent.putExtra("id",productId);
                    startActivity(intent);
                }
            });
        }
        else{
            toast("加载Coupon失败");
        }
    }

    private void showUser(long uid){
        UserModel.getInstance().searchUserById(uid, new OperationListener<User>() {
            @Override
            public void done(User obj, AppException exception) {
                if(exception==null){
                    tv_firstname.setText(obj.getFirstName());
                    tv_lastname.setText(obj.getLastName());
                    tv_phone.setText(obj.getMobile());
                    tv_points.setText(obj.getPoints()+"");
                    Picasso.with(CouponSendActivity.this).load(obj.getAvatarUrl()).placeholder(R.mipmap.icon_account).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(iv_avatar);
                }
            }
        });
    }

}
