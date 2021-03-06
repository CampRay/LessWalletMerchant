package com.campray.lesswalletmerchant.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.campray.lesswalletmerchant.db.entity.Merchant;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MERCHANT".
*/
public class MerchantDao extends AbstractDao<Merchant, Long> {

    public static final String TABLENAME = "MERCHANT";

    /**
     * Properties of entity Merchant.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Desc = new Property(2, String.class, "desc", false, "DESC");
        public final static Property StoreId = new Property(3, int.class, "storeId", false, "STORE_ID");
        public final static Property PictureUrl = new Property(4, String.class, "pictureUrl", false, "PICTURE_URL");
        public final static Property PicturePath = new Property(5, String.class, "picturePath", false, "PICTURE_PATH");
    }


    public MerchantDao(DaoConfig config) {
        super(config);
    }
    
    public MerchantDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MERCHANT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"NAME\" TEXT NOT NULL ," + // 1: name
                "\"DESC\" TEXT NOT NULL ," + // 2: desc
                "\"STORE_ID\" INTEGER NOT NULL ," + // 3: storeId
                "\"PICTURE_URL\" TEXT," + // 4: pictureUrl
                "\"PICTURE_PATH\" TEXT);"); // 5: picturePath
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MERCHANT\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Merchant entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getName());
        stmt.bindString(3, entity.getDesc());
        stmt.bindLong(4, entity.getStoreId());
 
        String pictureUrl = entity.getPictureUrl();
        if (pictureUrl != null) {
            stmt.bindString(5, pictureUrl);
        }
 
        String picturePath = entity.getPicturePath();
        if (picturePath != null) {
            stmt.bindString(6, picturePath);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Merchant entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getName());
        stmt.bindString(3, entity.getDesc());
        stmt.bindLong(4, entity.getStoreId());
 
        String pictureUrl = entity.getPictureUrl();
        if (pictureUrl != null) {
            stmt.bindString(5, pictureUrl);
        }
 
        String picturePath = entity.getPicturePath();
        if (picturePath != null) {
            stmt.bindString(6, picturePath);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Merchant readEntity(Cursor cursor, int offset) {
        Merchant entity = new Merchant( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // name
            cursor.getString(offset + 2), // desc
            cursor.getInt(offset + 3), // storeId
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // pictureUrl
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // picturePath
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Merchant entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.getString(offset + 1));
        entity.setDesc(cursor.getString(offset + 2));
        entity.setStoreId(cursor.getInt(offset + 3));
        entity.setPictureUrl(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setPicturePath(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Merchant entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Merchant entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Merchant entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
