package com.example.dllo.tomatotodo.db;

import android.content.Context;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by dllo on 16/7/21.
 */
public class DBTools {

    private static DBTools dbTools;
    private LiteOrm liteOrm;

    private DBTools(Context context) {
        liteOrm = LiteOrm.newSingleInstance(context, "MyDB.db");
    }

    public LiteOrm SingleLiteOrm() {
        return liteOrm;
    }

    public static DBTools getInstance(Context context) {
        if (dbTools == null) {
            synchronized (DBTools.class) {
                if (dbTools == null) {
                    dbTools = new DBTools(context);
                }
            }
        }
        return dbTools;
    }


    //插入单条数据
    public <T> void insertSingle(Object T) {
        liteOrm.insert(T);
    }


    public <T> void upDataSingle(Object T) {
        liteOrm.update(T);
    }


    //插入多条
    public <T> void insertAll(List<Class<T>> list) {
        liteOrm.insert(list);
    }


    //清除指定数据库
    public <T> void deleteAll(Class<T> tClass) {
        liteOrm.deleteAll(tClass);
    }


    //条件删除
    public <T> void deleteCondition(Class<T> tClass, String columnName, String condition) {
        liteOrm.delete(new WhereBuilder(tClass).where(columnName + " LIKE ? ", new String[]{condition}));
    }

//    public <T> void upDataCondition(T t, String columnName) {
//        liteOrm.update(t, new ColumnsValue(columnName), ConflictAlgorithm.Fail);
//    }
    //查询指定表所有数据

    public <T> List<T> queryAll(Class<T> T) {
        List list = liteOrm.query(T);
        return list;
    }

    //条件查询
    public <T> List<T> queryCondition(Class<T> tClass, String columnName, String condition) {
        List<T> list = new ArrayList();
        list = liteOrm.query(new QueryBuilder(tClass).where(columnName + " LIKE ? ", new String[]{condition}));
        return list;
    }


}
