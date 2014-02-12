/* This activity takes care of the grid of images displayed. On click of each of the image it calls the FullImage Activity.
 */
package com.example.getimages;


import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class GridViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    Log.d("inside GridView", "");
        
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_layout);
		GridView gridView = (GridView) findViewById(R.id.grid_view);
	    Intent i = getIntent();
	    final ArrayList<String> strArr=i.getExtras().getStringArrayList("imagelist");
	        
	    gridView.setAdapter(new ImageAdapter(this,strArr));
	    
	        gridView.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View v,
	                    int position, long id) {
	 
	                // Sending image id to FullScreenActivity
	                Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
	                // passing array index
	                System.out.println("position"+position);
	                i.putExtra("id", position);
   	                i.putStringArrayListExtra("array_list",strArr);
	                startActivity(i);
	            }
	        });
	        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.grid_view, menu);
		return true;
	}

}
