package com.campray.lesswalletmerchant.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.campray.lesswalletmerchant.LessWalletApplication;
import com.campray.lesswalletmerchant.R;
import com.campray.lesswalletmerchant.adapter.ProductAdapter;
import com.campray.lesswalletmerchant.db.entity.Coupon;
import com.campray.lesswalletmerchant.db.entity.CouponStyle;
import com.campray.lesswalletmerchant.db.entity.Product;
import com.campray.lesswalletmerchant.db.entity.SpecAttr;
import com.campray.lesswalletmerchant.db.entity.User;
import com.campray.lesswalletmerchant.listener.OperationListener;
import com.campray.lesswalletmerchant.model.CouponModel;
import com.campray.lesswalletmerchant.model.UserModel;
import com.campray.lesswalletmerchant.ui.base.MenuActivity;
import com.campray.lesswalletmerchant.util.AppException;
import com.campray.lesswalletmerchant.util.ResourcesUtils;
import com.campray.lesswalletmerchant.view.RoundImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 扫描Coupon二维码后页面
 * Created by Phills on 7/2/2018.
 */

public class ScanCouponActivity extends MenuActivity {
    //Coupon控件
    @BindView(R.id.gl_coupon_top)
    GridLayout gl_coupon_top;
    @BindView(R.id.iv_coupon_shading)
    ImageView iv_coupon_shading;
    @BindView(R.id.iv_coupon_img)
    ImageView iv_coupon_img;
    @BindView(R.id.iv_coupon_logo)
    ImageView iv_coupon_logo;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_merchant)
    TextView tv_merchant;
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
    @BindView(R.id.tv_shortdesc)
    TextView tv_shortdesc;
    @BindView(R.id.tv_more)
    TextView tv_more;


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

    @BindView(R.id.tv_add)
    TextView tv_add;
    @BindView(R.id.tv_reduce)
    TextView tv_reduce;
    @BindView(R.id.et_points)
    EditText et_points;
    @BindView(R.id.btn_confirm)
    Button btn_confirm;



    @BindView(R.id.rl_dialog)
    RelativeLayout rl_dialog;

    private Coupon curCoupon=null;
    private boolean isAdd=false;
    private int key=0;

    private String serviceTime;
    private String cashAmount;
    private String buyTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_coupon);
        long oid = this.getBundle().getLong("id");
        //根据扫码获取的ID，从服务器获取对应的电子卡卷信息
        getCoupondFromServer(oid);
    }

    /**
     * 从服务器获取下载Coupon
     * @param oid
     */
    private void getCoupondFromServer(long oid){
        CouponModel.getInstance().getCouponFromServer(oid, new OperationListener<Coupon>() {
            @Override
            public void done(Coupon coupon, AppException exe) {
                if (exe == null) {
                    //如果卡卷商品在手机本地数据库不存在
                    if(coupon.getProduct()==null){
                        toast("无法识别的优惠卷");
                        startActivity(MainActivity.class,null,true);
                    }
                    else{
                        if(coupon.getDeleted()){
                            toast(getResources().getString(R.string.text_coupon_is_deleded));
                            startActivity(MainActivity.class,null,true);
                        }
                        else {
                            curCoupon = coupon;
                            showCoupon(curCoupon);
                        }
                    }
                } else {
                    toast(getResources().getString(ResourcesUtils.getStringId(getApplicationContext(),exe.getErrorCode())));
                    startActivity(MainActivity.class,null,true);
                }
            }
        });
    }

    /**
     * 根据获取的卡卷信息，显示卡卷图像
     * @param coupon
     */
    private void showCoupon(final Coupon coupon){
        try {
            Product product=coupon.getProduct();
            if(product!=null) {
                tv_title.setText(product.getTitle());
                tv_merchant.setText(product.getMerchant().getName());
                tv_number.setText(coupon.getCid());
                final String expired = (TextUtils.isEmpty(coupon.getStartTimeLocal()) ? "" : coupon.getStartTimeLocal()) + (TextUtils.isEmpty(coupon.getEndTimeLocal()) ? "" : " - " + coupon.getEndTimeLocal());
                tv_expired.setText(expired);
                tv_price.setText(product.getPriceStr());
                tv_shortdesc.setText(product.getShortDesc());
                CouponStyle  couponStyle= product.getCouponStyle();
                if(couponStyle!=null) {
                    //设置背景色
                    gl_coupon_top.setBackgroundColor(Color.parseColor(couponStyle.getBgColor()));
                    //加载底纹
                    Picasso.with(this).load(couponStyle.getShadingUrl()).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(iv_coupon_shading);
                    //加载商家logo
                    Picasso.with(this).load(couponStyle.getLogoUrl()).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(iv_coupon_logo);
                    //加载自定义图片
                    Picasso.with(this).load(couponStyle.getPictureUrl()).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(iv_coupon_img);

                    if (!TextUtils.isEmpty(couponStyle.getBenefitOne())) {
                        key=1;
                        tv_benefit.setText(R.string.coupon_benefit_onetime);
                        tv_benefit_value.setText(couponStyle.getBenefitOne());
                        tv_add.setText(getResources().getString(R.string.coupon_one_redeemed));
                        tv_reduce.setVisibility(View.GONE);
                    } else if (!TextUtils.isEmpty(couponStyle.getBenefitPrepaidCash())) {
                        key=2;
                        tv_benefit.setText(R.string.coupon_benefit_precash);
                        tv_benefit_value.setText(couponStyle.getBenefitPrepaidCash()+"/"+coupon.getCustomValues().get("2"));
                        tv_add.setText(getResources().getString(R.string.coupon_cash_add));
                        tv_reduce.setText(getResources().getString(R.string.coupon_cash_deduct));
                    } else if (!TextUtils.isEmpty(couponStyle.getBenefitPrepaidService())) {
                        key=3;
                        tv_benefit.setText(R.string.coupon_benefit_preservice);
                        tv_benefit_value.setText(couponStyle.getBenefitPrepaidService()+"/"+coupon.getCustomValues().get("3"));
                        tv_add.setText(getResources().getString(R.string.coupon_service_add));
                        tv_reduce.setText(getResources().getString(R.string.coupon_service_deduct));
                    } else if (!TextUtils.isEmpty(couponStyle.getBenefitBuyNGetOne())) {
                        key=4;
                        tv_benefit.setText(R.string.coupon_benefit_buyngetone);
                        tv_benefit_value.setText(couponStyle.getBenefitBuyNGetOne()+"/"+coupon.getCustomValues().get("4"));
                        tv_add.setText(getResources().getString(R.string.coupon_buy_add));
                        tv_reduce.setText(getResources().getString(R.string.coupon_buy_deduct));
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

                cashAmount=curCoupon.getCustomValues().get("2");
                serviceTime=curCoupon.getCustomValues().get("3");
                buyTime=curCoupon.getCustomValues().get("4");
                UserModel.getInstance().searchUserById(coupon.getUserId(), new OperationListener<User>() {
                    @Override
                    public void done(User obj, AppException exception) {
                        if(obj!=null) {
                            tv_firstname.setText(obj.getFirstName());
                            tv_lastname.setText(obj.getLastName());
                            tv_phone.setText(obj.getMobile());
                            tv_points.setText(obj.getPoints()+"");
                            Picasso.with(ScanCouponActivity.this).load(obj.getAvatarUrl()).placeholder(R.mipmap.icon_account).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(iv_avatar);
                        }
                    }
                });

                gl_coupon_top.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ll_desc.isShown()) {
                            ll_desc.setVisibility(View.GONE);
                        } else {
                            ll_desc.setVisibility(View.VISIBLE);
                        }
                    }
                });

                tv_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(ScanCouponActivity.this, CouponUseActivity.class);
                        intent.putExtra("id",curCoupon.getProductId());
                        intent.putExtra("expired",expired);
                        intent.putExtra("cash", cashAmount);
                        intent.putExtra("service",serviceTime);
                        intent.putExtra("buy",buyTime);
                        startActivity(intent);
                    }
                });
            }

        }
        catch (Exception e){
            toast(e.getMessage());
        }

    }

    /**
     * 点击添加积分按钮
     */
    @OnClick(R.id.tv_add)
    public void onClickButtonAdd(){
        isAdd = true;
        CouponStyle  couponStyle= curCoupon.getCouponStyle();
        if(couponStyle!=null) {
            if (!TextUtils.isEmpty(couponStyle.getBenefitOne())) {
                CouponModel.getInstance().delCouponsFromServer(curCoupon.getOrderId().toString(), new OperationListener<Coupon>() {
                    @Override
                    public void done(Coupon obj, AppException exception) {
                        if(exception!=null){
                            toast(getResources().getString(R.string.text_operate_failed)+getResources().getString(ResourcesUtils.getStringId(ScanCouponActivity.this,exception.getErrorCode())) );
                            rl_dialog.setVisibility(View.GONE);
                        }
                        else{
                            toast(getResources().getString(R.string.text_coupon_redeemed_success));
                            startActivity(MainActivity.class,null,true);
                        }
                    }
                });
            } else if (!TextUtils.isEmpty(couponStyle.getBenefitPrepaidCash())) {
                et_points.setHint(R.string.text_cash_amount_add);
                rl_dialog.setVisibility(View.VISIBLE);
            } else if (!TextUtils.isEmpty(couponStyle.getBenefitPrepaidService())) {
                et_points.setHint(R.string.text_service_time_add);
                rl_dialog.setVisibility(View.VISIBLE);
            } else if (!TextUtils.isEmpty(couponStyle.getBenefitBuyNGetOne())) {
                et_points.setHint(R.string.text_buy_time_add);
                rl_dialog.setVisibility(View.VISIBLE);
            }
        }

    }

    /**
     * 点击扣减积分按钮
     */
    @OnClick(R.id.tv_reduce)
    public void onClickButtonDeduct(){
        isAdd=false;
        et_points.setText("");
        CouponStyle  couponStyle= curCoupon.getCouponStyle();
        if(couponStyle!=null) {
            if (!TextUtils.isEmpty(couponStyle.getBenefitPrepaidCash())) {
                et_points.setHint(R.string.text_cash_amount_deduct);
            } else if (!TextUtils.isEmpty(couponStyle.getBenefitPrepaidService())) {
                et_points.setText("1");
                et_points.setHint(R.string.text_service_time_deduct);
            } else if (!TextUtils.isEmpty(couponStyle.getBenefitBuyNGetOne())) {
                et_points.setText("1");
                et_points.setHint(R.string.text_buy_time_deduct);
            }
            rl_dialog.setVisibility(View.VISIBLE);
        }

    }

    @OnClick(R.id.btn_confirm)
    public void onClickButtonOk(){
        //修改用户权益
        if(!TextUtils.isEmpty(et_points.getText().toString())) {
            int value=Integer.parseInt(et_points.getText().toString());
            if(value>0) {
                final int amount=isAdd?value:-value;
                CouponModel.getInstance().changeCouponsBenefit(curCoupon.getOrderId(),key+"",amount+"", new OperationListener<Coupon>() {
                    @Override
                    public void done(Coupon obj, AppException exception) {
                        if (exception!= null) {
                            toast(getResources().getString(R.string.text_operate_failed) + getResources().getString(ResourcesUtils.getStringId(getApplicationContext(), exception.getErrorCode())));
                        }
                        else{
                            CouponStyle  couponStyle= curCoupon.getCouponStyle();
                            if (!TextUtils.isEmpty(couponStyle.getBenefitPrepaidCash())) {
                                cashAmount=(Float.parseFloat(cashAmount)+amount)+"";
                                tv_benefit_value.setText(couponStyle.getBenefitPrepaidCash()+"/"+cashAmount);
                                if(isAdd) {
                                    toast(getResources().getString(R.string.text_coupon_add_cash_success));
                                }
                                else{
                                    toast(getResources().getString(R.string.text_coupon_deduct_cash_success));
                                }
                            } else if (!TextUtils.isEmpty(couponStyle.getBenefitPrepaidService())) {
                                serviceTime=(Integer.parseInt(serviceTime)+amount)+"";
                                tv_benefit_value.setText(couponStyle.getBenefitPrepaidService()+"/"+serviceTime);
                                if(isAdd) {
                                    toast(getResources().getString(R.string.text_coupon_add_service_success));
                                }
                                else{
                                    toast(getResources().getString(R.string.text_coupon_deduct_service_success));
                                }
                            } else if (!TextUtils.isEmpty(couponStyle.getBenefitBuyNGetOne())) {
                                buyTime=(Integer.parseInt(buyTime)+amount)+"";
                                tv_benefit_value.setText(couponStyle.getBenefitBuyNGetOne()+"/"+buyTime);
                                if(isAdd) {
                                    toast(getResources().getString(R.string.text_coupon_add_buy_success));
                                }
                                else{
                                    toast(getResources().getString(R.string.text_coupon_deduct_buy_success));
                                }
                            }

                        }
                        rl_dialog.setVisibility(View.GONE);
                    }
                });
            }
            else{
                toast("请输入大于0的整数!");
                return;
            }
        }
    }

    /**
     * 点击空白区域取消对话框
     */
    @OnClick(R.id.rl_dialog)
    public void onCloseDialog(){
        rl_dialog.setVisibility(View.GONE);
        btn_confirm.setText(R.string.button_ok);
    }

}
