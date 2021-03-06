package com.campray.lesswalletmerchant.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.campray.lesswalletmerchant.db.entity.Coupon;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "COUPON".
*/
public class CouponDao extends AbstractDao<Coupon, Long> {

    public static final String TABLENAME = "COUPON";

    /**
     * Properties of entity Coupon.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property OrderId = new Property(0, Long.class, "orderId", true, "_id");
        public final static Property ProductId = new Property(1, Long.class, "productId", false, "PRODUCT_ID");
        public final static Property Cid = new Property(2, String.class, "cid", false, "CID");
        public final static Property UserId = new Property(3, long.class, "userId", false, "USER_ID");
        public final static Property OrderTotal = new Property(4, float.class, "orderTotal", false, "ORDER_TOTAL");
        public final static Property PaymentStatus = new Property(5, int.class, "paymentStatus", false, "PAYMENT_STATUS");
        public final static Property StartTime = new Property(6, String.class, "startTime", false, "START_TIME");
        public final static Property EndTime = new Property(7, String.class, "endTime", false, "END_TIME");
        public final static Property Deleted = new Property(8, boolean.class, "deleted", false, "DELETED");
    }


    public CouponDao(DaoConfig config) {
        super(config);
    }
    
    public CouponDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"COUPON\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: orderId
                "\"PRODUCT_ID\" INTEGER," + // 1: productId
                "\"CID\" TEXT," + // 2: cid
                "\"USER_ID\" INTEGER NOT NULL ," + // 3: userId
                "\"ORDER_TOTAL\" REAL NOT NULL ," + // 4: orderTotal
                "\"PAYMENT_STATUS\" INTEGER NOT NULL ," + // 5: paymentStatus
                "\"START_TIME\" TEXT NOT NULL ," + // 6: startTime
                "\"END_TIME\" TEXT," + // 7: endTime
                "\"DELETED\" INTEGER NOT NULL );"); // 8: deleted
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"COUPON\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Coupon entity) {
        stmt.clearBindings();
 
        Long orderId = entity.getOrderId();
        if (orderId != null) {
            stmt.bindLong(1, orderId);
        }
 
        Long productId = entity.getProductId();
        if (productId != null) {
            stmt.bindLong(2, productId);
        }
 
        String cid = entity.getCid();
        if (cid != null) {
            stmt.bindString(3, cid);
        }
        stmt.bindLong(4, entity.getUserId());
        stmt.bindDouble(5, entity.getOrderTotal());
        stmt.bindLong(6, entity.getPaymentStatus());
        stmt.bindString(7, entity.getStartTime());
 
        String endTime = entity.getEndTime();
        if (endTime != null) {
            stmt.bindString(8, endTime);
        }
        stmt.bindLong(9, entity.getDeleted() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Coupon entity) {
        stmt.clearBindings();
 
        Long orderId = entity.getOrderId();
        if (orderId != null) {
            stmt.bindLong(1, orderId);
        }
 
        Long productId = entity.getProductId();
        if (productId != null) {
            stmt.bindLong(2, productId);
        }
 
        String cid = entity.getCid();
        if (cid != null) {
            stmt.bindString(3, cid);
        }
        stmt.bindLong(4, entity.getUserId());
        stmt.bindDouble(5, entity.getOrderTotal());
        stmt.bindLong(6, entity.getPaymentStatus());
        stmt.bindString(7, entity.getStartTime());
 
        String endTime = entity.getEndTime();
        if (endTime != null) {
            stmt.bindString(8, endTime);
        }
        stmt.bindLong(9, entity.getDeleted() ? 1L: 0L);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Coupon readEntity(Cursor cursor, int offset) {
        Coupon entity = new Coupon( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // orderId
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // productId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // cid
            cursor.getLong(offset + 3), // userId
            cursor.getFloat(offset + 4), // orderTotal
            cursor.getInt(offset + 5), // paymentStatus
            cursor.getString(offset + 6), // startTime
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // endTime
            cursor.getShort(offset + 8) != 0 // deleted
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Coupon entity, int offset) {
        entity.setOrderId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setProductId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setCid(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setUserId(cursor.getLong(offset + 3));
        entity.setOrderTotal(cursor.getFloat(offset + 4));
        entity.setPaymentStatus(cursor.getInt(offset + 5));
        entity.setStartTime(cursor.getString(offset + 6));
        entity.setEndTime(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setDeleted(cursor.getShort(offset + 8) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Coupon entity, long rowId) {
        entity.setOrderId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Coupon entity) {
        if(entity != null) {
            return entity.getOrderId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Coupon entity) {
        return entity.getOrderId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
