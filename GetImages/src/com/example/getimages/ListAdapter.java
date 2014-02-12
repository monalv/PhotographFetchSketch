/* ListAdapter is used to display the the Friends of the user. Also on Click on one of the friends, it gets the images where the user and his friend is tagged together and send those to GridActivity.
*/

package com.example.getimages;


import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListAdapter extends ArrayAdapter<User> {
	Context contexts;
    private static final int ACTIVITY_EDIT=1;
    public ListAdapter(Context context, int textViewResourceId) {
	    super(context, textViewResourceId);
	    // TODO Auto-generated constructor stub
	}
	
	private List<User> items;
	public ListAdapter(Context context, int resource, List<User> items) {
	    super(context, resource, items);
	    this.items = items;
	    contexts=context;
	    Log.d("inside constructor"," ");
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {	
	    View v = convertView;
	    Log.d("inside the view method","");
	    if (v == null) {
	        LayoutInflater vi;
	        vi = LayoutInflater.from(getContext());
	        v = vi.inflate(R.layout.listrow, null);
	        Log.d(" v==null inflated ","");
	        
	       LinearLayout layout = (LinearLayout) v.findViewById(R.id.row);
	       layout.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	String name = items.get(position).getName();
	            	String id = items.get(position).getUid();
	            	Log.d("id",id);		            
	            	Log.d("name",name);
	            	Context c = getContext();
	            	getImageList(id,c);       
	            } 	            
	        });	        
	    }
	    User p = items.get(position);
	    if (p != null) {
	        TextView tt = (TextView) v.findViewById(R.id.textView1);
	        ImageView iv=(ImageView)v.findViewById(R.id.imageView1);
		   		        
			
	        //tt.setTextColor(Color.parseColor("#FFFFF"));
	        Log.d(" inside p!=null ","");
	        
	        if (tt != null) {
	            tt.setText(p.getName());
	            Log.d("name : ",p.getName());
	        }
	        if(iv !=null){
	        	URL url;
				try {
					new ImageSomething(v).execute(p.getUrl());							   
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
	    }	    
	    return v;
	}
	public class ImageSomething extends AsyncTask<String,Void,Bitmap>{
		
		View v;
		public ImageSomething(View v){
			this.v=v;
		}
		@Override
		protected Bitmap doInBackground(String... strings){
			Bitmap bmp=null;
			URL url;
			try {
				url = new URL(strings[0]);
				bmp= BitmapFactory.decodeStream(url.openConnection().getInputStream());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}					
			return bmp;	
			//return bmp;
		}
		protected void onPostExecute(Bitmap bmp){
			
			ImageView iv=(ImageView)v.findViewById(R.id.imageView1);
			iv.setImageBitmap(bmp);
		}
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

		//  super.onActivityResult(requestCode, resultCode, intent);
        Log.d("getting called ","on activity result");
      
        //  fillData();
    }
	private void getImageList(String id,final Context c){
		final ArrayList strArr=new ArrayList();
 //   	String fqlQuery = "SELECT uid, name, pic_square FROM user WHERE uid IN " +
//            "(SELECT uid2 FROM friend WHERE uid1 = me() LIMIT 25)";
      
      	String fqlQuery = "SELECT object_id, src, owner, caption, created FROM photo WHERE object_id IN "+
    	    	"(SELECT object_id FROM photo_tag WHERE subject = me()) AND object_id IN "+
    	    	"(SELECT object_id FROM photo_tag WHERE subject = '"+id+"')";
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
							strArr.add(job.getString("src"));
							Log.d("src : ",job.getString("src"));
						}										
						Log.d("before intent", "");		                
						
						Intent intent= new Intent(contexts, GridViewActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						Log.d("put extr", "");		                
						intent.putStringArrayListExtra("imagelist",strArr);
		                Log.d("before start GridView", "");
		                contexts.startActivity(intent);
		                Log.d("after start GridView", "");
					}					
					catch(Exception e){
						e.printStackTrace();
					}
				} 
				
        }); 
        Request.executeBatchAsync(request);           	        
    }
}