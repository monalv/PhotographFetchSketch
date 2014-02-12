/*Send Activity sends the customized image using the applications installed on the phone.
*/

package com.example.getimages;

import java.io.File;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class SendActvity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_actvity);
		System.out.println("entered the send activity");
		ImageView im1 = (ImageView) findViewById(R.id.imageViewfinal);
		
		
		System.out.println("after setting image");
		Intent i = getIntent();
		Bitmap b=(Bitmap)i.getParcelableExtra("Bitmap");
		im1.setImageBitmap(b);
		final File f = (File) i.getExtras().get("input");
		/*send saving*/
		System.out.println("after gettting intent");
		Button but = (Button) findViewById(R.id.button1);
		but.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent picMessageIntent=new Intent(android.content.Intent.ACTION_SEND);
         		picMessageIntent.setType("image/jpeg");
         		picMessageIntent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(f));
         		startActivity(Intent.createChooser(picMessageIntent,"Send picture using :"));         		
         		         	   
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.send_actvity, menu);
		return true;
	}

}
