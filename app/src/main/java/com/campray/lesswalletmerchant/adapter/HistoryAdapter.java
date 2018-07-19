package com.campray.lesswalletmerchant.adapter;


import android.content.Context;
import android.content.DialogInterface;

import com.campray.lesswalletmerchant.LessWalletApplication;
import com.campray.lesswalletmerchant.R;
import com.campray.lesswalletmerchant.adapter.base.BaseRecyclerAdapter;
import com.campray.lesswalletmerchant.adapter.base.BaseRecyclerHolder;
import com.campray.lesswalletmerchant.adapter.base.IMutlipleItem;
import com.campray.lesswalletmerchant.db.entity.History;
import com.campray.lesswalletmerchant.db.entity.User;
import com.campray.lesswalletmerchant.model.HistoryModel;
import com.campray.lesswalletmerchant.util.ResourcesUtils;
import com.campray.lesswalletmerchant.view.CustomDialog;

import java.util.Collection;


/**
 * 列表控件的数据适配器类(历史消息)
 * @author Phills
 */
public class HistoryAdapter extends BaseRecyclerAdapter<History> {
    public HistoryAdapter(Context context, IMutlipleItem<History> items, Collection<History> datas) {
        super(context,items,datas);
    }

    /**
     * 根据指定消息id，获取列表中对应消息的位置
     * @param targetId
     * @return
     */
    public int findPosition(String targetId) {
        int index = this.getCount();
        int position = -1;
        while(index-- > 0) {
            if((getItem(index)).getId().equals(targetId)) {
                position = index;
                break;
            }
        }
        return position;
    }

    /**
     * 绑定显示消息到列表view对象
     * @param holder
     * @param history
     * @param position
     */
    @Override
    public void bindView(final BaseRecyclerHolder holder, History history, final int position) {
        if (history != null) {
            String title = "";
            String desc ="";
            User user=LessWalletApplication.INSTANCE().getAccount();
            String fmt=user.getCurrenty().getCustomFormatting().replace("0.00","");
            String[] strArr=history.getComment().split(":");
            switch (history.getType()) {
                case 139://发送Coupon给用户成功消息
                    title = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_merchant_coupon_sent"));
                    desc = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_merchant_coupon_sent_text"));
                    break;
                case 140://收回Coupon的消息
                    title = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_merchant_coupon_redeemed"));
                    desc = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_merchant_coupon_redeemed_text"));
                    break;
                case 141://扣减积分成功的消息
                    title = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_merchant_point_redeemed"));
                    desc = String.format(this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_merchant_point_redeemed_text")),strArr[1].replace("-",""));

                    break;
                case 142://送积分给用户成功消息
                    title = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_merchant_point_added"));
                    desc = String.format(this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_merchant_point_added_text")),strArr[1]);
                    break;
                case 143://扣减金额成功的消息
                    title = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_merchant_cash_redeemed"));
                    desc = String.format(this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_merchant_cash_redeemed_text")),fmt+strArr[2].replace("-",""));

                    break;
                case 144://送金额给用户成功消息
                    title = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_merchant_cash_added"));
                    desc = String.format(this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_merchant_cash_added_text")),fmt+strArr[2]);
                    break;
                case 145://发送card给用户成功消息
                    title = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_merchant_card_sent"));
                    desc = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_merchant_card_sent_text"));
                    break;
                case 146://添加服务次数成功的消息
                    title = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_merchant_service_added"));
                    desc = String.format(this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_merchant_service_added_text")),strArr[2]);

                    break;
                case 147://扣减服务次数成功消息
                    title = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_merchant_service_redeemed"));
                    desc = String.format(this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_merchant_service_redeemed_text")),strArr[2].replace("-",""));
                    break;
                case 148://添加购买次数成功的消息
                    title = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_merchant_buy_added"));
                    desc = String.format(this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_merchant_buy_added_text")),strArr[2]);

                    break;
                case 149://扣减购买次数成功消息
                    title = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_merchant_buy_deduct"));
                    desc = String.format(this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_merchant_buy_deduct_text")),strArr[2].replace("-",""));
                    break;
                default:
                    title = "";
            }
            holder.setText(R.id.tv_title, title);
            holder.setText(R.id.tv_desc, desc);
            holder.setText(R.id.tv_time, history.getCreatedTimeLocal());

        }
    }

    /**
     * 显示移除对话框
     * @param holder
     * @param position
     */
    private void showDialog(final BaseRecyclerHolder holder,final int position){
        final CustomDialog.Builder normalDialog =
                new CustomDialog.Builder(HistoryAdapter.this.context);
        normalDialog.setIcon(R.mipmap.face_sad);
        //normalDialog.setTitle("Title");
        normalDialog.setMessage("Do you want to remove this message?");
        normalDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        final History delHistory=HistoryAdapter.this.getItem(position);
                        HistoryModel.getInstance().delHistory(delHistory);
                    }
                });
        normalDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        // 显示
        normalDialog.show();
    }

}