package one.bw.com.jingdong.sousuo.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/12/012.
 */

public class MySousuoDao {
    SQLiteDatabase db;
    public MySousuoDao(SQLiteDatabase db) {
        this.db=db;
        db.execSQL("create table if not exists sousuo(id Integer primary key,name varchar(50))");
    }

    public void addShuJu(String name){
        db.execSQL("insert into sousuo(name) values('"+name+"')");
    }

    public boolean chaLiShi(String name){
        Cursor cursor = db.rawQuery("select *from sousuo where name='" + name + "'", null);
        boolean b = cursor.moveToNext();
        return b;
    }
    public ArrayList<String> chaList(){
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select *from sousuo", null);
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("name"));
            list.add(name);
        }
        return list;
    }

    public void qingLishi(){
        db.execSQL("delete from sousuo");
    }

}
