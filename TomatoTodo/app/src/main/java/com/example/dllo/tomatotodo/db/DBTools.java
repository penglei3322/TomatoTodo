package com.example.dllo.tomatotodo.db;

import android.content.Context;

import com.litesuits.orm.LiteOrm;

/**
 * Created by dllo on 16/7/21.
 */
public class DBTools {

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


}
