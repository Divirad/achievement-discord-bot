package com.divirad.discordbot.achievement.database;

import java.util.ArrayList;

public class DaoEvent<T> {
	public static final int INSERT = 100;
	
	public static final int SINGLE_INSERT 	= 1 + INSERT;
	public static final int MULTI_INSERT	= 2 + INSERT;
	
	public static final int SELECT = 200;
	
	public static final int UPDATE = 300;
	
	public static final int DELETE = 400;
	
	public static final int REPLACE= 500;
	public static final int SINGLE_REPLACE	= 1 + REPLACE;
	public static final int MULTI_REPLACE	= 2 + REPLACE;
	
	public int id;
	
	public long when;
	
	public int rows_affected;
	
	public ArrayList<T> data_affected;
	
	public Class<T> cls;
	
	public DaoEvent(ArrayList<T> data, long when, int id, int rows_affected, Class<T> cls) {
		this.data_affected = data;
		this.when = when;
		this.id = id;
		this.rows_affected = rows_affected;
		this.cls = cls;
	}
	
	public DaoEvent(T data, long when, int id, int rows_affected, Class<T> cls) {
		this.data_affected = new ArrayList<>();
		data_affected.add(data);
		this.when = when;
		this.id = id;
		this.rows_affected = rows_affected;
		this.cls = cls;
	} 
}
