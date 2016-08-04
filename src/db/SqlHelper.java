package db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import domain.Person;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 数据库操作类
 * 
 * @author Administrator
 * 
 */
public class SqlHelper {
	// 数据库帮助类
	private MyOpenHelper dbHelper;
	// 查询列集合
	private static HashMap<String, String> menuProjectionMap;

	// 构造函数
	public SqlHelper(Context c) {
		//初始化数据库对象
		dbHelper = new MyOpenHelper(c);
	}

	// 添加方法
	public Boolean insert(ContentValues values,String text) {
		// 获得数据库实例
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		// 插入之前先判断text是否已经存在
		String sql = "select * from " + MyOpenHelper.TABLES_TABLE_NAME + " where text=?";
		Cursor c = db.rawQuery(sql,  new String[]{text});
		
		
		if(c.getCount()==0){//不存在这条数据
			// 插入数据，返回行ID
			long rowId = db.insert(MyOpenHelper.TABLES_TABLE_NAME, null, values);
			// 如果插入成功返回uri
			if (rowId > 0) {
				return true;
			} else {
				return false;
			}
		}else{
			return false;
		}
		
	}


	
		// 查询方法
		public List<Person> query() {
				List<Person> pData = new ArrayList<Person>();
		        // 获得数据库实例
		        SQLiteDatabase db = dbHelper.getReadableDatabase();
		        // 返回游标集合
		        Cursor c = db.query(MyOpenHelper.TABLES_TABLE_NAME, null, null, null, null, null, null);
				while (c.moveToNext()) {
					String text = c.getString(c.getColumnIndex("text"));
					String name = c.getString(c.getColumnIndex("name"));
					String path = c.getString(c.getColumnIndex("path"));
					String item = c.getString(c.getColumnIndex("item"));
					Person p = new Person();
					p.setText(text);
					p.setName(name);
					p.setPath(path);
					p.setItem(item);
					System.out.println(text+name+"``````````````````````````");
					pData.add(p);
				}
		        return pData;
		        
		}
		
		
		
		public void delete(String text){
			//获得数据库实例
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			
			db.delete("person", "text=?", new String[]{text});
			db.close();
			
		}

}
