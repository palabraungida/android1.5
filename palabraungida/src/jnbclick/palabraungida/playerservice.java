package jnbclick.palabraungida;

 
 import java.io.IOException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
 
 

public class playerservice extends Service implements OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener, OnVideoSizeChangedListener {
	    private static final String TAG = null;
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
	    private boolean buferenable = true;
	     private int intents = 0;
	    private int intentsbufer =0;

  public  IBinder onBind(Intent arg0)  {
	/** 
   * A constructor is required, and must call the super IntentService(String)
   * constructor with a name for the worker thread.
   */
	  return null;
		// TODO Auto-generated constructor stub
	  
	}


  
  /**
   * The IntentService calls this method from the default worker thread with
   * the intent that started the service. When this method returns, IntentService
   * stops the service, as appropriate.
   */
  @Override
  public void  onCreate() {
      // Normally we would do some work here, like download a file.
      // For our sample, we just sleep for 5 seconds.
	  
	 
	  Broadcastmsg("Looking for transmission ");
	  playVideo();

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
  public void playVideo() {
      doCleanUp();
      releaseMediaPlayer();
      
      try {      /*
                   * TODO: Set path variable to progressive streamable mp4 or
                   * 3gpp format URL. Http protocol should be used.
                   * Mediaplayer can only play "progressive streamable
                   * contents" which basically means: 1. the movie atom has to
                   * precede all the media data atoms. 2. The clip has to be
                   * reasonably interleaved.rtmp://livestream0001.a1network.org/live/palabraungida-video
                   * http://mobile.streamingmediahosting.com/inplservices-live/jnbc/playlist.m3u8
                   *  */
                  //path = "rtsp://canalungidotv.com:1935/tran/live1_240p";
                  path= "http://zenorad.io:10578/listen.pls";
            // Create a new media player and set the listeners
                  Log.d(TAG, "path=playvideo="+ path);
                            
          mMediaPlayer = new MediaPlayer();
          mMediaPlayer.setVolume(100, 100);
          
          
          try {
        	 mMediaPlayer.setDataSource(path);  
          } catch (IllegalArgumentException e) {
              e.printStackTrace();
          } catch (IllegalStateException e) {
              e.printStackTrace();
          } catch (IOException e) {
              e.printStackTrace();
          }
          
          
         
        //  mMediaPlayer.setDisplay(holder);
          //mMediaPlayer.prepareAsync();
          mMediaPlayer.prepare();
          mMediaPlayer.setOnBufferingUpdateListener(this);
          mMediaPlayer.setOnCompletionListener(this);
          mMediaPlayer.setOnPreparedListener(this);
          mMediaPlayer.setOnVideoSizeChangedListener(this);
          mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        
          

      } catch (Exception e) {
          Log.e(TAG, "general system fault: " + e.getMessage(), e);
          
      }
      
  }
  public void onBufferingUpdate(MediaPlayer arg0, int percent ) {
  	String msg =("Buffering ....");
  	intentsbufer++;
  	if(intentsbufer>10 && percent==0)
  	{   intentsbufer=0;
        Broadcastmsg("Out memory try to clean..");
        playVideo();
  	}
  	if(buferenable)
  	 {Broadcastmsg(msg);
  	  buferenable = false;
  	 }
  	 Log.d(TAG, "Buffering percent:" + percent);
      
  }

  public void onCompletion(MediaPlayer arg0) {
      Log.d(TAG, "onCompletion called" + arg0);
  	intents++; 
  	String msg ="signal lost Trying to reaching signal ...";
  	 Broadcastmsg(msg);
  	if(intents<2)
  	   {playVideo();}
  	if(intents>=2)
  	{  	msg ="signal lost try later ...";
 	    Broadcastmsg(msg);
 	    stopSelf();
  		
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
  	 
  	String msg ="Ready to transmit...";
  	Broadcastmsg(msg);
  	  
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
      


  }
 
  @Override
public void onDestroy() {
      super.onDestroy();
      Log.d(TAG, "DESTROY THE SYSTEM");
     String msg ="Terminando transmision..";
     Broadcastmsg(msg);
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
   
  	String msg ="Nuestro Pastor Transmitiendo";
  	  Broadcastmsg(msg);
  	  buferenable = true;
  	  intents = 0;
  	  intentsbufer=0;
      //holder.setFixedSize(mVideoWidth, mVideoHeight);
      mMediaPlayer.start();
  }
  
  
}
