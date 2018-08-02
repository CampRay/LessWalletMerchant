package com.campray.lesswalletmerchant.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.campray.lesswalletmerchant.R;
import com.campray.lesswalletmerchant.adapter.HistoryAdapter;
import com.campray.lesswalletmerchant.adapter.base.BaseRecyclerAdapter;
import com.campray.lesswalletmerchant.adapter.listener.OnRecyclerViewListener;
import com.campray.lesswalletmerchant.db.entity.History;
import com.campray.lesswalletmerchant.event.MessageEvent;
import com.campray.lesswalletmerchant.model.HistoryModel;
import com.campray.lesswalletmerchant.ui.base.MenuActivity;
import com.campray.lesswalletmerchant.view.RecyclerViewItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Phills on 11/2/2017.
 */

public class MainActivity extends MenuActivity {
    @BindView(R.id.rc_message_list)
    RecyclerView rc_message_list;

    private BaseRecyclerAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    //当前列表显示的最后一个对象的位置索引
    private int lastPosition=0;
    //当前列表加载的数据页数
    private int lastPage=1;
    private int pageSize=10;

    /**
     * 注册消息接收事件，接收EventBus通過EventBus.getDefault().post(T)方法发出的事件通知，onMessageEvent(T)方法就会自动接收并处理些事件通知
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event){
        queryData(1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this.initPermissions();
        showMessageList();
        setListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        queryData(1);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * 显示消息列表
     */
    private void showMessageList(){
        adapter=new HistoryAdapter(this,R.layout.item_message,null);

        rc_message_list.setAdapter(adapter);
        linearLayoutManager=new LinearLayoutManager(this);
        //设置消息列表对象显示布局
        rc_message_list.setLayoutManager(linearLayoutManager);
        //设置分隔线
        rc_message_list.addItemDecoration( new RecyclerViewItemDecoration(this));
        //设置增加或删除条目的动画
        rc_message_list.setItemAnimator( new DefaultItemAnimator());

    }

    /**
     * 设置相关事件
     */
    private void setListener(){
        //设置RecyclerView滚动事件监听器
        rc_message_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            /**
             * 滚动状态变更事件
             * @param recyclerView
             * @param newState
             */
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //当滚动停止时
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

            /**
             * 列表滚动事件
             * @param recyclerView
             * @param dx horizontal distance scrolled in pixels
             * @param dy vertical distance scrolled in pixels
             */
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
            public boolean onItemLongClick(final int position) {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("提示")//设置对话框的标题
                        .setMessage("确定要删除此消息吗？")//设置对话框的内容
                        //设置对话框的按钮
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                History delHistory=(History)adapter.getItem(position);
                                HistoryModel.getInstance().delHistory(delHistory);
                                adapter.remove(position);
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
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
            rc_message_list.scrollToPosition(lastPosition+1);
        }
        //查询指定的某一页数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                //new Handler().post(new Runnable() {
                //同上面的new Handler().post()一样，都是在主线程运行，可以修改UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List messageList=HistoryModel.getInstance().getAllHistories(page,pageSize);
                        if(messageList!=null) {
                            if(page==1){
                                adapter.bindDatas(messageList);
                            }
                            else{
                                //adapter.removeFooter();
                                adapter.appendDatas(messageList);
                            }
                            lastPage=page;
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }).start();
    }

}
