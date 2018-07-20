package com.campray.lesswalletmerchant.ui;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.campray.lesswalletmerchant.LessWalletApplication;
import com.campray.lesswalletmerchant.R;
import com.campray.lesswalletmerchant.adapter.ProductAdapter;
import com.campray.lesswalletmerchant.adapter.base.BaseRecyclerAdapter;
import com.campray.lesswalletmerchant.adapter.listener.OnRecyclerViewListener;
import com.campray.lesswalletmerchant.db.entity.Product;
import com.campray.lesswalletmerchant.db.entity.User;
import com.campray.lesswalletmerchant.listener.OperationListener;
import com.campray.lesswalletmerchant.model.ProductModel;
import com.campray.lesswalletmerchant.ui.base.MenuActivity;
import com.campray.lesswalletmerchant.util.AppException;


import java.util.List;

import butterknife.BindView;

/**
 * 电子卡卷的页面Activity处理类
 * Created by Phills on 11/2/2017.
 */

public class CouponActivity extends MenuActivity {
    @BindView(R.id.rc_coupon_list)
    RecyclerView rc_coupon_list;
    //Coupon列表的适配器
    private BaseRecyclerAdapter adapter;
    private int typeId=1;
    private LinearLayoutManager linearLayoutManager;
    //当前列表显示的最后一个对象的位置索引
    private int lastPosition=0;
    //当前列表加载的数据页数
    private int lastPage=1;
    private int pageSize=10;
    private long vendorId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        typeId=this.getBundle().getInt("type_id");
        User user=LessWalletApplication.INSTANCE().getAccount();
        vendorId=user.getVenderId();
        showCouponList(typeId);
        setListener();
    }
    @Override
    public void onResume() {
        super.onResume();
        queryData(1);
    }

    /**
     * 根据电子卷的类型适配显示不同的电子卷列表
     */
    private void showCouponList(final int typeId){
        int resId=typeId==1?R.layout.item_coupon:R.layout.item_card;
        adapter = new ProductAdapter(this, resId, null);
        rc_coupon_list.setAdapter(adapter);
        linearLayoutManager=new LinearLayoutManager(this);
        rc_coupon_list.setLayoutManager(linearLayoutManager);
    }

    /**
     * 设置相关事件
     */
    private void setListener(){
        //设置RecyclerView滚动事件监听器
        rc_coupon_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    int count=adapter.getItemCount();
                    if(lastPosition+1==count){
                        int showPage=(int)Math.ceil((lastPosition+1)/pageSize)+1;
                        if(lastPage<showPage) {
                            queryData(showPage);
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastPosition=linearLayoutManager.findLastVisibleItemPosition();
            }
        });
        //设置列表适配器中自定义的事件监听器
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
            }

            @Override
            public boolean onItemLongClick(int position) {
                return true;
            }
        });

    }

    /**
     * 获取新数据,并刷新列表
     * @param page
     * @return
     */
    private void queryData(final int page){
        if(page>1) {
            //adapter.addFooter();
            rc_coupon_list.scrollToPosition(lastPosition+1);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                //new Handler().post(new Runnable() {
                //同上面的new Handler().post()一样，都是在主线程运行，可以修改UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ProductModel.getInstance().getAllProductsFromServer(vendorId,typeId,page-1,pageSize, new OperationListener<List<Product>>(){
                            @Override
                            public void done(List<Product> productList, AppException exception) {
                                if(productList!=null) {
                                    if(page==1){
                                        adapter.bindDatas(productList);
                                    }
                                    else{
                                        //adapter.removeFooter();
                                        adapter.appendDatas(productList);
                                    }
                                    lastPage=page;
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                });
            }
        }).start();


    }

}
