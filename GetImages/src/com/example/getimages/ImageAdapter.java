package com.example.getimages;


import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import com.example.getimages.ListAdapter.ImageSomething;

import android.R.drawable;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
 
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    public ArrayList<String> strArr = new ArrayList<String>();
    public ImageAdapter(Context c, ArrayList<String> strarr){
    	this.strArr = strarr;
        mContext = c;
		Log.d("stringArr",strarr.size()+"");
	     
    }
	public class ImageThread extends AsyncTask<String,Void,Drawable>{
		ImageView v;
		public ImageThread(ImageView v){
			
			Log.d("inside image thread const","");	    	
			System.out.println("inside image thread const");
			this.v=v;
		}
		@Override
		protected Drawable doInBackground(String... strings){
			try {
	    		Log.d("try of doinbckg","");
	            InputStream is = (InputStream) new URL(strings[0]).getContent();
	            Drawable d = Drawable.createFromStream(is, "src name");
	            return d;	            
	        } catch (Exception e) {
	    		Log.d("catch","");
	        	e.printStackTrace();	    	    
	        	return null;
	        }			
		}
		protected void onPostExecute(Drawable d){			
			v.setImageDrawable(d);
			Log.d("on post exec","");
	    	
		}
	}

    // Constructor
    public ImageAdapter(Context c){
        mContext = c;
    }
 
    @Override
    public int getCount() {
    	return strArr.size();
        //return mThumbIds.length;
    }
 
    @Override
    public Object getItem(int position) {
        //return mThumbIds[position];
    	return strArr.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return 0;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	ImageView imageView;
    	if(convertView == null){
	        imageView = new ImageView(mContext);
	        Log.d("after " +
	        		"" +
	        		" set Image","");
		    
	        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	        imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
        }
    	else{
    		Log.d("else before set Image to convert","");
    		imageView = (ImageView) convertView;    		
    	}
    	try {
    		Log.d("inside try of getView","");        	
			new ImageThread(imageView).execute(strArr.get(position));							   
		} 
    	catch (Exception e) {
			e.printStackTrace();
		}
    	Log.d("before return imageview","");
    	return imageView;    	
    }
    public Drawable drawableFn(String s){
    	
    	try {
    		Log.d("try","");
            InputStream is = (InputStream) new URL(s).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
    		Log.d("catch","");
        	e.printStackTrace();
    		//System.exit(0);
            return null;
        }	
    }
}

