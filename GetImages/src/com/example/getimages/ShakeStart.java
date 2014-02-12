package com.example.getimages;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

public class ShakeStart extends Activity implements Shaker.Callback {
  private Shaker shaker=null;
  private TextView transcript=null;
  private ScrollView scroll=null;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.demo);
     
    transcript=(TextView)findViewById(R.id.transcript);
    scroll=(ScrollView)findViewById(R.id.scroll);    
    shaker=new Shaker(this, 1.25d, 500, this);
  }
  
  @Override
  public void onDestroy() {
    super.onDestroy();
    
    shaker.close();
  }
  
  public void shakingStarted() {
    Log.d("ShakerDemo", "Shaking started!");
    Context context = getApplicationContext();
    CharSequence text = "start toast!";
    int duration = Toast.LENGTH_SHORT;
    scroll.fullScroll(View.FOCUS_DOWN);
    
    Intent intent = new Intent(this, FriendPickerSampleActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    intent.setAction(Intent.ACTION_MAIN);
    intent.addCategory(Intent.CATEGORY_LAUNCHER);
    startActivity(intent);
  }
  
  public void shakingStopped() {
    Log.d("ShakerDemo", "Shaking stopped!");
    Context context = getApplicationContext();
    CharSequence text = "stop toast!";
    int duration = Toast.LENGTH_SHORT;
    scroll.fullScroll(View.FOCUS_DOWN);
  }
}
