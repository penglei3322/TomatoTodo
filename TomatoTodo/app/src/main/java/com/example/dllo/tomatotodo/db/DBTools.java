package com.example.dllo.tomatotodo.db;

import android.content.Context;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;

import java.util.ArrayList;

/**
 * Created by dllo on 16/7/21.
 */
public class DBTools{

    private static DBTools dbTools;
    private LiteOrm liteOrm;

    private DBTools(Context context) {
        liteOrm = LiteOrm.newSingleInstance(context,"MyDB.db");
    }

    public LiteOrm SingleLiteOrm(){
        return  liteOrm;
    }

    public static DBTools getInstance(Context context){
        if (dbTools == null){
            synchronized (DBTools.class){
                if (dbTools == null){
                    dbTools = new DBTools(context);
                }
            }
        }
        return dbTools;
    }

    // 添加数据方法
    public void addData(Object T) {
        liteOrm.insert(T);
    }

    // 查询全部数据方法
    public <T> ArrayList<T> queryAll(Class<T> tClass){
        return liteOrm.query(tClass);
    }

}
