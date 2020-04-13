package com.netradar.qrobot;



import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnCancelListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends Activity implements OnClickListener, SensorEventListener, OnCancelListener, OnCheckedChangeListener {
	
	
	private FragmentManager fragmentManager;
	
	private Mode1Fragment mode1Fragment;

	

	
	private SensorManager sensorMgr;  
	Sensor sensor;
	
	private Button	settingBtn;
	
	boolean isConnected = false;
	
	NetworkManager network;
	
	ProgressDialog	pd;
	
	
	BroadcastReceiver bdReceiver;
	private IntentFilter ifilter;
	
	TextView curModeTextView;
	




    private boolean enableOffline = true; 
	
	
	public class ConnectTask extends AsyncTask<Void, Void, Integer> {

		
		@Override
		protected void onPostExecute(Integer result) {

			if(result<0)
			{
				pd.dismiss();
				onConnectError();
			}
			
		}

		@Override
		protected Integer doInBackground(Void... params) {
			
			return network.connect();
		}

	}

	
	View touchBackground;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        fragmentManager = getFragmentManager();
        
        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);  
        
        sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); 
        
        sensorMgr.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL );
        
        
        
        settingBtn = (Button)findViewById(R.id.setting_btn);
        
        curModeTextView = (TextView)findViewById(R.id.curmode_text);
        
        registerForContextMenu(settingBtn);
        
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        
        if (mode1Fragment == null) {
			
			mode1Fragment = new Mode1Fragment();
			transaction.add(R.id.content, mode1Fragment);
		} else {
			
			transaction.show(mode1Fragment);
		}
       

        
        transaction.commit();
        
        
        bdReceiver=new BroadcastReceiver() 
		{  
	  		  
    	    @Override  
    	    public void onReceive(Context context, Intent intent) 
    	    {  
 
    	    	String bd_type=intent.getAction();
    	    	
    	    	if(bd_type.equals("car.bd.message"))
    	    	{
    	    		if(intent.getBooleanExtra("isClose", false))
    	    		{
    	    			serverClose();
    	    		}
    	    		else
    	    		{
	    	    		byte[] buf = intent.getByteArrayExtra("buffer");
	    	    		processReceive(buf);
    	    		}
    	    	}
    	   
    	    }
		}; 
		ifilter= new IntentFilter(); 
    	ifilter.addAction("car.bd.message");
    	registerReceiver(bdReceiver,ifilter);
        
        network = new NetworkManager(this);
        
        mode1Fragment.network= network;

       

    }

	
	public void onSetting(View v)
	{
		if(network.isConnected)
		{
			network.closeConnect();
			return;
	
		}
		pd=new ProgressDialog(this);
		pd.setCancelable(true);
		pd.setMessage(this.getString(R.string.connecting));
		pd.setCanceledOnTouchOutside(false);
		pd.setOnCancelListener(this);
		pd.show();
		
		new ConnectTask().execute();
		
		//v.showContextMenu();
	}







	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
			case 0://mode change
				break;
			case 1://control mode change
				break;
			case 2://disconnect
				break;
		}
		return super.onContextItemSelected(item);
	}
	

	public void onDirPressed(View v)
	{
		//network.closeAll();
		
		
		//mode1Fragment.onDirPressed(v);
	}


	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onSensorChanged(SensorEvent event)
	{
		mode1Fragment.onSensorChanged(event);
		
	}
	
	public void onModeBtn(View v)
	{
		mode1Fragment.onModeBtn(v);
	}









	public void onConnectError()
	{
		Intent i = new Intent();
		i.setClass(this, Dialog.class);
		i.putExtra("text", R.string.connectFailHint);
		i.putExtra("ok", R.string.OK);
		i.putExtra("cancel",R.string.Cancel);
		this.startActivityForResult(i,1);
		
		
	}








	
	public void processReceive(byte[] buffer)
	{
		
		switch(buffer[0])
		{
	
		case 0x01:
			if(buffer[2]==0x01)
			{
				if(pd.isShowing())
					pd.dismiss();
				network.isConnected = true;
				settingBtn.setText(R.string.disconnect);
				curModeTextView.setText(R.string.connected);
				mode1Fragment.startGetDistinceTimer();
				//network.switchLed(0x04,0x00);
				Toast.makeText(this, R.string.connectOK, Toast.LENGTH_SHORT).show();
			}
			else
			{
				if(pd.isShowing())
					pd.dismiss();
				Toast.makeText(this, R.string.connectFail, Toast.LENGTH_SHORT).show();
				
			}
			break;
		case 0x0b:
			
			network.closeAll();
			curModeTextView.setText(R.string.disconnect);
			settingBtn.setText(R.string.connect);
			mode1Fragment.resetAll();
			
			break;
		case 0x0c:
			mode1Fragment.sonarDistince.setText(String.format("%d", buffer[1]));
			break;
		}
	}














	@Override
	public void onCancel(DialogInterface dialog) {
		
		//Toast.makeText(this, "连接已取消", Toast.LENGTH_SHORT).show();
		
	}









	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}









	public void serverClose() 
	{
		
		curModeTextView.setText(R.string.disconnect);
		settingBtn.setText(R.string.connect);
		mode1Fragment.resetAll();
	}









	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	{
		
	}
	
	public void onLedClick(View v)
	{
		mode1Fragment.onLedClick(v);
	}
	public void onPlayMusicBtn(View v)
	{
		mode1Fragment.onPlayMusicBtn();
	}

	public void onStart(View v)
	{
		mode1Fragment.onLanuch();
	}





	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(bdReceiver);
	}




	
}
