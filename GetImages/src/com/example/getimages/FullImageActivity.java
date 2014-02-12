/*On click of one of the images from the image grid this Activity gets called.
 * It has a drawer layout associated so as to display the categories as a vertical drawer.
 */
package com.example.getimages;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.MeasureSpec;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class FullImageActivity extends Activity implements ColorPickerDialog.OnColorChangedListener{
	 private String[] mPlanetTitles  = {"Birthday","Christmas","Meet&Greet","Concern Wishes","Love","Friends Forever","Best Wishes"};
	 private DrawerLayout mDrawerLayout;
	 private ListView mDrawerList;
	 private ActionBarDrawerToggle mDrawerToggle;
	 private CharSequence mDrawerTitle;
	 private CharSequence mTitle;
	 private Drawable dr;
	 Paint myPaint=new Paint();
		
    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);
        int color=Color.WHITE;
		myPaint.setColor(color);
		myPaint.setTextSize(15);
		
        System.out.println("just entered oncreate in full image activity");
        
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_list_item, mPlanetTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mTitle = mDrawerTitle = getTitle();
        //mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
        
            /** Called when a drawer has settled in a completely closed state. */
            @SuppressLint("NewApi")
			public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            @SuppressLint("NewApi")
			public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.onDrawerOpened(mDrawerLayout);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        
        Intent i = getIntent();
        int position = i.getExtras().getInt("id");
        ArrayList<String> strArr=i.getExtras().getStringArrayList("array_list");
        System.out.println("position in full image activity"+position);
        ImageAdapter imageAdapter = new ImageAdapter(this,strArr);
        System.out.println(imageAdapter.strArr.size());
        final ImageView imageView = (ImageView) findViewById(R.id.full_image_view);
        //TextView tv = (TextView) findViewById(R.id.textView2);
        Button buttonUndo = (Button) findViewById(R.id.buttonUndo);
        buttonUndo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            if(dr!=null)	
            imageView.setImageDrawable(dr);
            }
        });
        
		Button buttonDone = (Button) findViewById(R.id.buttonsend);
		buttonDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            save();            
            }
        });
        
        try {        	
			new FullImageThread(imageView).execute(strArr.get(position));							   
		} 
    	catch (Exception e) {
			e.printStackTrace();
		}
        //   imageView.setImageDrawable(imageAdapter.drawableFn(imageAdapter.strArr.get(position)));
        
    }
    

    private static final int COLOR_MENU_ID = Menu.FIRST;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
       
        menu.add(0, COLOR_MENU_ID, 0, "Color").setShortcut('3', 'c');

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Log.i("TAG","onitemselected");
        myPaint.setXfermode(null);
        myPaint.setAlpha(0xFF);

        switch (item.getItemId()) {
            case COLOR_MENU_ID:
            	new ColorPickerDialog(this, this, myPaint.getColor()).show();
            	System.out.println("color"+myPaint.getColor());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void save(){

		ImageView imgv=(ImageView) findViewById(R.id.full_image_view);
		/*start saving*/
		imgv.setDrawingCacheEnabled(true);
		imgv.setDrawingCacheEnabled(true);
		System.out.println("after setting the cache enabled to true");
		 
        
		imgv.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), 
		            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		System.out.println("after setting the measure");
		
		imgv.layout(0, 0, imgv.getMeasuredWidth(), imgv.getMeasuredHeight()); 		
		imgv.buildDrawingCache(true);
		System.out.println("after buildDrawingCache");
		if(imgv.getDrawingCache() == null)
			System.out.println("imgv is null");
		Bitmap b = Bitmap.createBitmap(imgv.getDrawingCache());
		System.out.println("before set drawing cache enabled");
		
		imgv.setDrawingCacheEnabled(false); // clear drawing cache
		System.out.println("set drawing cache enabled");
		
		String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
	    OutputStream outStream = null;
	    final File file = new File(extStorageDirectory, "er"+Math.random()+".PNG");
	    try {
	     outStream = new FileOutputStream(file);
	     b.compress(Bitmap.CompressFormat.PNG, 100, outStream);
	     outStream.flush();
	     outStream.close();
	    }
	    catch(Exception e)
	    {
	    	System.out.println("exc");
	    }
	    Intent intent = new Intent(this, SendActvity.class);
        intent.putExtra("input", file);
        intent.putExtra("Bitmap",b);
        startActivity(intent);

    }
    
    protected void onResume() {
		super.onResume();
		mDrawerLayout.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				mDrawerLayout.openDrawer(Gravity.LEFT);
			}
		}, 1000);
	}
    
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
       // If the nav drawer is open, hide action items related to the content view
       boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
       return super.onPrepareOptionsMenu(menu);
    }
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //selectItem(position);
        	Log.d("position",""+position);
        	updateScrollView(position);
        	mDrawerLayout.closeDrawers();
        }
    }
    
    @SuppressLint("NewApi")
	public void updateScrollView(int pos){
    	HorizontalScrollView hsv = (HorizontalScrollView) findViewById(R.id.scrollLayout);
    	hsv.setVisibility(HorizontalScrollView.VISIBLE);
    	LinearLayout linear = (LinearLayout) findViewById(R.id.linearLayout);
    	String arr1[][] = {{"Happy Birthday!!","You Hit 25","Birthday!!","Get Drunk 21","1 Year Old","Cake Splash"},
    						 {"Merry Christmas","Santa Time","Christmas","Carol Time","Candy Time","Christmas Wishes"},
    						 {"Meet At 6","dinner at 7?","Date with me?","Game Evening","night out?","Dinner Tonight"},
    						 {"Get Well Soon","Get Better ASAP","Take Care","Happy Healing","My Wish For You","Praying For You"},
    						 {"I Love You","Miss You","Get Back Soon","My Love","Hugs","Kisses"},
    						 {"Friends Forever","Best Friends","Forever Friends","Special Friend","BFF","Friendship Day"},
    						 {"All The Best","Good Luck","Best Wishes","Be Good Do Good","Move On","Hearty Wishes"}};
    	
    			 System.out.println("pos"+pos);    			 
    			 for(int j=0;j<6;j++){
    				 TextView t =  (TextView)linear.getChildAt(j);
    				 t.setText(arr1[pos][j]);
    				 t.setOnTouchListener(new MyTouchListener(arr1[pos][j]));
    				    findViewById(R.id.full_image_view).setOnDragListener(new MyDragListener());
    			 }    
    }
    private final class MyTouchListener implements OnTouchListener {
    	public String data="";
        public MyTouchListener(String data) {
        	this.data = data;
			// TODO Auto-generated constructor stub
		}
    	@SuppressLint("NewApi")
		public boolean onTouch(View view, MotionEvent motionEvent) {
          if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData clipdata = ClipData.newPlainText("data",(CharSequence) data);
            DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(clipdata, shadowBuilder, view, 0);
            return true;
          } else {
            return false;
          }
        }
      }
    
    @SuppressLint("NewApi")
	class MyDragListener implements OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
          int action = event.getAction();
          switch (event.getAction()) {
          case DragEvent.ACTION_DRAG_STARTED:
        	  Button buttonUndo = (Button) findViewById(R.id.buttonUndo);
        	  buttonUndo.callOnClick();
            break;
          case DragEvent.ACTION_DRAG_ENTERED:
            break;
          case DragEvent.ACTION_DRAG_EXITED:
            break;
          case DragEvent.ACTION_DROP:
            // Dropped, reassign View to ViewGroup
            System.out.println("clipdata"+event.getClipData().getItemAt(0).getText());
            
            CustomSave((String) event.getClipData().getItemAt(0).getText(),event.getX(),event.getY());
        	  break;
          case DragEvent.ACTION_DRAG_ENDED:
          default:
            break;
          }
          return true;
        }
      }
 
    
    
    
    public void CustomSave(String content,float x,float y){
    	BitmapFactory.Options myOpt = new BitmapFactory.Options();
    	myOpt.inDither=true;
    	myOpt.inScaled=false;
    	myOpt.inPreferredConfig=Bitmap.Config.ARGB_8888;
    	myOpt.inDither=false;
    	myOpt.inPurgeable=true;
    	
    	
		Bitmap img= BitmapFactory.decodeResource(getResources(), R.drawable.bg);
		System.out.println("size ");
		
		ImageView imgv1=(ImageView) findViewById(R.id.full_image_view);
		BitmapDrawable drawable = (BitmapDrawable) imgv1.getDrawable();		
		Bitmap img3 = drawable.getBitmap();
		dr=imgv1.getDrawable();
		ImageView imgv2=(ImageView) findViewById(R.id.full_image_view);
		
		System.out.println("size "+img3.getHeight()+" "+img3.getWidth());
		Bitmap img1=Bitmap.createBitmap(img3.getWidth(),img3.getHeight(),Bitmap.Config.ARGB_8888);
		
		Canvas c=new Canvas(img1);
		c.drawBitmap(img3,0,0,null);
		
		c.drawText(content, 20,20,myPaint);
		
		ImageView imgv=(ImageView) findViewById(R.id.full_image_view);
		System.out.println("after getting the image view");
		
		imgv.setImageDrawable(new BitmapDrawable(getResources(),img1));
		System.out.println("after setting the image drawable");   
    }
    
    public class FullImageThread extends AsyncTask<String,Void,Drawable>{
		
    	ImageView v;
		public FullImageThread(ImageView v){
			Log.d("inside image thread const full image","");	    	
			System.out.println("inside image thread const full image");
			this.v=v;
		}
		@Override
		protected Drawable doInBackground(String... strings){
			try {
	    		Log.d("try of doinbckg full image","");
	    		System.out.println("do in bkg full image");
				
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
			System.out.println("on post exec");			
			v.setImageDrawable(d);
			System.out.println("on post exec after set");						
			Log.d("on post exec","");	    	
		}
	}

    public void colorChanged(int color) {
    	System.out.println("color changed");
    	Log.i("TAG","colorchanged");
        myPaint.setColor(color);
        setNewColorTextViews(color);
        
    }
    
    public void setNewColorTextViews(int color){
    	LinearLayout linear = (LinearLayout) findViewById(R.id.linearLayout);
    	 for(int j=0;j<6;j++){
    				 TextView t =  (TextView)linear.getChildAt(j);
    				 t.setTextColor(color);
    			 } 	
    	}
}

