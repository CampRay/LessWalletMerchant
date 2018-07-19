package com.campray.lesswalletmerchant.ui.base;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.campray.lesswalletmerchant.LessWalletApplication;
import com.campray.lesswalletmerchant.R;

import com.campray.lesswalletmerchant.db.entity.Product;
import com.campray.lesswalletmerchant.db.entity.User;
import com.campray.lesswalletmerchant.model.ProductModel;
import com.campray.lesswalletmerchant.ui.AccountActivity;
import com.campray.lesswalletmerchant.ui.CardSendActivity;
import com.campray.lesswalletmerchant.ui.CouponActivity;
import com.campray.lesswalletmerchant.ui.CouponSendActivity;
import com.campray.lesswalletmerchant.ui.MainActivity;
import com.campray.lesswalletmerchant.ui.QRCodeCaptureActivity;
import com.campray.lesswalletmerchant.ui.ScanCardActivity;
import com.campray.lesswalletmerchant.ui.ScanCouponActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 有相同头部菜单页面的基类
 * @author :Phills
 * @project:BaseActivity
 * @date :2017-08-15 18:23
 */
public class MenuActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    private final static int TAKE_ALL_REQUEST_CODE=100;
    private final static int TAKE_CAMERA_REQUEST_CODE=200;

    private final static int SCANNIN_REQUEST_CODE = 111;//普通扫描
    private final static int SCANNIN_REQUEST_IMAGE = 112;//扫描图片
    private final static int SCANNIN_USER_CODE = 113;//赠送卡卷时扫描用户二维码

    @BindView(R.id.tv_username)
    TextView tv_username;


    /**
     * 点击扫描Home的事件方法
     * @param view
     */
    @OnClick(R.id.ib_home)
    public void onHomeClick(View view){
        startActivity(MainActivity.class,null,true);
    }

    /**
     * 点击我的按钮事件方法
     * @param view
     */
    @OnClick(R.id.ib_my)
    public void onMyClick(View view){
        startActivity(AccountActivity.class,null,false);
    }

    /**
     * 点击扫描按钮的事件方法
     * @param view
     */
    @OnClick(R.id.ib_scan)
    public void onScanClick(View view){
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            //如果有权限,打开扫码页面
            openQRcodeScanActivity();
        } else {
            EasyPermissions.requestPermissions(this, "扫码需要请求摄像头权限",
                    TAKE_CAMERA_REQUEST_CODE, Manifest.permission.CAMERA);
        }
    }

    /**
     * 点击coupon按钮的事件方法
     * @param view
     */
    @OnClick(R.id.ib_coupon)
    public void onCouponClick(View view){
        Bundle bundle=new Bundle();
        bundle.putInt("type_id", 1);
        startActivity(CouponActivity.class,bundle,false);
    }

    /**
     * 点击card按钮的事件方法
     * @param view
     */
    @OnClick(R.id.ib_card)
    public void onCardClick(View view){
        Bundle bundle=new Bundle();
        bundle.putInt("type_id", 3);
        startActivity(CouponActivity.class,bundle,false);
    }

    /**
     * 打开扫码页面
     */
    private void openQRcodeScanActivity(){
        Intent intent = new Intent(getApplication(), QRCodeCaptureActivity.class);
        startActivityForResult(intent, SCANNIN_REQUEST_CODE);
//        this.startActivity(QRCodeCaptureActivity.class,null,false);
    }

    /**
     * 上面的startActivityForResult方法调用后的返回结果处理方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != data) {
            Bundle bundle = data.getExtras();
            if (bundle == null) {
                return;
            }
            String[] strArr=null;
            //解析扫码结果数据(二维码数据结构是：product+":"+商品类型ID+":"+商口ID)
            String result = bundle.getString("result");
            //String text=EncryptionUtil.desECBDecrypt(result,"lesswalletCampRay@201404");
            try {
                String text = new String(Base64.decode(result, Base64.DEFAULT), "UTF-8");
                toast( "解析结果:" + text);
                strArr = text.split(":");
            }catch (Exception e) {
                toast("无效的二维码");
                return;
            }

            switch (requestCode) {
                //处理扫码结果
                case SCANNIN_REQUEST_CODE:
                    if (strArr!=null&&strArr.length > 0) {
                        String model = strArr[0];
                        if ("coupon".equals(model)) {
                            try {
                                long oid = Long.parseLong(strArr[1]);
                                bundle.clear();
                                bundle.putLong("id", oid);
                                this.startActivity(ScanCouponActivity.class, bundle, true);
                            } catch (Exception e) {
                            }
                        } else if ("card".equals(model)) {
                            long oid = Long.parseLong(strArr[1]);
                            bundle.clear();
                            bundle.putLong("id", oid);
                            this.startActivity(ScanCardActivity.class, bundle, true);
                        } else if ("user".equals(model)) {
//                            long uid = Long.parseLong(strArr[1]);
//                            bundle.clear();
//                            bundle.putLong("id", uid);
//                            this.startActivity(ScanUserActivity.class, bundle, true);

                        }
                    }
                    break;
                case SCANNIN_REQUEST_IMAGE://选择系统图片并解析
                    break;
                case SCANNIN_USER_CODE:
                    //解析扫码结果数据(二维码数据结构是：user+":"+用户ID
                    if (strArr!=null&&strArr.length > 0) {
                        String model = strArr[0];
                        if ("user".equals(model)) {
                            long uid = Long.parseLong(strArr[1]);
                            long productId = bundle.getLong("productId");
                            bundle.clear();
                            bundle.putLong("userId", uid);
                            bundle.putLong("productId", productId);
                            Product product=ProductModel.getInstance().getProductById(productId);
                            if(product.getProductTypeId()==1) {
                                this.startActivity(CouponSendActivity.class, bundle, false);
                            }else if(product.getProductTypeId()==3) {
                                this.startActivity(CardSendActivity.class, bundle, false);
                            }

                        } else {
                            toast("无法识别的用户二维码");
                        }
                    }
                    break;
                default:
            }
        }
    }

    /**初始化主面视图
     */
    @Override
    protected void initView() {
        User user= LessWalletApplication.INSTANCE().getAccount();
        tv_username.setText(user.getUserName());
    }

    /**
     * 初始化权限事件
     */
    protected void initPermissions(){
        String[] perms = {Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            //没有权限则向用户申请权限
            EasyPermissions.requestPermissions(this, "扫码需要摄像头权限",
                    TAKE_ALL_REQUEST_CODE, perms);
        }
    }

    /**
     * 向用户询问申请权限结果处理
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    /**
     * 授权成功
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if(TAKE_CAMERA_REQUEST_CODE==requestCode){
            //打开扫码页面
            openQRcodeScanActivity();
        }
        else if(TAKE_ALL_REQUEST_CODE==requestCode){
            Toast.makeText(this, "Permissions granted successfully", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 拒绝授权
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if(TAKE_CAMERA_REQUEST_CODE==requestCode){
            new AppSettingsDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_permission_title))
                    .setRationale(getString(R.string.dialog_camera_permission))
                    .setPositiveButton(getString(R.string.button_ok))
                    .setNegativeButton(getString(R.string.button_cancel))
                    .setRequestCode(TAKE_CAMERA_REQUEST_CODE)
                    .build()
                    .show();
        }
        else if(TAKE_ALL_REQUEST_CODE==requestCode){
            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                Toast.makeText(this, "您拒绝授予指定的权限，某些功能将不能正常工作", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
