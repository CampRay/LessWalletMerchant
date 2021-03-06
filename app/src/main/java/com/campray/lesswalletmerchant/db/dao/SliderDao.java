package com.campray.lesswalletmerchant.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.campray.lesswalletmerchant.db.entity.Slider;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SLIDER".
*/
public class SliderDao extends AbstractDao<Slider, Long> {

    public static final String TABLENAME = "SLIDER";

    /**
     * Properties of entity Slider.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property PicUrl = new Property(1, String.class, "picUrl", false, "PIC_URL");
        public final static Property Text = new Property(2, String.class, "text", false, "TEXT");
        public final static Property Link = new Property(3, String.class, "link", false, "LINK");
    }


    public SliderDao(DaoConfig config) {
        super(config);
    }
    
    public SliderDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SLIDER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"PIC_URL\" TEXT," + // 1: picUrl
                "\"TEXT\" TEXT," + // 2: text
                "\"LINK\" TEXT);"); // 3: link
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SLIDER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Slider entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String picUrl = entity.getPicUrl();
        if (picUrl != null) {
            stmt.bindString(2, picUrl);
        }
 
        String text = entity.getText();
        if (text != null) {
            stmt.bindString(3, text);
        }
 
        String link = entity.getLink();
        if (link != null) {
            stmt.bindString(4, link);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Slider entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String picUrl = entity.getPicUrl();
        if (picUrl != null) {
            stmt.bindString(2, picUrl);
        }
 
        String text = entity.getText();
        if (text != null) {
            stmt.bindString(3, text);
        }
 
        String link = entity.getLink();
        if (link != null) {
            stmt.bindString(4, link);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Slider readEntity(Cursor cursor, int offset) {
        Slider entity = new Slider( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // picUrl
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // text
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // link
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Slider entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPicUrl(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setText(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setLink(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Slider entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Slider entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Slider entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
