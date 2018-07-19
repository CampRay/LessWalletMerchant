package com.campray.lesswalletmerchant.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.campray.lesswalletmerchant.LessWalletApplication;
import com.campray.lesswalletmerchant.R;
import com.campray.lesswalletmerchant.db.entity.Currency;
import com.campray.lesswalletmerchant.db.entity.User;
import com.campray.lesswalletmerchant.listener.OperationListener;
import com.campray.lesswalletmerchant.model.CurrencyModel;
import com.campray.lesswalletmerchant.model.UserModel;
import com.campray.lesswalletmerchant.ui.base.MenuActivity;
import com.campray.lesswalletmerchant.util.AppException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 二维码扫码支付页面
 * @author :Phills
 * @project:RegisterActivity
 * @date :2018-04-25-18:23
 */
public class CollectPaymentActivity extends MenuActivity {
    @BindView(R.id.tv_amount)
    TextView tv_amount;
    @BindView(R.id.tv_collecter)
    TextView tv_collecter;
    @BindView(R.id.tv_amount_label)
    TextView tv_amount_label;

    @BindView(R.id.ll_input)
    LinearLayout ll_input;

    @BindView(R.id.et_amount)
    EditText et_amount;
    @BindView(R.id.sp_currency)
    Spinner sp_currency;

    //货币选择框显示数据集合
    private List<String> displayDataList=new ArrayList<String>();
    private List<Currency> currencyList=new ArrayList<Currency>();
    //设置货币下拉列表数据源
    private void setCurrencyDisplayData() {
        currencyList= CurrencyModel.getInstance().getAllCurrencies();
        if(currencyList==null) {
            currencyList.add(new Currency(1L,"HK Dollar","HKD","1","HK$",1,false));
        }
        for (Currency bean : currencyList) {
            displayDataList.add(bean.getName());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_payment);
        long uid = this.getBundle().getLong("uid");
        String currencyCode = this.getBundle().getString("currency");
        String amount = this.getBundle().getString("amount");
        UserModel.getInstance().searchUserById(uid,new OperationListener<User>() {
            @Override
            public void done(User user, AppException exe) {
                if (exe == null) {
                    if(user!=null){
                        tv_collecter.setText(user.getFullName());
                    }
                } else {
                    toast(exe.toString(getApplicationContext()));
                }
            }
        });

        if(!TextUtils.isEmpty(currencyCode)&&!TextUtils.isEmpty(amount)){
            Currency currency=CurrencyModel.getInstance().getCurrencyByCode(currencyCode);
            String fmtStr="%s";
            if(!TextUtils.isEmpty(currency.getCustomFormatting())){
                fmtStr=currency.getCustomFormatting().replace("0.00","%.2f");
            }
            tv_amount.setText(String.format(fmtStr,amount));
        }
        else{
            ll_input.setVisibility(View.VISIBLE);
            tv_amount_label.setVisibility(View.GONE);
            tv_amount.setVisibility(View.GONE);
        }

        //设置货币列表
        setCurrencyDisplayData();
        // 声明一个ArrayAdapter用于存放简单数据
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_spinner_text,
                displayDataList);
        sp_currency.setAdapter(adapter);
        Currency userCurrency=LessWalletApplication.INSTANCE().getAccount().getCurrenty();
        if(userCurrency!=null) {
            //设置默认使用的货币
            for (int i = 0; i < currencyList.size(); i++) {
                Currency item = currencyList.get(i);
                if (userCurrency.getCurrencyCode().equals(item.getCurrencyCode())) {
                    sp_currency.setSelection(i);
                    break;
                }
            }
        }

    }


    /**
     * 点击设置收款金额对话框的确认按钮
     * @param view
     */
    @OnClick(R.id.btn_yes)
    public void onConfirmClick(View view){
        this.startActivity(MainActivity.class,null,true);
    }

    /**
     * 点击取消按钮
     * @param view
     */
    @OnClick(R.id.btn_no)
    public void onCancelClick(View view){
        this.startActivity(MainActivity.class,null,true);
    }
}
