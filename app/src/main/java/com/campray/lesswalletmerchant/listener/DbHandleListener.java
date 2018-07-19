package com.campray.lesswalletmerchant.listener;

import com.campray.lesswalletmerchant.util.AppException;

/**
 * Created by Phills on 9/5/2017.
 */

public abstract class DbHandleListener<T> implements IListener {
    public abstract void done(T obj, AppException exception);
}
