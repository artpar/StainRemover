package com.parth.stainremover.nostain;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import com.parth.stainremover.nostain.adapter.CustomAdapter;
import com.parth.stainremover.nostain.database.MySQLiteHelper;

public class MainActivity extends ActionBarActivity
{

	private CustomAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ListView listView = (ListView) findViewById(R.id.Stain);
		final MySQLiteHelper dbHelper = new MySQLiteHelper(this);
		Cursor stainCursor = dbHelper.getAllSuggestedValues("");
		this.adapter =
		        new CustomAdapter(this, R.layout.list_item, stainCursor, new String[]{MySQLiteHelper.COLUMN_FROM},
		                new int[]{android.R.id.text1});
		listView.setAdapter(adapter);
		EditText filter = (EditText) findViewById(R.id.filterText);
		adapter.setFilterQueryProvider(new FilterQueryProvider()
		{

			@Override
			public Cursor runQuery(CharSequence constraint)
			{
				String partialValue = constraint.toString();
				return dbHelper.getAllSuggestedValues(partialValue);

			}
		});

		filter.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{

			}

			@Override
			public void afterTextChanged(Editable s)
			{
				adapter.getFilter().filter(s.toString());

			}
		});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				Intent intent = null;
				// If network available goto Store else take user to Library.
				intent = new Intent(getApplicationContext(), StainData.class);
				Bundle bundle = new Bundle();
				bundle.putLong("item", adapterView.getItemIdAtPosition(i));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
