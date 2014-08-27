package jnbclick.palabraungida;

 
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class PalabraungidaActivity extends Activity {

     private Button mstreamvideo;
     private static final String MEDIA = "media";
    private static final int LOCAL_AUDIO = 1;
    private static final int STREAM_AUDIO = 2;
    private static final int RESOURCES_AUDIO = 3;
    private static final int LOCAL_VIDEO = 4;
    private static final int STREAM_VIDEO = 5;
    private Button Bstream;
    private Button Bstop;
    private Button mphone_call;
    private Button memailto;
    private Button mworldw;
    private ProgressBar mprogress;
       
    private static final String TAG = null;
    
     private PalabraungidaActivity esto = this;
    private   Intent svc; 
 	private TextView Bmensa;
    private static final int HELLO_ID = 1175;
	private NotificationManager mNotificationManager;
	

	 public void barmsgnotification()
	  {String ns = Context.NOTIFICATION_SERVICE;
	  mNotificationManager = (NotificationManager) getSystemService(ns);
	  int icon = R.drawable.ic_pu;
	  CharSequence tickerText = "Palabra Ungida Audio";
	  long when = System.currentTimeMillis();

	  Notification notification = new Notification(icon, tickerText, when);
	  Context context = getApplicationContext();
	  CharSequence contentTitle = "Palabra Ungida audio";
	  CharSequence contentText = "Tap para retomar el audio !";
	  Intent notificationIntent = new Intent(context, PalabraungidaActivity.class);
	  notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
	  notificationIntent.setAction(Intent.ACTION_MAIN);
	  PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,   PendingIntent.FLAG_UPDATE_CURRENT);

	  notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);	
	  notification.contentIntent= contentIntent;
	  notification.flags= Notification.FLAG_ONGOING_EVENT;
	  mNotificationManager.notify(HELLO_ID, notification);	
	  }	    

    @Override
    protected void onCreate(Bundle icicle) {
        // TODO Auto-generated method stub
        super.onCreate(icicle);
        setContentView(R.layout.main);
        Bstream = (Button) findViewById(R.id.butstream);
        Bstream.setOnClickListener(BstreamListener);
        Bstop = (Button) findViewById(R.id.butstop);
        Bstop.setOnClickListener(BstopListener);
         
        Bstop.setVisibility(View.GONE);
        Bmensa = (TextView) findViewById(R.id.label);
        mstreamvideo = (Button) findViewById(R.id.streamvideo);
        mstreamvideo.setOnClickListener(mStreamVideoListener);
        mphone_call = (Button) findViewById(R.id.phonecall);
        mphone_call.setOnClickListener(mphonecallListener);
        memailto = (Button) findViewById(R.id.emailto);
        memailto.setOnClickListener(memailtoListener);
        mworldw = (Button) findViewById(R.id.worldw);
        mworldw.setOnClickListener(mworldwtoListener);
        Intent intent = new Intent(esto, CheckNetGetUrlActivity.class);
      
     }
  
    private OnClickListener BstreamListener = new OnClickListener() {
        public void onClick(View v) {
          	 Bmensa.setText("   ");
          	 
        	svc  = new Intent(esto, playerservice.class); 
        	  startService(svc);
        	
        	     
        	     Bstop.setVisibility(View.VISIBLE);
        	     Bstream.setVisibility(View.GONE);
        	     barmsgnotification();}
                     
    };
    private OnClickListener mStreamVideoListener = new OnClickListener() {
        public void onClick(View v) {
            
             Intent intent =
                    new Intent(PalabraungidaActivity.this.getApplication(),
                           videoActivity.class);
            intent.putExtra(MEDIA, RESOURCES_AUDIO);
            startActivity(intent);
           
        }
    };
    private OnClickListener mworldwtoListener = new OnClickListener() {
        public void onClick(View v) {
            
             Intent intent =
                    new Intent(PalabraungidaActivity.this.getApplication(),
                           webactivity.class);
            intent.putExtra(MEDIA, RESOURCES_AUDIO);
            startActivity(intent);
           
        }
    };
  
    private OnClickListener BstopListener = new OnClickListener() {
        public void onClick(View v) {
        	 Bmensa.setText("Transmsion Finalizada");
        	    stopService(svc);
            	  Bstream.setVisibility(View.VISIBLE);
               Bstop.setVisibility(View.GONE);
               mNotificationManager.cancel(HELLO_ID); 
        }
                   
        };
        private OnClickListener mphonecallListener = new OnClickListener() {
            public void onClick(View v) {
            	try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:2138407500"));
                    startActivity(callIntent);
                } catch (ActivityNotFoundException activityException) {
                     Log.e(TAG,"helloandroid dialing example");
                }
            };
            
        };
            private OnClickListener memailtoListener = new OnClickListener() {
                public void onClick(View v) {
                	Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);  
                	String aEmailList[] = { "edgarcalderon7777@gmail.com"};   
                	   
                	emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);  
                 
                	emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "desde mi telefono");  
                	  
                	emailIntent.setType("plain/text");  
                	emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "saludos   desde mi telefono ");  
                	emailIntent.setType("plain/text");  
                	startActivity(Intent.createChooser(emailIntent, "Send your email in:"));  
                	 
                   
                }
            }; 
            public void onBackPressed() {
            	
               if(Bstop.getVisibility() ==View.VISIBLE)
                  {Bstop.setVisibility(View.VISIBLE);}
             if(Bstream.getVisibility()==View.VISIBLE)
         	  {Bstop.setVisibility(View.GONE);}
                
            
            	}

}
