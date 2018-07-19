package com.campray.lesswalletmerchant.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.campray.lesswalletmerchant.LessWalletApplication;
import com.campray.lesswalletmerchant.R;
import com.campray.lesswalletmerchant.db.entity.Coupon;
import com.campray.lesswalletmerchant.db.entity.Friend;
import com.campray.lesswalletmerchant.db.entity.Merchant;
import com.campray.lesswalletmerchant.db.entity.Product;
import com.campray.lesswalletmerchant.db.entity.User;
import com.campray.lesswalletmerchant.event.LoginedEvent;
import com.campray.lesswalletmerchant.listener.OperationListener;
import com.campray.lesswalletmerchant.model.CouponModel;
import com.campray.lesswalletmerchant.model.FriendModel;
import com.campray.lesswalletmerchant.model.MerchantModel;
import com.campray.lesswalletmerchant.model.ProductModel;
import com.campray.lesswalletmerchant.model.UserModel;
import com.campray.lesswalletmerchant.ui.base.BaseActivity;
import com.campray.lesswalletmerchant.util.AppException;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**登陆界面
 * @author :Phills
 * @project:LoginActivity
 * @date :2016-01-15-18:23
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_username)
    EditText et_username;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.cb_remember)
    CheckBox cb_remember;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        User userBean=LessWalletApplication.INSTANCE().getAccount();
        if(userBean!=null){
            et_username.setText(userBean.getUserName());
        }
    }

    @OnClick(R.id.btn_login)
    public void onLoginClick(View view){
        UserModel.getInstance().login(et_username.getText().toString(), et_password.getText().toString(), cb_remember.isChecked(),new OperationListener<User>() {
            @Override
            public void done(User user, AppException exe) {
                if (exe == null) {
                    LessWalletApplication.INSTANCE().setAccount(user);
                    MerchantModel.getInstance().getMerchantFromServer(user.getVenderId(), new OperationListener<Merchant>() {
                        @Override
                        public void done(Merchant obj, AppException exception) {}
                    });

                    startActivity(MainActivity.class, null, true);
                } else {
                    toast(exe.toString(getApplicationContext()));
                }
            }
        });
    }

}
