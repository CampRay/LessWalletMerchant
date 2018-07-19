package com.campray.lesswalletmerchant.db.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.campray.lesswalletmerchant.db.entity.Country;
import com.campray.lesswalletmerchant.db.entity.Coupon;
import com.campray.lesswalletmerchant.db.entity.Currency;
import com.campray.lesswalletmerchant.db.entity.Friend;
import com.campray.lesswalletmerchant.db.entity.History;
import com.campray.lesswalletmerchant.db.entity.Language;
import com.campray.lesswalletmerchant.db.entity.LocalizedProperty;
import com.campray.lesswalletmerchant.db.entity.Merchant;
import com.campray.lesswalletmerchant.db.entity.Product;
import com.campray.lesswalletmerchant.db.entity.ProductAttribute;
import com.campray.lesswalletmerchant.db.entity.Slider;
import com.campray.lesswalletmerchant.db.entity.User;

import com.campray.lesswalletmerchant.db.dao.CountryDao;
import com.campray.lesswalletmerchant.db.dao.CouponDao;
import com.campray.lesswalletmerchant.db.dao.CurrencyDao;
import com.campray.lesswalletmerchant.db.dao.FriendDao;
import com.campray.lesswalletmerchant.db.dao.HistoryDao;
import com.campray.lesswalletmerchant.db.dao.LanguageDao;
import com.campray.lesswalletmerchant.db.dao.LocalizedPropertyDao;
import com.campray.lesswalletmerchant.db.dao.MerchantDao;
import com.campray.lesswalletmerchant.db.dao.ProductDao;
import com.campray.lesswalletmerchant.db.dao.ProductAttributeDao;
import com.campray.lesswalletmerchant.db.dao.SliderDao;
import com.campray.lesswalletmerchant.db.dao.UserDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig countryDaoConfig;
    private final DaoConfig couponDaoConfig;
    private final DaoConfig currencyDaoConfig;
    private final DaoConfig friendDaoConfig;
    private final DaoConfig historyDaoConfig;
    private final DaoConfig languageDaoConfig;
    private final DaoConfig localizedPropertyDaoConfig;
    private final DaoConfig merchantDaoConfig;
    private final DaoConfig productDaoConfig;
    private final DaoConfig productAttributeDaoConfig;
    private final DaoConfig sliderDaoConfig;
    private final DaoConfig userDaoConfig;

    private final CountryDao countryDao;
    private final CouponDao couponDao;
    private final CurrencyDao currencyDao;
    private final FriendDao friendDao;
    private final HistoryDao historyDao;
    private final LanguageDao languageDao;
    private final LocalizedPropertyDao localizedPropertyDao;
    private final MerchantDao merchantDao;
    private final ProductDao productDao;
    private final ProductAttributeDao productAttributeDao;
    private final SliderDao sliderDao;
    private final UserDao userDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        countryDaoConfig = daoConfigMap.get(CountryDao.class).clone();
        countryDaoConfig.initIdentityScope(type);

        couponDaoConfig = daoConfigMap.get(CouponDao.class).clone();
        couponDaoConfig.initIdentityScope(type);

        currencyDaoConfig = daoConfigMap.get(CurrencyDao.class).clone();
        currencyDaoConfig.initIdentityScope(type);

        friendDaoConfig = daoConfigMap.get(FriendDao.class).clone();
        friendDaoConfig.initIdentityScope(type);

        historyDaoConfig = daoConfigMap.get(HistoryDao.class).clone();
        historyDaoConfig.initIdentityScope(type);

        languageDaoConfig = daoConfigMap.get(LanguageDao.class).clone();
        languageDaoConfig.initIdentityScope(type);

        localizedPropertyDaoConfig = daoConfigMap.get(LocalizedPropertyDao.class).clone();
        localizedPropertyDaoConfig.initIdentityScope(type);

        merchantDaoConfig = daoConfigMap.get(MerchantDao.class).clone();
        merchantDaoConfig.initIdentityScope(type);

        productDaoConfig = daoConfigMap.get(ProductDao.class).clone();
        productDaoConfig.initIdentityScope(type);

        productAttributeDaoConfig = daoConfigMap.get(ProductAttributeDao.class).clone();
        productAttributeDaoConfig.initIdentityScope(type);

        sliderDaoConfig = daoConfigMap.get(SliderDao.class).clone();
        sliderDaoConfig.initIdentityScope(type);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        countryDao = new CountryDao(countryDaoConfig, this);
        couponDao = new CouponDao(couponDaoConfig, this);
        currencyDao = new CurrencyDao(currencyDaoConfig, this);
        friendDao = new FriendDao(friendDaoConfig, this);
        historyDao = new HistoryDao(historyDaoConfig, this);
        languageDao = new LanguageDao(languageDaoConfig, this);
        localizedPropertyDao = new LocalizedPropertyDao(localizedPropertyDaoConfig, this);
        merchantDao = new MerchantDao(merchantDaoConfig, this);
        productDao = new ProductDao(productDaoConfig, this);
        productAttributeDao = new ProductAttributeDao(productAttributeDaoConfig, this);
        sliderDao = new SliderDao(sliderDaoConfig, this);
        userDao = new UserDao(userDaoConfig, this);

        registerDao(Country.class, countryDao);
        registerDao(Coupon.class, couponDao);
        registerDao(Currency.class, currencyDao);
        registerDao(Friend.class, friendDao);
        registerDao(History.class, historyDao);
        registerDao(Language.class, languageDao);
        registerDao(LocalizedProperty.class, localizedPropertyDao);
        registerDao(Merchant.class, merchantDao);
        registerDao(Product.class, productDao);
        registerDao(ProductAttribute.class, productAttributeDao);
        registerDao(Slider.class, sliderDao);
        registerDao(User.class, userDao);
    }
    
    public void clear() {
        countryDaoConfig.clearIdentityScope();
        couponDaoConfig.clearIdentityScope();
        currencyDaoConfig.clearIdentityScope();
        friendDaoConfig.clearIdentityScope();
        historyDaoConfig.clearIdentityScope();
        languageDaoConfig.clearIdentityScope();
        localizedPropertyDaoConfig.clearIdentityScope();
        merchantDaoConfig.clearIdentityScope();
        productDaoConfig.clearIdentityScope();
        productAttributeDaoConfig.clearIdentityScope();
        sliderDaoConfig.clearIdentityScope();
        userDaoConfig.clearIdentityScope();
    }

    public CountryDao getCountryDao() {
        return countryDao;
    }

    public CouponDao getCouponDao() {
        return couponDao;
    }

    public CurrencyDao getCurrencyDao() {
        return currencyDao;
    }

    public FriendDao getFriendDao() {
        return friendDao;
    }

    public HistoryDao getHistoryDao() {
        return historyDao;
    }

    public LanguageDao getLanguageDao() {
        return languageDao;
    }

    public LocalizedPropertyDao getLocalizedPropertyDao() {
        return localizedPropertyDao;
    }

    public MerchantDao getMerchantDao() {
        return merchantDao;
    }

    public ProductDao getProductDao() {
        return productDao;
    }

    public ProductAttributeDao getProductAttributeDao() {
        return productAttributeDao;
    }

    public SliderDao getSliderDao() {
        return sliderDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

}