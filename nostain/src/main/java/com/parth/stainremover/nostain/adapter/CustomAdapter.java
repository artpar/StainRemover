package com.parth.stainremover.nostain.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import com.parth.stainremover.nostain.R;

/**
 * author parth.mudgal on 18/07/14.
 */
public class CustomAdapter extends SimpleCursorAdapter
{
	private final Context context;
	Cursor items;
	private LayoutInflater mInflater;
	private int viewResourceId;
	private int selectedPosition;

	public CustomAdapter(Context context, int resourceId, Cursor data, String[] fields, int[] is)
	{
		super(context, resourceId, data, fields, is, FLAG_REGISTER_CONTENT_OBSERVER);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		viewResourceId = resourceId;
		this.context = context;
		items = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{

		// get reference to the row
		View view = super.getView(position, convertView, parent);
		ImageView image = (ImageView) view.findViewById(R.id.image);
		String imgName = getCursor().getString(2);
		String[] parts = imgName.split("\\.");
		int resource = context.getResources().getIdentifier("img" + parts[0], "drawable", context.getPackageName());
		image.setImageResource(resource);
		return view;
	}
}
