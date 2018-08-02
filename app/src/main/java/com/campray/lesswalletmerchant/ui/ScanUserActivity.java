package com.campray.lesswalletmerchant.ui;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.campray.lesswalletmerchant.R;
import com.campray.lesswalletmerchant.db.entity.Coupon;
import com.campray.lesswalletmerchant.db.entity.CouponStyle;
import com.campray.lesswalletmerchant.db.entity.Product;
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

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 扫描卡卷二维码后页面
 * Created by Phills on 11/2/2017.
 */

public class ScanUserActivity extends MenuActivity {
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
    @BindView(R.id.tv_expired)
    TextView tv_expired;
    @BindView(R.id.tv_cash)
    TextView tv_cash;

    @BindView(R.id.et_points)
    EditText et_points;
    @BindView(R.id.btn_confirm)
    Button btn_confirm;



    @BindView(R.id.rl_dialog)
    RelativeLayout rl_dialog;

    @BindView(R.id.ll_card_layout)
    LinearLayout ll_card_layout;

    private Coupon curCoupon=null;
    private int curPoints=0;
    private float curCash=0f;
    private boolean isAdd=false;
    private boolean isCase=false;
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
     * 从服务器获取下载用户
     * @param uid
     */
    private void getUserFromServer(long uid){
        UserModel.getInstance().searchUserById(uid, new OperationListener<User>() {
            @Override
            public void done(User user, AppException exe) {
                if (exe == null) {
                    //如果卡卷商品在手机本地数据库不存在
                    if(user==null){
                        toast("无法识别的用户");
                        finish();
                    }
                    else{
                        curCoupon=null;
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
                        finish();
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
            if(card!=null) {
                Product product = card.getProduct();

                expired = (TextUtils.isEmpty(product.getStartTimeLocal()) ? "" : product.getStartTimeLocal()) + (TextUtils.isEmpty(product.getEndTimeLocal()) ? "" : " - " + product.getEndTimeLocal());
                tv_expired.setText(expired);
                CouponStyle style = card.getCouponStyle();
                //设置背景色
                GradientDrawable gd = (GradientDrawable) rl_card_top.getBackground();
                gd.setColor(Color.parseColor(style.getBgColor()));
                //加载底纹
                Picasso.with(this).load(style.getShadingUrl()).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(iv_card_shading);
                //加载商家logo
                Picasso.with(this).load(style.getLogoUrl()).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(iv_card_logo);
                //加载自定义图片
                Picasso.with(this).load(style.getPictureUrl()).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(iv_card_img);

                tv_title.setText(product.getTitle());
                tv_merchant.setText(product.getMerchant().getName());
                tv_number.setText("no." + card.getProduct().getNumPrefix() + card.getCid());

                UserModel.getInstance().searchUserById(card.getUserId(), new OperationListener<User>() {
                    @Override
                    public void done(User obj, AppException exception) {
                        tv_firstname.setText(obj.getFirstName());
                        tv_lastname.setText(obj.getLastName());
                        tv_phone.setText(obj.getMobile());
                        tv_points.setText(obj.getPoints());
                        curPoints = obj.getPoints();
                        Picasso.with(ScanUserActivity.this).load(obj.getAvatarUrl()).placeholder(R.mipmap.icon_account).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(iv_avatar);
                    }
                });
            }
            else{
                ll_card_layout.setVisibility(View.GONE);
            }

        }
        catch (Exception e){
            toast(e.getMessage());
        }

    }

    /**
     * 点击Card图片打开详情页面
     */
    @OnClick(R.id.layout_coupon)
    public void onClickCard(){
        Bundle bundle=new Bundle();
        bundle.putLong("id",curCoupon.getOrderId());
        bundle.putString("expired",expired);
        startActivity(CardUseActivity.class,bundle,false);
    }

    /**
     * 点击添加积分按钮
     */
    @OnClick(R.id.tv_add_point)
    public void onClickAddPoint(){
        isAdd=true;
        et_points.setText("");
        et_points.setHint(R.string.text_point_amount_add);
        btn_confirm.setText(R.string.card_point_add);
        rl_dialog.setVisibility(View.VISIBLE);
    }

    /**
     * 点击扣减积分按钮
     */
    @OnClick(R.id.tv_reduce_point)
    public void onClickDeductPoint(){
        isAdd=false;
        et_points.setText("");
        et_points.setHint(R.string.text_point_amount_deduct);
        btn_confirm.setText(R.string.card_point_deduct);
        rl_dialog.setVisibility(View.VISIBLE);
    }

    /**
     * 点击充值金额按钮
     */
    @OnClick(R.id.tv_add_cash)
    public void onClickAddCash(){
        isAdd=true;
        isCase=true;
        et_points.setText("");
        et_points.setHint(R.string.text_cash_amount_add);
        btn_confirm.setText(R.string.card_cash_add);
        rl_dialog.setVisibility(View.VISIBLE);
    }

    /**
     * 点击扣减余额按钮
     */
    @OnClick(R.id.tv_reduce_cash)
    public void onClickDeductCash(){
        isAdd=false;
        isCase=true;
        et_points.setText("");
        et_points.setHint(R.string.text_cash_amount_deduct);
        btn_confirm.setText(R.string.card_cash_deduct);
        rl_dialog.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_confirm)
    public void onClickButtonOk(){
        //修改用户积分
        if(!TextUtils.isEmpty(et_points.getText().toString())) {
            int amount =0;
            try {
                amount = Integer.parseInt(et_points.getText().toString());
            }
            catch (Exception e){
                toast("请输入大于0的整数!");
                return;
            }
            if(amount>0) {
                final int amountVal=isAdd?amount:-amount;
                if(isCase){
                    UserModel.getInstance().updateUserCash(curCoupon.getUserId(), amountVal, "", new OperationListener<User>() {
                        @Override
                        public void done(User user, AppException exception) {
                            if (exception == null) {
                                curCash +=amountVal;
                                tv_cash.setText(curCash + "");
                                rl_dialog.setVisibility(View.GONE);
                                if (isAdd) {
                                    toast("赠送金额成功!");
                                } else {
                                    toast("扣减金额成功!");
                                }
                            } else {
                                toast("修改金额操作失败，错误原因: " + getResources().getString(ResourcesUtils.getStringId(getApplicationContext(), exception.getErrorCode())));
                            }
                        }
                    });
                }
                else {
                    UserModel.getInstance().updateUserPoints(curCoupon.getUserId(), amountVal, "", new OperationListener<User>() {
                        @Override
                        public void done(User user, AppException exception) {
                            if (exception == null) {
                                curPoints = curPoints + amountVal;
                                tv_points.setText(curPoints + "");
                                rl_dialog.setVisibility(View.GONE);
                                if (isAdd) {
                                    toast("赠送积分成功!");
                                } else {
                                    toast("扣减积分成功!");
                                }
                            } else {
                                toast("修改积分操作失败，错误原因: " + getResources().getString(ResourcesUtils.getStringId(getApplicationContext(), exception.getErrorCode())));
                            }
                        }
                    });
                }
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
