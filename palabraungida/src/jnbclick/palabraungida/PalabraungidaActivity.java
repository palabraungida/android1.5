package jnbclick.palabraungida;

 
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ProgressBar;
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
    private boolean updateLink;
    private String parmUrl;
    private String tvUrl;
    private String audioUrl;
    private String podCast;
    private String telUrl;
    
    private ProgressBar  mProgress;
       
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
        updateLink = false;
       
    	  }
    protected  void onStart ()
    { super.onStart();
    	if(updateLink==false)
    {  mProgress = (ProgressBar) findViewById(R.id.ProgressBar);
        mProgress.setVisibility(View.VISIBLE);
       String SetUrl = "http://www.canal.palabraungidatv.com/conexcust/datapps.php?nid=23&prot=rtsp";
           myClickHandler(SetUrl);
          
          
     }
    	
    	
    }
  //play audio  
    private OnClickListener BstreamListener = new OnClickListener() {
        public void onClick(View v) {
          	 Bmensa.setText("   ");
          /*	Intent intent =
                    new Intent(PalabraungidaActivity.this.getApplication(),
                            webactivity.class);
              Bundle b = new Bundle();
              b.putString("key", audioUrl);
             intent.putExtras(b);
             startActivity(intent); */
          	 
        	svc  = new Intent(esto, playerservice.class); 
        	  startService(svc); 
        	
        	     
        	     Bstop.setVisibility(View.VISIBLE);
        	     Bstream.setVisibility(View.GONE);
        	     //barmsgnotification();
        	     }
                     
    };
    
    
    //play video
    private OnClickListener mStreamVideoListener = new OnClickListener() {
        public void onClick(View v) {
            if(checkConnectivity (2))
             {  /*Intent intent =
             new Intent(PalabraungidaActivity.this.getApplication(),
                     webactivity.class);
       Bundle b = new Bundle();
       b.putString("key", tvUrl);
      intent.putExtras(b);
      startActivity(intent);} */
            	
            	Intent intent =
                    new Intent(PalabraungidaActivity.this.getApplication(),
                           videoActivity.class);
            intent.putExtra(MEDIA, RESOURCES_AUDIO);
            startActivity(intent);} 
            else 
            	Bmensa.setText("No WIFI network connection available.");
               
        }
    };
    
    //trae los sermones
    private OnClickListener mworldwtoListener = new OnClickListener() {
        public void onClick(View v) {
        	 if(checkConnectivity (2))
        	 {
             Intent intent =
                    new Intent(PalabraungidaActivity.this.getApplication(),
                           webactivity.class);
             Bundle b = new Bundle();
             b.putString("key", podCast);
            intent.putExtras(b);
            startActivity(intent);}
        	 else
        	 {
        		 Bmensa.setText("No WIFI network connection available."); 
        	 }
           
        }
    };
  //cambia los cambios del boton
    private OnClickListener BstopListener = new OnClickListener() {
        public void onClick(View v) {
        	 Bmensa.setText("Transmsion Finalizada");
        	   // stopService(svc);
            	  Bstream.setVisibility(View.VISIBLE);
               Bstop.setVisibility(View.GONE);
              // mNotificationManager.cancel(HELLO_ID); 
        }
                   
        };
        
        //hace la llamada
        private OnClickListener mphonecallListener = new OnClickListener() {
            public void onClick(View v) {
            	try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    String tempTel = "tel:"+telUrl;
                    callIntent.setData(Uri.parse(tempTel));
                    startActivity(callIntent);
                } catch (ActivityNotFoundException activityException) {
                     Log.e(TAG,"helloandroid dialing example");
                }
            };
            
        };
        
        //envia el email
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
            
            //check if cameback from activity
            public void onBackPressed() {
            	
               if(Bstop.getVisibility() ==View.VISIBLE)
                  {Bstop.setVisibility(View.VISIBLE);}
             if(Bstream.getVisibility()==View.VISIBLE)
         	  {Bstop.setVisibility(View.GONE);}
                
            
            	}
            
            // check network status and ask for links sermons and streaming tv ans radio
            
            // When user clicks button, calls AsyncTask.
    	    // Before attempting to fetch the URL, makes sure that there is a network connection.
 
        public boolean checkConnectivity (int typeCheck)
            {  ConnectivityManager connMgr = (ConnectivityManager) 
            getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            switch (typeCheck)
            {case 1:
            	if (networkInfo != null && networkInfo.isConnected()) {
            			return (true); 
            		} else {return (false);}
            case 2:
            	boolean isWiFi = networkInfo.getType() == connMgr.TYPE_WIFI;
            	if(isWiFi)
            	return true;
            	else return false;
            default: return false;		
            }
           }
            public void myClickHandler(String st ) {
    	        // Gets the URL from the UI's text field.
            	String stringUrl = st;
    	       if(checkConnectivity(1)){
    	            new DownloadWebpageTask().execute(stringUrl);
    	           
    	        } else {
    	            Bmensa.setText("No network connection available.");
    	            mProgress.setVisibility(View.GONE);
    	        }
    	    }

    	     // Uses AsyncTask to create a task away from the main UI thread. This task takes a 
    	     // URL string and uses it to create an HttpUrlConnection. Once the connection
    	     // has been established, the AsyncTask downloads the contents of the webpage as
    	     // an InputStream. Finally, the InputStream is converted into a string, which is
    	     // displayed in the UI by the AsyncTask's onPostExecute method.
    	     private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
    	        @Override
    	        protected String doInBackground(String... urls) {
    	              
    	            // params comes from the execute() call: params[0] is the url.
    	            try {
    	                return downloadUrl(urls[0]);
    	            } catch (IOException e) {
    	                return "Unable to retrieve web page. URL may be invalid.";
    	            }
    	        }
    	        // onPostExecute displays the results of the AsyncTask.
    	        @Override
    	        protected void onPostExecute(String result) {
    	        	String[] separated = result.split("@");
    	            mProgress.setVisibility(View.GONE);
    	            audioUrl = separated[0].trim();
    	            tvUrl    = separated[1];
    	            podCast  = separated[2];
    	            telUrl   = separated[3]; 
    	            updateLink= true;
    	          
    	            
    	       }
    	    }
    	     private String downloadUrl(String myurl) throws IOException {
    	    	    InputStream is = null;
    	    	    // Only display the first 500 characters of the retrieved
    	    	    // web page content.
    	    	    int len = 500;
    	    	        
    	    	    try {
    	    	        URL url = new URL(myurl);
    	    	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    	    	        conn.setReadTimeout(10000 /* milliseconds */);
    	    	        conn.setConnectTimeout(15000 /* milliseconds */);
    	    	        conn.setRequestMethod("GET");
    	    	        conn.setDoInput(true);
    	    	        // Starts the query
    	    	        conn.connect();
    	    	        int response = conn.getResponseCode();
    	    	        Log.d("checkMensa", "The response is: " + response);
    	    	        is = conn.getInputStream();

    	    	        // Convert the InputStream into a string
    	    	        String contentAsString = readIt(is, len);
    	    	        return contentAsString;
    	    	        
    	    	    // Makes sure that the InputStream is closed after the app is
    	    	    // finished using it.
    	    	    } finally {
    	    	        if (is != null) {
    	    	            is.close();
    	    	        } 
    	    	    }
    	    	}
    	  // Reads an InputStream and converts it to a String.
    	     public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
    	         Reader reader = null;
    	         reader = new InputStreamReader(stream, "UTF-8");        
    	         char[] buffer = new char[len];
    	         reader.read(buffer);
    	         return new String(buffer);
    	     }


}
