package com.campray.lesswalletmerchant.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import com.campray.lesswalletmerchant.R;
import com.campray.lesswalletmerchant.adapter.base.BaseRecyclerAdapter;
import com.campray.lesswalletmerchant.adapter.base.BaseRecyclerHolder;
import com.campray.lesswalletmerchant.db.entity.CouponStyle;
import com.campray.lesswalletmerchant.db.entity.Product;
import com.campray.lesswalletmerchant.ui.ProductDetailActivity;
import com.campray.lesswalletmerchant.ui.QRCodeCaptureActivity;

import java.util.Collection;


/**
 * 使用进一步封装的Coupon
 * @author Phills
 */
public class ProductAdapter extends BaseRecyclerAdapter<Product> {
    private final static int SCANNIN_USER_CODE = 113;//赠送卡卷时扫描用户二维码

    public ProductAdapter(Context context, int layoutResourceId, Collection<Product> datas) {
        super(context,layoutResourceId,datas);
    }

    /**
     * 根据指定卡卷类型和卡卷id，获取卡卷在列表中的位置
     * @param targetId
     * @return
     */
    public int findPosition(String targetId) {
        int index = this.getCount();
        int position = -1;
        while(index-- > 0) {
            if((getItem(index)).getProductId().equals(targetId)) {
                position = index;
                break;
            }
        }
        return position;
    }

    /**
     * 绑定显示卡卷信息到列表view对象
     * @param holder
     * @param product
     * @param position
     */
    @Override
    public void bindView(final BaseRecyclerHolder holder, final Product product, final int position) {
        if (product != null) {
            int productType = product.getProductTypeId();

            String expired = (TextUtils.isEmpty(product.getStartTimeLocal()) ? "" : product.getStartTimeLocal()) + (TextUtils.isEmpty(product.getEndTimeLocal()) ? "" : " - " + product.getEndTimeLocal());
            holder.setText(R.id.tv_title, product.getTitle());
            holder.setText(R.id.tv_expired, expired);
            holder.setText(R.id.tv_number, product.getStockQuantity()+"");
            holder.setText(R.id.tv_price, product.getPriceStr());
            holder.setText(R.id.tv_shortdesc, product.getShortDesc());

            CouponStyle couponStyle = product.getCouponStyle();
            if (couponStyle != null) {
                if(couponStyle.getValidityDay()>0) {
                    holder.setText(R.id.tv_validity, couponStyle.getValidityDay()+this.context.getResources().getString(R.string.coupon_validity_day) );
                }else if(couponStyle.getValidityMonth()>0) {
                    holder.setText(R.id.tv_validity, couponStyle.getValidityMonth()+this.context.getResources().getString(R.string.coupon_validity_month) );
                }else if(couponStyle.getValidityYear()>0) {
                    holder.setText(R.id.tv_validity, couponStyle.getValidityYear()+this.context.getResources().getString(R.string.coupon_validity_year) );
                }
                else{
                    holder.setText(R.id.tv_validity, this.context.getResources().getString(R.string.coupon_validity_nolimit) );
                }

                if (productType == 1) {
                    holder.setBackgroundColor(R.id.gl_coupon_top, couponStyle.getBgColor());
                    if(!TextUtils.isEmpty(couponStyle.getBenefitOne())) {
                        holder.setText(R.id.tv_benefit, R.string.coupon_benefit_onetime);
                        holder.setText(R.id.tv_benefit_value, couponStyle.getBenefitOne());
                    }else if(!TextUtils.isEmpty(couponStyle.getBenefitPrepaidCash())) {
                        holder.setText(R.id.tv_benefit, R.string.coupon_benefit_precash);
                        holder.setText(R.id.tv_benefit_value, couponStyle.getBenefitPrepaidCash());
                    }else if(!TextUtils.isEmpty(couponStyle.getBenefitPrepaidService())) {
                        holder.setText(R.id.tv_benefit, R.string.coupon_benefit_preservice);
                        holder.setText(R.id.tv_benefit_value, couponStyle.getBenefitPrepaidService());
                    }else if(!TextUtils.isEmpty(couponStyle.getBenefitBuyNGetOne())) {
                        holder.setText(R.id.tv_benefit, R.string.coupon_benefit_buyngetone);
                        holder.setText(R.id.tv_benefit_value, couponStyle.getBenefitBuyNGetOne());
                    }


                    if (!TextUtils.isEmpty(couponStyle.getShadingUrl())) {
                        holder.setImageView(R.id.iv_coupon_shading, couponStyle.getShadingUrl(), 0);
                    } else {
                        holder.setVisible(R.id.iv_coupon_shading, View.GONE);
                    }
                    if (!TextUtils.isEmpty(couponStyle.getPictureUrl())) {
                        holder.setImageView(R.id.iv_coupon_img, couponStyle.getPictureUrl(), 0);
                    } else {
                        holder.setVisible(R.id.iv_coupon_img, View.GONE);
                    }
                    if (!TextUtils.isEmpty(couponStyle.getLogoUrl())) {
                        holder.setImageView(R.id.iv_coupon_logo, couponStyle.getLogoUrl(), 0);
                    } else {
                        holder.setVisible(R.id.iv_coupon_logo, View.GONE);
                    }

                } else if (productType == 3) {
                    //设置背景色
                    View view = holder.getView(R.id.rl_card_top);
                    GradientDrawable gd = (GradientDrawable) view.getBackground();
                    gd.setColor(Color.parseColor(couponStyle.getBgColor()));

                    View backview = holder.getView(R.id.rl_card_back);
                    GradientDrawable backgd = (GradientDrawable) backview.getBackground();
                    backgd.setColor(Color.parseColor("#f2e4cf"));

                    if (!TextUtils.isEmpty(couponStyle.getShadingUrl())) {
                        holder.setImageView(R.id.iv_card_shading, couponStyle.getShadingUrl(), 0);
                    } else {
                        holder.setVisible(R.id.iv_card_shading, View.GONE);
                    }
                    if (!TextUtils.isEmpty(couponStyle.getPictureUrl())) {
                        holder.setImageView(R.id.iv_card_img, couponStyle.getPictureUrl(), 0);
                    } else {
                        holder.setVisible(R.id.iv_card_img, View.GONE);
                    }
                    if (!TextUtils.isEmpty(couponStyle.getLogoUrl())) {
                        holder.setImageView(R.id.iv_card_logo, couponStyle.getLogoUrl(), 0);
                    } else {
                        holder.setVisible(R.id.iv_card_logo, View.GONE);
                    }
                }
            }

            //设置送出事件
            holder.setOnClickListener(R.id.tv_give, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    //intent.setClass(ProductAdapter.this.context, CouponSendActivity.class);
                    intent.setClass(ProductAdapter.this.context, QRCodeCaptureActivity.class);
                    intent.putExtra("product_id", product.getProductId());
                    ((FragmentActivity) ProductAdapter.this.context).startActivityForResult(intent, SCANNIN_USER_CODE);
                }
            });

