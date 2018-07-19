package com.campray.lesswalletmerchant.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
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
import com.campray.lesswalletmerchant.db.entity.Coupon;
import com.campray.lesswalletmerchant.db.entity.CouponStyle;
import com.campray.lesswalletmerchant.db.entity.Currency;
import com.campray.lesswalletmerchant.db.entity.Product;
import com.campray.lesswalletmerchant.db.entity.SpecAttr;
import com.campray.lesswalletmerchant.db.entity.User;
import com.campray.lesswalletmerchant.db.service.ProductDaoService;
import com.campray.lesswalletmerchant.listener.OperationListener;
import com.campray.lesswalletmerchant.model.CouponModel;
import com.campray.lesswalletmerchant.model.CurrencyModel;
import com.campray.lesswalletmerchant.model.ProductModel;
import com.campray.lesswalletmerchant.model.UserModel;
import com.campray.lesswalletmerchant.qrcode.encode.QRCodeEncoder;
import com.campray.lesswalletmerchant.ui.base.MenuActivity;
import com.campray.lesswalletmerchant.util.AppException;
import com.campray.lesswalletmerchant.util.ImageUtil;
import com.campray.lesswalletmerchant.util.ResourcesUtils;
import com.campray.lesswalletmerchant.view.RoundImageView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 扫描卡卷二维码后页面
 * Created by Phills on 11/2/2017.
 */

public class ScanCardActivity extends MenuActivity {
    @BindView(R.id.rl_card_top)
    RelativeLayout rl_card_top;
    @BindView(R.id.iv_card_shading)
    RoundImageView iv_card_shading;
    @BindView(R.id.iv_card_img)
    RoundImageView iv_card_img;
    @BindView(R.id.iv_card_logo)
    ImageView iv_card_logo;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_number)
    TextView tv_number;
    @BindView(R.id.tv_merchant)
    TextView tv_merchant;

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
    private int curPoints=0;
    private boolean isAdd=false;
    private String expired;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_card);
        long oid = this.getBundle().getLong("id");
        //根据扫码获取的ID，从服务器获取对应的电子卡卷信息
        getCardFromServer(oid);
    }

    /**
     * 从服务器获取下载Card
     * @param oid
     */
    private void getCardFromServer(long oid){
        CouponModel.getInstance().getCouponFromServer(oid, new OperationListener<Coupon>() {
            @Override
            public void done(Coupon coupon, AppException exe) {
                if (exe == null) {
                    //如果卡卷商品在手机本地数据库不存在
                    if(coupon.getProduct()==null){
                        toast("无法识别的会员卡");
                        startActivity(MainActivity.class,null,true);
                    }
                    else{
                        curCoupon=coupon;
                        showCard(curCoupon);
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
     * @param card
     */
    private void showCard(final Coupon card){
        try {
            Product product=card.getProduct();

            expired = (TextUtils.isEmpty(card.getStartTimeLocal())?"":card.getStartTimeLocal()) + (TextUtils.isEmpty(card.getEndTimeLocal()) ? "" : " - " + card.getEndTimeLocal());
            CouponStyle style=card.getCouponStyle();
            //设置背景色
            GradientDrawable gd=(GradientDrawable)rl_card_top.getBackground();
            gd.setColor(Color.parseColor(style.getBgColor()));
            //加载底纹
            Picasso.with(this).load(style.getShadingUrl()).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(iv_card_shading);
            //加载商家logo
            Picasso.with(this).load(style.getLogoUrl()).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(iv_card_logo);
            //加载自定义图片
            Picasso.with(this).load(style.getPictureUrl()).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(iv_card_img);

            tv_title.setText(product.getTitle());
            tv_merchant.setText(product.getMerchant().getName());
            tv_number.setText(card.getCid());
            tv_price.setText(product.getPriceStr());

            UserModel.getInstance().searchUserById(card.getUserId(), new OperationListener<User>() {
                @Override
                public void done(User obj, AppException exception) {
                    if(obj!=null) {
                        tv_firstname.setText(obj.getFirstName());
                        tv_lastname.setText(obj.getLastName());
                        tv_phone.setText(obj.getMobile());
                        tv_points.setText(obj.getPoints()+"");
                        curPoints = obj.getPoints();
                        Picasso.with(ScanCardActivity.this).load(obj.getAvatarUrl()).placeholder(R.mipmap.icon_account).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(iv_avatar);
                    }
                }
            });



        }
        catch (Exception e){
            toast(e.getMessage());
        }

    }

    /**
     * 点击Card图片打开详情页面
     */
    @OnClick(R.id.ll_card_layout)
    public void onClickCard(){
        Intent intent = new Intent();
        intent.setClass(this, CardUseActivity.class);
        intent.putExtra("id",curCoupon.getProductId());
        intent.putExtra("expired",expired);
        intent.putExtra("cash", curCoupon.getCustomValues().get("2"));
        intent.putExtra("service",curCoupon.getCustomValues().get("3"));
        intent.putExtra("buy",curCoupon.getCustomValues().get("4"));
        startActivity(intent);
    }

    /**
     * 点击添加积分按钮
     */
    @OnClick(R.id.tv_add)
    public void onClickButtonAdd(){
        isAdd=true;
        et_points.setText("");
        et_points.setHint(R.string.text_point_amount_add);
        btn_confirm.setText(R.string.card_point_add);
        rl_dialog.setVisibility(View.VISIBLE);
    }

    /**
     * 点击扣减积分按钮
     */
    @OnClick(R.id.tv_reduce)
    public void onClickButtonDeduct(){
        isAdd=false;
        et_points.setText("");
        et_points.setHint(R.string.text_point_amount_deduct);
        btn_confirm.setText(R.string.card_point_deduct);
        rl_dialog.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_confirm)
    public void onClickButtonOk(){
        //修改用户积分
        if(!TextUtils.isEmpty(et_points.getText().toString())) {
            int points=Integer.parseInt(et_points.getText().toString());
            if(points>0) {
                final int pointsVal=isAdd?points:-points;
                UserModel.getInstance().updateUserPoints(curCoupon.getUserId(),pointsVal,"", new OperationListener<User>() {
                    @Override
                    public void done(User user, AppException exception) {
                        if (exception == null) {
                            curPoints=curPoints+pointsVal;
                            tv_points.setText(curPoints+"");
                            rl_dialog.setVisibility(View.GONE);
                            btn_confirm.setText(R.string.button_ok);
                            if(isAdd) {
                                toast("赠送积分成功!");
                            }
                            else{
                                toast("扣减积分成功!");
                            }
                        } else {
                            toast("修改积分操作失败，错误原因: " + getResources().getString(ResourcesUtils.getStringId(getApplicationContext(), exception.getErrorCode())));
                        }
                    }
                });
            }
            else{

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
