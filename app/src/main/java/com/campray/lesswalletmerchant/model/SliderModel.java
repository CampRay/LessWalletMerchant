package com.campray.lesswalletmerchant.model;

import com.campray.lesswalletmerchant.db.entity.Slider;
import com.campray.lesswalletmerchant.db.service.SliderDaoService;
import com.campray.lesswalletmerchant.listener.ApiHandleListener;
import com.campray.lesswalletmerchant.listener.OperationListener;
import com.campray.lesswalletmerchant.util.AppException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * 轮播图片对象业务处理模型
 * @author :Phills
 * @project:SliderModel
 * @date :2018-05-22-18:09
 */
public class SliderModel extends BaseModel {

    private static SliderModel ourInstance = new SliderModel();

    public static SliderModel getInstance() {
        return ourInstance;
    }

    private SliderModel() {
    }

    /**
     * 从服务器获取所有轮播图片信息数据
     * @param listener
     */
    public void getAllSlidersFromServer(final OperationListener<List<Slider>> listener) {
        this.httpPostAPI(SliderModel.URL_API_GETALLSLIDERS, null,new ApiHandleListener<JsonObject>() {
            @Override
            public void done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors").isJsonNull()) {
                            if (obj.get("Data").isJsonArray()) {
                                JsonArray jArr = obj.get("Data").getAsJsonArray();
                                Gson gson = new Gson();
                                List<Slider> sliderList = gson.fromJson(jArr, new TypeToken<List<Slider>>() {}.getType());
                                for(Slider slider:sliderList){
                                    try {
                                        SliderDaoService.getInstance(getContext()).insertOrUpdateSlider(slider);
                                    }
                                    catch (Exception exe){
                                        listener.done(null, new AppException("E_1005"));
                                        break;
                                    }
                                }
                            }
                            listener.done(null, null);
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
     * 查询所有轮播图片数据对象
     * @return
     */
    public List<Slider> getAllSliders() {
        try {
            List<Slider> sliders = SliderDaoService.getInstance(getContext()).getAllSliders();
            return sliders;
        }
        catch (Exception e){ }
        return null;
    }


    /**
     * 根据记录ID查询轮播图片数据对象
     * @param id
     * @return
     */
    public Slider getSliderById(long id) {
        try {
            return SliderDaoService.getInstance(getContext()).getSlider(id);
        }
        catch (Exception e){ }
        return null;
    }

}