            if (productType == 1) {//coupon的对象点击事件,展开详情
                holder.setOnClickListener(R.id.gl_coupon_top, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(product.getStockQuantity()<=0){
                            toast(ProductAdapter.this.context.getResources().getString(R.string.E_3021));
                        }
                        else {
                            holder.setVisibleToggle(R.id.ll_desc);
                        }
                    }
                });

                //点击查看Coupon详情按钮
                holder.setOnClickListener(R.id.tv_more, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(ProductAdapter.this.context, ProductDetailActivity.class);
                        intent.putExtra("id",product.getProductId());
                        ProductAdapter.this.context.startActivity(intent);
                    }
                });
            }
            else if(productType == 3){
                holder.setText(R.id.tv_merchant, product.getMerchant().getName());
                //coard的对象点击事件,处理实现翻转动画
                holder.setOnClickListener(R.id.ll_card_front, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(product.getStockQuantity()<=0){
                            toast(ProductAdapter.this.context.getResources().getString(R.string.E_3022));
                        }
                        else {
                            LinearLayout cardBack = (LinearLayout) holder.getView(R.id.ll_card_back);
                            flipAnimatorXViewShow(v, cardBack, 200);
                        }
                    }
                });
                holder.setOnClickListener(R.id.ll_card_back, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout cardFront=(LinearLayout)holder.getView(R.id.ll_card_front);
                        flipAnimatorXViewShow(v,cardFront,200);
                    }
                });

                //点击使用详情按钮
                holder.setOnClickListener(R.id.tv_more, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(ProductAdapter.this.context, ProductDetailActivity.class);
                        intent.putExtra("id",product.getProductId());
                        ProductAdapter.this.context.startActivity(intent);
                    }
                });

            }
        }
    }

    /**
     * 翻转动画
     * @param oldView
     * @param newView
     * @param time
     */
    private void flipAnimatorXViewShow(final View oldView, final View newView, final long time) {

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(oldView, "rotationY", 0, 90);
        final ObjectAnimator animator2 = ObjectAnimator.ofFloat(newView, "rotationY", -90, 0);

        animator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                setCameraDistance(oldView);
                setCameraDistance(newView);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                oldView.setVisibility(View.GONE);
                animator2.setDuration(time).start();
                newView.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        animator1.setDuration(time).start();
    }

    /**
     * 改变视角距离, 要不然翻转时视图会拉伸很长
     */
    private void setCameraDistance(View view) {
        int distance = 16000;
        float scale = this.context.getResources().getDisplayMetrics().density * distance;
        view.setCameraDistance(scale);
    }


}