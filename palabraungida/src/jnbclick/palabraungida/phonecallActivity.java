package jnbclick.palabraungida;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
 
public class phonecallActivity extends Activity {
    /** Called when the activity is first created. */
	 private static final String TAG = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        call();
    }
 
private void call() {
    try {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:5628841349"));
        startActivity(callIntent);
    } catch (ActivityNotFoundException activityException) {
         Log.e(TAG,"helloandroid dialing example");
    }
}
 
}
