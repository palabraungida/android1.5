package jnbclick.palabraungida;

 

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;


public class videoActivity extends Activity implements
        OnBufferingUpdateListener, OnCompletionListener,
        OnPreparedListener, OnVideoSizeChangedListener,SurfaceHolder.Callback{

    private static final String TAG = "videoActivity";
    private int mVideoWidth;
    private int mVideoHeight;
    private MediaPlayer mMediaPlayer;
    private SurfaceView mPreview;
    private SurfaceHolder holder;
    private String path;
    private Bundle extras;
    private static final String MEDIA = "media";
    private boolean mIsVideoSizeKnown = false;
    private boolean mIsVideoReadyToBePlayed = false;
    private boolean bufering =true;
    private int intents = 0;
    private int intentsbufer =0;
    private NotificationManager mNotificationManager ;
    private static final int NOTIF_ID = 1174;
    ViewFlipper flipper;
    
	private Animation inFromRightAnimation() {
	Animation inFromRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, +1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
	inFromRight.setDuration(500);
	inFromRight.setInterpolator(new AccelerateInterpolator());
	return inFromRight;}
	
	private Animation outToLeftAnimation() {
		Animation outtoLeft = new TranslateAnimation(
		Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f,
		Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoLeft.setDuration(500);
		outtoLeft.setInterpolator(new AccelerateInterpolator());
		return outtoLeft;}
	
	private Animation inFromLeftAnimation() {
		Animation inFromLeft = new TranslateAnimation(
		Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
		Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromLeft.setDuration(500);
		inFromLeft.setInterpolator(new AccelerateInterpolator());
		return inFromLeft;}
	
	private Animation outToRightAnimation() {
		Animation outtoRight = new TranslateAnimation(
		Animation.RELATIVE_TO_PARENT,0.0f, Animation.RELATIVE_TO_PARENT,+1.0f,
		Animation.RELATIVE_TO_PARENT,0.0f, Animation.RELATIVE_TO_PARENT,0.0f);
		outtoRight.setDuration(500);
		outtoRight.setInterpolator(new AccelerateInterpolator());
		return outtoRight;}

    /**
     * 
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        setContentView(R.layout.mediaplayer_2);
        mPreview = (SurfaceView) findViewById(R.id.surface);
       holder = mPreview.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
         extras = getIntent().getExtras(); 
          

    }
    
    public void Broadcastmsg(String msg) {
 	   LayoutInflater inflater = (LayoutInflater)  getSystemService
 		      (LAYOUT_INFLATER_SERVICE);
 	  View layout = inflater.inflate(R.layout.toast_layout,
               null);

 	  ImageView image = (ImageView) layout.findViewById(R.id.image);
 	  image.setImageResource(R.drawable.ic_pu);
 	  TextView text = (TextView) layout.findViewById(R.id.text);
 	   text.setText(msg);
 	  ProgressBar probar = (ProgressBar) layout.findViewById(R.id.progbar);
 	  
 	  Toast toast = new Toast(getApplicationContext());
 	  toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
 	  toast.setDuration(Toast.LENGTH_LONG);
 	  toast.setView(layout);
 	  toast.show();
 	 
 }

    private void playVideo() {
        doCleanUp();
        releaseMediaPlayer();
        Broadcastmsg("Looking for transmission ");
        try {              
             //path = "rtsp://canalungidotv.com:1935/tran/live1_160p";
             path = "http://m.ustream.tv/channel/ungidatv";
            // Create a new media player and set the listeners
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setVolume(100, 100);
            
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.setDisplay(holder);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnVideoSizeChangedListener(this);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); }
        catch (Exception e) {
        	Broadcastmsg("general system fault: " + e.getMessage());
            Log.e(TAG, "general system fault: " + e.getMessage(), e);
            
        }


       
        
    }

    public void onBufferingUpdate(MediaPlayer arg0, int percent) {
        Log.d(TAG, "onBufferingUpdate percent:" + percent);
        intentsbufer++;
        if(intentsbufer >10 && percent==0 )
        {intentsbufer=0;
         Broadcastmsg("Out memory try to clean..");
        
         playVideo();
        }
        if(bufering)
        {Broadcastmsg("Buffering");
         bufering = false;
        }
    }

    public void onCompletion(MediaPlayer arg0) {
        Log.d(TAG, "onCompletion called");
        intents++;
        if(intents<2)
        {      Broadcastmsg("singal lost try to reach");
               playVideo();}
       if(intents>=2) {    Broadcastmsg("signal lost try Later...");
               doCleanUp();
               releaseMediaPlayer();
    
        }
        
    }
    

    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        Log.v(TAG, "onVideoSizeChanged called");
        if (width == 0 || height == 0) {
            Log.e(TAG, "invalid video width(" + width + ") or height(" + height + ")");
            return;
        }
        mIsVideoSizeKnown = true;
        mVideoWidth = width;
        mVideoHeight = height;
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            startVideoPlayback();
        }
    }

    public void onPrepared(MediaPlayer mediaplayer) {
        Log.d(TAG, "onPrepared called");
        mIsVideoReadyToBePlayed = true;
       if (mIsVideoReadyToBePlayed ) { 
      
            startVideoPlayback();
       }
    } /*   && mIsVideoSizeKnown */

    public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
        Log.d(TAG, "surfaceChanged called");

    }

    public void surfaceDestroyed(SurfaceHolder surfaceholder) {
        Log.d(TAG, "surfaceDestroyed called");
    }


    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated called");
        playVideo( );


    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaPlayer();
        doCleanUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
        doCleanUp();
        
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void doCleanUp() {
        mVideoWidth = 0;
        mVideoHeight = 0;
        mIsVideoReadyToBePlayed = false;
        mIsVideoSizeKnown = false;
    }

    private void startVideoPlayback() {
        Log.v(TAG, "startVideoPlayback");
        Broadcastmsg("El pastor transimtiendo" );
        holder.setFixedSize(mVideoWidth, mVideoHeight);
        mMediaPlayer.start();
        bufering = true;
        intents = 0;
        intentsbufer=0;
    }
    
    
}
