package com.campray.lesswalletmerchant.model;

import com.campray.lesswalletmerchant.LessWalletApplication;
import com.campray.lesswalletmerchant.db.entity.Merchant;
import com.campray.lesswalletmerchant.db.entity.Product;
import com.campray.lesswalletmerchant.db.service.MerchantDaoService;
import com.campray.lesswalletmerchant.db.service.ProductDaoService;
import com.campray.lesswalletmerchant.listener.ApiHandleListener;
import com.campray.lesswalletmerchant.listener.OperationListener;
import com.campray.lesswalletmerchant.util.AppException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 卡卷商口对象业务处理模型
 * @author :Phills
 * @project:ProductModel
 * @date :2017-08-22-18:09
 */
public class ProductModel extends BaseModel {

    private static ProductModel ourInstance = new ProductModel();

    public static ProductModel getInstance() {
        return ourInstance;
    }

    private ProductModel() {
    }

    /**
     * 根据商品ID从服务器获取卡卷商品信息数据
     * @param listener
     */
    public void getProductFromServer(long pid,final OperationListener<Product> listener) {
        //封装请求参数
        JsonObject jObj=new JsonObject();
        jObj.addProperty("productId",(int)pid);
        jObj.addProperty("device",this.getDeviceId());
        this.httpPostAPI(ProductModel.URL_API_GETPRODUCT, jObj, new ApiHandleListener<JsonObject>() {
            @Override
            public void  done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors").isJsonNull()) {
                            if (obj.get("Data").isJsonArray()) {
                                JsonArray jArr = obj.get("Data").getAsJsonArray();
                                Gson gson = new Gson();
                                try {
                                    JsonObject item=jArr.get(0).getAsJsonObject();
                                    Product product = gson.fromJson(item, Product.class);
                                    long vendorId=LessWalletApplication.INSTANCE().getAccount().getVenderId();
                                    if(product.getMerchantId()<=0||product.getMerchantId()!=vendorId){
                                        listener.done(null, new AppException("E_3020"));
                                    }
                                    else{
                                        listener.done(product, null);
                                    }
                                }
                                catch (Exception exe){
                                    listener.done(null, new AppException("E_1005"));
                                }
                            }
                            else{
                                listener.done(null, new AppException("E_3020"));
                            }
                        } else {
                            listener.done(null, new AppException(obj.get("Errors").getAsString()));
                        }
                    }
                    catch (Exception e){
                        listener.done(null, new AppException("E_1004"));
                    }
                } else {
                    listener.done(null, exception);
                }
            }
        });
    }

    /**
     * 从服务器获取商家所有卡卷商品信息数据
     * @param vid 商户ID
     * @param productType 商品类型
     * @param pageIndex 当前页索引
     * @param pageSize 每页大小
     * @param listener
     */
    public void getAllProductsFromServer(long vid,int productType,int pageIndex,int pageSize,final OperationListener<List<Product>> listener) {
        //封装请求参数
        JsonObject jObj=new JsonObject();
        jObj.addProperty("vendorId",vid);
        jObj.addProperty("device",this.getDeviceId());
        jObj.addProperty("productType",productType);
        jObj.addProperty("pageIndex",pageIndex);
        jObj.addProperty("pageSize",pageSize);
        this.httpPostAPI(ProductModel.URL_API_GETALL_MERCHANT_PRODUCTS, jObj, new ApiHandleListener<JsonObject>() {
            @Override
            public void  done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors").isJsonNull()) {
                            if (obj.get("Data").isJsonArray()) {
                                JsonArray jArr = obj.get("Data").getAsJsonArray();
                                Gson gson = new Gson();
                                try {
                                    List<Product> list=new ArrayList<Product>();
                                    for(JsonElement item:jArr) {
                                        Product product = gson.fromJson(item, Product.class);
                                        ProductDaoService.getInstance(getContext()).insertOrUpdateProduct(product);
                                        list.add(product);
                                    }
                                    listener.done(list, null);
                                }
                                catch (Exception exe){
                                    listener.done(null, new AppException("E_1005"));
                                }
                            }
                            else{
                                listener.done(null, null);
                            }
                        } else {
                            listener.done(null, new AppException(obj.get("Errors").getAsString()));
                        }
                    }
                    catch (Exception e){
                        listener.done(null, new AppException("E_1004"));
                    }
                } else {
                    listener.done(null, exception);
                }
            }
        });
    }

    /**
     * 根据记录ID查询卡卷商品数据对象
     * @param ProductId
     * @return
     */
    public Product getProductById(long ProductId) {
        try {
            return ProductDaoService.getInstance(getContext()).getProduct(ProductId);
        }
        catch (Exception e){ }
        return null;
    }

    /**
     * 根据商家ID查询所有的卡卷对象
     * @param merchantId
     * @return
     */
    public List<Product> getAllProductByMerchantId(int merchantId) {
        try {
            return ProductDaoService.getInstance(getContext()).getAllProductByMerchant(merchantId);
        }
        catch (Exception e){ }
        return null;
    }

    /**
     * 根据类型查询所有的卡卷对应的商品对象
     * @param typeId
     * @return
     */
    public List<Product> getAllProductByType(int typeId) {
        try {
            return ProductDaoService.getInstance(getContext()).getAllProductByType(typeId);
        }
        catch (Exception e){ }
        return null;
    }

    /**
     * 根据类型查询分页显示信息
     * @param typeId          卡卷类别
     * @param pageNum         当前页数
     * @param pageSize        每页显示数
     * @return                信息列表
     */
    public List<Product> getProductPageByType(int typeId,int pageNum,int pageSize) {
        try {
            return ProductDaoService.getInstance(getContext()).getPageProductByType(typeId,pageNum,pageSize);
        }
        catch (Exception e){ }
        return null;
    }

    /**
     * 添加卡卷对应的商品对象
     * @param product
     * @return
     */
    public boolean insertOrUpdateProduct(Product product) {
        try {
            ProductDaoService.getInstance(getContext()).insertOrUpdateProduct(product);
            return true;
        }
        catch (Exception e){ return false;}
    }

    /**
     * 修改卡卷对应的商品对象
     * @param product
     * @return
     */
    public boolean updaeProduct(Product product) {
        try {
            ProductDaoService.getInstance(getContext()).updateProduct(product);
            return true;
        }
        catch (Exception e){ return false;}
    }


    /**
     * 删除卡卷对应的商品对象
     * @param product
     * @return
     */
    public boolean deleteProduct(Product product) {
        try {
            ProductDaoService.getInstance(getContext()).deleteProduct(product);
            return true;
        }
        catch (Exception e){ return false;}
    }

    /**
     * 删除卡卷对应的商品对象
     * @param productId
     * @return
     */
    public boolean deleteProductById(long productId) {
        try {
            ProductDaoService.getInstance(getContext()).deleteProduct(productId);
            return true;
        }
        catch (Exception e){ return false;}
    }

    /**
     * 查询指定id的卡卷商品是否存在
     * @param productId*
     * @return boolean
     */
    public boolean hasProduct(long productId){
        return ProductDaoService.getInstance(getContext()).hasProduct(productId);
    }

}
