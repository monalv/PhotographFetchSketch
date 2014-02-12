/*Gets the list of friends of a user and send it to the list adapter for display. */
package com.example.getimages;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;

public class GetFriendList extends ListActivity {
	ArrayList usersListMain=new ArrayList();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.activity_get_friend_list);
		Intent i = getIntent();
		String searched  = i.getStringExtra("input");
		getFriendList(searched);
	    
	//	((ListAdapter) getListView().getAdapter()).notifyDataSetChanged();      
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.get_friend_list, menu);
		return true;
	}
    private void getFriendList(final String searched){
    	final ArrayList usersList=new ArrayList();
		//Query to get the list of friends of the user
    	String fqlQuery = "SELECT uid, name, pic_square FROM user WHERE uid IN " +
            "(SELECT uid2 FROM friend WHERE uid1 = me())";
      
    	Log.d("FQL",fqlQuery);
        Bundle params = new Bundle();
        params.putString("q", fqlQuery);
        Session session = Session.getActiveSession();
        Log.d("session",session.toString());
        Request request = new Request(session,
            "/fql",                         
            params,                         
            HttpMethod.GET,                 
            new Request.Callback(){         
        		//On completion of the request extract the needed data and use the Custom Adapter for the display.
				@Override
				public void onCompleted(Response res) {
					try{
						// TODO Auto-generated method stub
						Log.d("result1", "Result1: " + res.toString());													
						Log.i("result1", "Result1: " + res.toString());
						Log.d("state ","asdf");
						String result1 = "{Response:  {responseCode: 200, graphObject: GraphObject{graphObjectClass=GraphObject, state={\"data\":[{\"owner\":100004727378732,\"caption\":\"\",\"src\":\"https://fbcdn-photos-a-a.akamaihd.net/hphotos-ak-frc3/1441561_235116783322566_1102217397_s.jpg\",\"created\":1384580863,\"object_id\":235116783322566},{\"owner\":100004727378732,\"caption\":\"\",\"src\":\"https://fbcdn-photos-e-a.akamaihd.net/hphotos-ak-ash3/1395814_235116439989267_2032024064_s.jpg\",\"created\":1384580744,\"object_id\":235116439989267},{\"owner\":100004727378732,\"caption\":\"\",\"src\":\"https://fbcdn-photos-h-a.akamaihd.net/hphotos-ak-prn2/8660_235116299989281_939401260_s.jpg\",\"created\":1384580665,\"object_id\":235116299989281},{\"owner\":100004727378732,\"caption\":\"\",\"src\":\"https://fbcdn-photos-a-a.akamaihd.net/hphotos-ak-ash3/600887_235116059989305_749987324_s.jpg\",\"created\":1384580540,\"object_id\":235116059989305}]}}, error: null, isFromCache:false}}";
						int indexState = res.toString().indexOf("state");
						int indexError = res.toString().lastIndexOf("error");
						
						String jsonStr = res.toString().substring(indexState+6, indexError-3);
						Log.d("Str ",jsonStr);
						
						JSONObject json = new JSONObject(jsonStr);
						JSONArray data = json.getJSONArray("data");
						Log.d("dataleng : ",Integer.toString(data.length()));
							
						for(int i=0; i< data.length(); i++){
							JSONObject job = data.getJSONObject(i);
							String uid=job.getString("uid");
							String name=job.getString("name");
							String imageURL=job.getString("pic_square");
							if(name.contains(searched))
								usersList.add(new User(uid,name,imageURL));
							Log.d("uid : ",job.getString("uid"));
							Log.d("name : ",job.getString("name"));
						}
						//Use of the Custom Adapter
						ListAdapter customAdapter = new ListAdapter(getApplicationContext(), R.layout.listrow,usersList);
				        setListAdapter(customAdapter);
					}					
					catch(Exception e){
						
					}
				}                  
        }); 
        Request.executeBatchAsync(request);           	        
    }

}


