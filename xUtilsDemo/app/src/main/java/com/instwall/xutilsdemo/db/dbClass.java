package com.instwall.xutilsdemo.db;

import android.util.Log;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;

public class dbClass {
    //配置数据库
    public static DbManager.DaoConfig initDB(){
        DbManager.DaoConfig mDbManager = new DbManager.DaoConfig();
        mDbManager.setDbName("sun.db");
        mDbManager.setAllowTransaction(true);
        mDbManager.setDbVersion(1);
        //设置表创建监听
        mDbManager.setTableCreateListener(new DbManager.TableCreateListener() {
            @Override
            public void onTableCreated(DbManager db, TableEntity<?> table) {
                Log.d("stcLog","create table listener:" + table.getName());
            }
        });
        //设置数据库更新的监听
        mDbManager.setDbUpgradeListener(new DbManager.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

            }
        });

        //设置数据库打开的监听
        mDbManager.setDbOpenListener(new DbManager.DbOpenListener() {
            @Override
            public void onDbOpened(DbManager db) {
                Log.d("stcLog","database open listener");
                //开启数据库支持多线程操作，提高性能
                db.getDatabase().enableWriteAheadLogging();
            }
        });
        return mDbManager;
    }
}
