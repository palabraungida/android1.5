package jnbclick.palabraungida;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

 

public class CheckUrlActivity  extends CheckNetGetUrlActivity {
	    
	public CheckUrlActivity()
	{
		super();
		String SetUrl = "http://www.canal.palabraungidatv.com/conexcust/datapps.php?nid=14&prot=http";
        myClickHandler(SetUrl);
	}
	   
}
