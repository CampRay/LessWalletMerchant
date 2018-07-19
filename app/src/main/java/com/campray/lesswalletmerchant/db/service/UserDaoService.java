package com.campray.lesswalletmerchant.db.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.campray.lesswalletmerchant.db.dao.DaoMaster;
import com.campray.lesswalletmerchant.db.dao.DaoSession;
import com.campray.lesswalletmerchant.db.dao.UserDao;
import com.campray.lesswalletmerchant.db.entity.User;

import java.util.List;

import static com.campray.lesswalletmerchant.db.dao.UserDao.Properties.Email;

/**
 * 用户表DAO业务处理服务类
 * Created by Phills on 10/30/2017.
 */

public class UserDaoService {
    private DaoMaster.DevOpenHelper openHelper;
    private static UserDaoService instance;
    public static synchronized  UserDaoService getInstance(Context context) {
        if (instance == null) {
            instance = new UserDaoService(context);
        }
        return instance;
    }

    private UserDaoService(Context context){
        this.clear();
        String DBName = "lesswallet";
        this.openHelper = new DaoMaster.DevOpenHelper(context.getApplicationContext(), DBName, null);
    }

    /**
     * 清空资源
     */
    public void clear() {
        if(openHelper !=null) {
            openHelper.close();
            openHelper = null;
        }
    }

    public DaoSession openReadableDb() {
        checkInit();
        SQLiteDatabase db = openHelper.getReadableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        return daoSession;
    }

    public DaoSession openWritableDb(){
        checkInit();
        SQLiteDatabase db = openHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        return daoSession;
    }

    private void checkInit(){
        if(openHelper ==null){
            throw new RuntimeException("请初始化db");
        }
    }

    //-------------------------------------------------------------

    /**获取本地所有的已登录过的用户信息
     * @return
     */
    public List<User> getAllUser(){
        UserDao dao =openReadableDb().getUserDao();
        return dao.queryBuilder().list();
    }

    /**
     * 根据帐号获取本地已保存的登录过的用户对象
     * @param uid*
     * @return
     */
    public User getUser(String uid){
        UserDao dao =openReadableDb().getUserDao();
        return dao.queryBuilder().where(UserDao.Properties.UserName.eq(uid)).build().unique();
    }

    /**
     * 查询帐号或Email相同的的用户对象
     * @param uid*
     * @return
     */
    public User getUserByNameOrEmail(String uid){
        UserDao dao =openReadableDb().getUserDao();
        return dao.queryBuilder().whereOr(UserDao.Properties.UserName.eq(uid), Email.eq(uid)).build().unique();
    }

    /**
     * 根据ID获取本地已保存的登录过的用户对象
     * @param uid*
     * @return
     */
    public User getUserById(long uid){
        UserDao dao =openReadableDb().getUserDao();
        return dao.queryBuilder().where(UserDao.Properties.Id.eq(uid)).build().unique();
    }

    /**
     * 根据帐号或Email获取本地已保存的登录过的用户对象
     * @param uid*
     * @param email*
     * @return
     */
    public User getUser(String uid,String email){
        UserDao dao =openReadableDb().getUserDao();
        return dao.queryBuilder().whereOr(UserDao.Properties.UserName.eq(uid), Email.eq(email)).build().unique();
    }

    /**创建或更新用户信息
     * @param user
     * @return long:返回插入或修改的id
     */
    public long insertOrUpdateUser(User user){
        UserDao dao = openWritableDb().getUserDao();
        User local = getUser(user.getUserName(),user.getEmail());
        if(local==null){
            return dao.insertOrReplace(user);
        }else{
            return local.getId();
        }
    }

    /**
     * 修改指定用户的API访问Token
     * @param user
     * @param token
     * @return
     */
    public long updateUserToken(User user,String token){
        UserDao dao = openWritableDb().getUserDao();
        user.setToken(token);
        return dao.insertOrReplace(user);
    }

    /**
     * 删除指定的用户
     * @param user
     */
    public void deleteUser(User user){
        UserDao dao = openWritableDb().getUserDao();
        dao.delete(user);
    }
}
