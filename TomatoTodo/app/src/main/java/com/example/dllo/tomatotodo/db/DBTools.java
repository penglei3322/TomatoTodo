package com.example.dllo.tomatotodo.db;

import android.content.Context;

import com.example.dllo.tomatotodo.potatolist.data.PhtatoListData;
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

    //指定更新某条数据
    public void upData(String oldContext, String newContext) {
        QueryBuilder<PhtatoListData> queryBuilder = new QueryBuilder<>(PhtatoListData.class);
        queryBuilder.whereEquals("content", oldContext);
        for (PhtatoListData data : liteOrm.query(queryBuilder)) {
            data.setContent(newContext);
            liteOrm.update(data);
        }
    }

    //条件删除
    public <T> void deleteCondition(Class<T> tClass, String columnName, String condition) {
        liteOrm.delete(new WhereBuilder(tClass).where(columnName + " LIKE ?", new String[]{condition}));
    }

    public <T> void delConditionByTime(Class<T> tClass, String columnName, Long condition){
        liteOrm.delete(new WhereBuilder(tClass).where(columnName + " LIKE ?", new Long[]{condition}));
    }

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

    // 条件查询, checkBox的选中状态
    public <T> List<T> queryChecked(Class<T> tClass, String columnName, boolean condition) {
        List<T> list = new ArrayList<>();
        list = liteOrm.query(new QueryBuilder<T>(tClass).where(columnName + " LIKE ?", new Boolean[]{condition}));
        return list;
    }


}
