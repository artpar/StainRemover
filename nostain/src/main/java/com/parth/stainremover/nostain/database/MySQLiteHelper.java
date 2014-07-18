package com.parth.stainremover.nostain.database;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper
{

	public static final String TABLE_COMMENTS = "stain";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_FROM = "stainof";
	public static final String COLUMN_ON = "fabric";
	public static final String COLUMN_NEED = "need";
	public static final String COLUMN_STEPS = "steps";
	public static final String COLUMN_NOTES = "notes";
	public static final String COLUMN_IMG = "img";

	private static final String DATABASE_NAME = "stain.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table " + TABLE_COMMENTS + "(" + COLUMN_ID
	        + " integer primary key autoincrement" + ", " + COLUMN_FROM + " text not null" + ", " + COLUMN_ON
	        + " text not null" + ", " + COLUMN_NEED + " text not null" + ", " + COLUMN_STEPS + " text not null" + ", "
	        + COLUMN_NOTES + " text not null" + ", " + COLUMN_IMG + " text not null" + ");";
	private final Context context;

	public MySQLiteHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	public String readFully(InputStream inputStream, String encoding) throws IOException
	{
		return new String(readFully(inputStream), encoding);
	}

	private byte[] readFully(InputStream inputStream) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = 0;
		while ((length = inputStream.read(buffer)) != -1)
		{
			baos.write(buffer, 0, length);
		}
		return baos.toByteArray();
	}

	@Override
	public void onCreate(SQLiteDatabase database)
	{
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
		database.execSQL(DATABASE_CREATE);
		try
		{
			InputStream input = context.getAssets().open("stain.sql");

			String[] lines = readFully(input, "UTF-8").split(";\n");
			for (String line : lines)
			{
				if (line == null || line.length() < 2)
				{
					continue;
				}
				Log.d(MySQLiteHelper.class.getName(), "Insert sql - " + line);
				database.execSQL(line);
			}
		}
		catch (IOException e)
		{
			Log.e(MySQLiteHelper.class.getName(), "Error while reading stain.sql", e);
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.w(MySQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion
		        + ", which will destroy all old data");
		onCreate(db);
	}

	public Cursor getAllSuggestedValues(String partialValue)
	{
		if (partialValue == null)
		{
			partialValue = "%";
		}
		else
		{
			partialValue = "%" + partialValue + "%";
		}
		return this.getReadableDatabase().rawQuery(
		        "select " + MySQLiteHelper.COLUMN_ID + ", " + MySQLiteHelper.COLUMN_FROM + " || ' on ' || " + COLUMN_ON + " as "
		                + COLUMN_FROM + "," + COLUMN_IMG + " from stain where " + COLUMN_FROM + " like ? order by "
		                + MySQLiteHelper.COLUMN_FROM, new String[]{partialValue});
	}
}
