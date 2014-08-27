package jnbclick.palabraungida;

 
 
import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class VozActivity extends Activity 
 {
    /** Called when the activity is first created. */
     

 
	private Button Bstream;
	private Button Bstop;
    private VozActivity esto = this;
    private   Intent svc; 

	private TextView Bmensa;
    

    /**
     * 
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.mediaplayer_b);
        Bstream = (Button) findViewById(R.id.butstream);
        Bstream.setOnClickListener(BstreamListener);
        Bstop = (Button) findViewById(R.id.butstop);
        Bstop.setOnClickListener(BstopListener);
         
        Bstop.setVisibility(View.GONE);
        Bmensa = (TextView) findViewById(R.id.label);
       
   }
        	
    private OnClickListener BstreamListener = new OnClickListener() {
        public void onClick(View v) {
          	 Bmensa.setText("   ");
             
        	svc  = new Intent(esto, playerservice.class); 
        	  startService(svc);
        	
        	     
        	     Bstop.setVisibility(View.VISIBLE);
        	     Bstream.setVisibility(View.GONE);}
                     
    };
    private OnClickListener BstopListener = new OnClickListener() {
        public void onClick(View v) {
        	 Bmensa.setText("Transmsion Finalizada");
        	    stopService(svc);
            	  Bstream.setVisibility(View.VISIBLE);
               Bstop.setVisibility(View.GONE);}
            
                      
    };

   
    
    
}
