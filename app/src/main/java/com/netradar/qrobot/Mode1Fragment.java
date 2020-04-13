package com.netradar.qrobot;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;




public class Mode1Fragment extends Fragment implements OnCheckedChangeListener, OnTouchListener {
	
	enum	modeEnum{manul,gravity,avoid,trace,follow,push,drop};
	
	View mode1Layout;
	
	boolean isZhongli = false;
	
	Button changeBtn;
	Button upBtn,downBtn,leftBtn,rightBtn;
	
	Button manulBtn,gravityBtn,avoidBtn,traceBtn,followBtn,pushBtn,dropBtn;
	
	TextView sonarDistince;
	TextView hintText;
	Button	playMusicBtn;
	
	View	directionLayout;
	int directionLayoutWidth;
	
	modeEnum	currentMode = modeEnum.manul;
	Button		currentBtn;
	Switch	frontLed,rearLed,leftLed,rightLed;
	
	ImageView ballImageView;
	Button	touchBackgroundBtn;
	
	NetworkManager network;
	
	Button	lanuchBtn;
	
 //   EventManager asr;
    

	
	public Mode1Fragment() {
	
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mode1Layout = inflater.inflate(R.layout.mode1_layout,
				container, false);
		
		
	/*	
		asr = EventManagerFactory.create(this.getActivity(), "asr");
		
		EventListener yourListener = new EventListener() {
			  @Override
			  public void onEvent(String name, String params, byte[] data, int offset, int length) {
				 // Log.d("lichao","ready to onEvent");
					if (params != null && !params.isEmpty()) {
			            
			        }
			        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_READY)) 
			        {
			            Log.d("lichao","ready to speak");
			        }

			        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_FINISH)) 
			        {
			            Log.d("lichao","recog finish");
			        }
			  }
			};
        asr.registerListener(yourListener);
        
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put(SpeechConstant.DECODER, 2);
        params.put(SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH, "assets://baidu_speech_grammar.bsg");
        asr.send(SpeechConstant.ASR_KWS_LOAD_ENGINE, new JSONObject(params).toString(), null, 0, 0);
  */
		
//		changeBtn = (Button)mode1Layout.findViewById(R.id.change_mode_btn);
	/*	upBtn = (Button)mode1Layout.findViewById(R.id.up_btn);
		downBtn = (Button)mode1Layout.findViewById(R.id.down_btn);
		leftBtn = (Button)mode1Layout.findViewById(R.id.left_btn);
		rightBtn = (Button)mode1Layout.findViewById(R.id.right_btn);
		*/
		hintText = (TextView)mode1Layout.findViewById(R.id.hint_text);
		playMusicBtn = (Button)mode1Layout.findViewById(R.id.playmusic_btn);
		lanuchBtn = (Button)mode1Layout.findViewById(R.id.start_btn);
		
	/*	
		upBtn.setOnTouchListener(this);
		downBtn.setOnTouchListener(this);
		leftBtn.setOnTouchListener(this);
		rightBtn.setOnTouchListener(this);
	*/	
		manulBtn = (Button)mode1Layout.findViewById(R.id.manul_btn);
		gravityBtn = (Button)mode1Layout.findViewById(R.id.gravity_btn);
		avoidBtn = (Button)mode1Layout.findViewById(R.id.avoid_btn);
		traceBtn = (Button)mode1Layout.findViewById(R.id.trace_btn);
		followBtn = (Button)mode1Layout.findViewById(R.id.follow_btn);
		pushBtn = (Button)mode1Layout.findViewById(R.id.push_btn);
		dropBtn = (Button)mode1Layout.findViewById(R.id.drop_btn);
	
		
		
		frontLed = (Switch)mode1Layout.findViewById(R.id.frontled_switch);
        rearLed = (Switch)mode1Layout.findViewById(R.id.rearled_switch);
        leftLed = (Switch)mode1Layout.findViewById(R.id.leftled_switch);
        rightLed = (Switch)mode1Layout.findViewById(R.id.rightled_switch);
        
        frontLed.setOnCheckedChangeListener(this);
        rearLed.setOnCheckedChangeListener(this);
        leftLed.setOnCheckedChangeListener(this);
        rightLed.setOnCheckedChangeListener(this);
        
        touchBackgroundBtn = (Button)mode1Layout.findViewById(R.id.touchbackground_btn);
        ballImageView = (ImageView)mode1Layout.findViewById(R.id.touchball_imageview);
        
        directionLayout = (View)mode1Layout.findViewById(R.id.touchbackground_layout);
        
        touchBackgroundBtn.setOnTouchListener(this);
        
        directionLayout.post(new Runnable() {
            @Override
            public void run()
            {
            	int w = directionLayout.getWidth();
            	int h = directionLayout.getHeight();
            	int l = w<h?w:h;
            	
            	
            	RelativeLayout.LayoutParams layoutParams=
            		    new RelativeLayout.LayoutParams(l, l);
            			layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            	touchBackgroundBtn.setLayoutParams(layoutParams);
            	
            	layoutParams=
            		    new RelativeLayout.LayoutParams(l/5, l/5);
            			layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            	
            	ballImageView.setLayoutParams(layoutParams);
            	
            }
        });
        
        touchBackgroundBtn.post(new Runnable() {
            @Override
            public void run()
            {
            	RelativeLayout.LayoutParams layoutParams=
            		    new RelativeLayout.LayoutParams(touchBackgroundBtn.getWidth()/5, touchBackgroundBtn.getWidth()/5);
            			layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            	ballImageView.setLayoutParams(layoutParams);
            	  touchBackgroundBtn.setVisibility(View.GONE);
                 ballImageView.setVisibility(View.GONE);
            	
            }
        });
     
      
		
		currentBtn = manulBtn;
		
		

		
		
	//	hintText.setVisibility(View.GONE);
	//	startBtn.setVisibility(View.GONE);
		
		sonarDistince = (TextView)mode1Layout.findViewById(R.id.sonar_text);
		
		Typeface face = Typeface.createFromAsset(this.getActivity().getAssets(),  
                "Digital2.ttf");  
		sonarDistince.setTypeface(face);
	
		sonarDistince.setText("0");
		
		return mode1Layout;
		
	}






	public void onSensorChanged(SensorEvent event) 
	{
		if(currentMode!=modeEnum.gravity||!isLanuching)
			return;
		int x = (int)event.values[0];  
		int y = (int)event.values[1];  
		int z = (int)event.values[2];  
		
		//hintText.setText("x="+x+" y="+y+" z="+z);
		
		if(Math.abs(x)<2&&Math.abs(y)<2)
		{
			if(curRunSpeed!=0||curTurnSpeed!=0)
			{
				curRunSpeed = 0;
				curTurnSpeed = 0;
				network.motorStop();
				Log.d("lichao","motor stop");
			}
			return;
		}
		
		if(Math.abs(x)>Math.abs(y))
		{
			int speed = 50+(Math.abs(x)-2)*40;
			if(speed>250) speed = 250;
			
			if(curRunSpeed==speed)
				return;
			curRunSpeed = speed;
			if(x<0)
			{
				network.motorRun(0x00, speed);
				Log.d("lichao","motor run forward speed is "+speed);
			}
			else
			{
				network.motorRun(0x01, speed);
				Log.d("lichao","motor run backward speed is "+speed);
			}
			
			
		}
		else
		{
			int speed = 50+(Math.abs(y)-2)*40;
			if(speed>250) speed = 250;
			
			if(curTurnSpeed==speed)
				return;
			curTurnSpeed = speed;
			
			
			if(y<0)
			{
				network.motorTurn(0x00, speed);
				Log.d("lichao","motor turn left speed is "+speed);
			}
			else
			{
				network.motorTurn(0x01, speed);
				Log.d("lichao","motor turn right speed is "+speed);
			}
		}
		
		
		
	}

	public void onModeBtn(View v) {
		
	//	currentBtn.setTextColor(Color.WHITE);
		
		//hintText.setWidth(directionLayout.getWidth());
		
		
		switch(v.getId())
		{
		case R.id.manul_btn:
			if(currentMode==modeEnum.manul)
				return;
			if(isLanuching)
			{
				Toast.makeText(this.getActivity(), R.string.switchModeFailHint, Toast.LENGTH_SHORT).show();
				return;
			}
			currentBtn.setTextColor(Color.WHITE);
			manulBtn.setTextColor(Color.RED);
			currentBtn = manulBtn;
			currentMode = modeEnum.manul;
			
			break;
			
		case R.id.gravity_btn:
			if(currentMode==modeEnum.gravity)
				return;
			if(isLanuching)
			{
				Toast.makeText(this.getActivity(), R.string.switchModeFailHint, Toast.LENGTH_SHORT).show();
				return;
			}
			currentBtn.setTextColor(Color.WHITE);
			gravityBtn.setTextColor(Color.RED);
			currentBtn = gravityBtn;
			currentMode = modeEnum.gravity;
			break;
			
		case R.id.avoid_btn:
			if(currentMode==modeEnum.avoid)
				return;
			if(isLanuching)
			{
				Toast.makeText(this.getActivity(), R.string.switchModeFailHint, Toast.LENGTH_SHORT).show();
				return;
			}
			currentBtn.setTextColor(Color.WHITE);
			avoidBtn.setTextColor(Color.RED);
			currentBtn = avoidBtn;
			currentMode = modeEnum.avoid;
			break;
			
		case R.id.trace_btn:
			if(currentMode==modeEnum.trace)
				return;
			if(isLanuching)
			{
				Toast.makeText(this.getActivity(), R.string.switchModeFailHint, Toast.LENGTH_SHORT).show();
				return;
			}
			currentBtn.setTextColor(Color.WHITE);
			traceBtn.setTextColor(Color.RED);
			currentBtn = traceBtn;
			currentMode = modeEnum.trace;
			break;
			
		case R.id.follow_btn:
			if(currentMode==modeEnum.follow)
				return;
			if(isLanuching)
			{
				Toast.makeText(this.getActivity(), R.string.switchModeFailHint, Toast.LENGTH_SHORT).show();
				return;
			}
			currentBtn.setTextColor(Color.WHITE);
			followBtn.setTextColor(Color.RED);
			currentBtn = followBtn;
			currentMode = modeEnum.follow;
			break;
			
		case R.id.push_btn:
			if(currentMode==modeEnum.push)
				return;
			if(isLanuching)
			{
				Toast.makeText(this.getActivity(), R.string.switchModeFailHint, Toast.LENGTH_SHORT).show();
				return;
			}
			currentBtn.setTextColor(Color.WHITE);
			pushBtn.setTextColor(Color.RED);
			currentBtn = pushBtn;
			currentMode = modeEnum.push;
			break;
		case R.id.drop_btn:
			if(currentMode==modeEnum.drop)
				return;
			if(isLanuching)
			{
				Toast.makeText(this.getActivity(), R.string.switchModeFailHint, Toast.LENGTH_SHORT).show();
				return;
			}
			currentBtn.setTextColor(Color.WHITE);
			dropBtn.setTextColor(Color.RED);
			currentBtn = dropBtn;
			currentMode = modeEnum.drop;
			break;
		
		}
		

		setModeTo(currentMode);
	}
	
	boolean isLanuching = false;
	
	public void onLanuch()
	{
		/*
		Map<String, Object> params = new LinkedHashMap<String, Object>();
        String event = null;
        event = SpeechConstant.ASR_START; 

       // if (enableOffline)
        {
            params.put(SpeechConstant.DECODER, 2);
        }
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
       String json = null; 
        json = new JSONObject(params).toString(); 
        asr.send(event, json, null, 0, 0);
        
        */
		
		if(!network.isConnected)
		{
			Toast.makeText(this.getActivity(), R.string.connectHint, Toast.LENGTH_SHORT).show();
			return;
		}
        
		int mode = 0x00,isStart = 0x01;
		
		if(currentMode==modeEnum.manul)
		{
			if(!isLanuching)
			{
				hintText.setVisibility(View.GONE);
				touchBackgroundBtn.setVisibility(View.VISIBLE);
				ballImageView.setVisibility(View.VISIBLE);
				lanuchBtn.setText(R.string.stop);
				lanuchBtn.setBackgroundResource(R.drawable.btn_style_red);
				isLanuching = true;
				
				mode = 0x00;
				isStart = 0x00;
			}
			else
			{
				
				touchBackgroundBtn.setVisibility(View.GONE);
				ballImageView.setVisibility(View.GONE);
				hintText.setVisibility(View.VISIBLE);
				lanuchBtn.setText(R.string.start);
				lanuchBtn.setBackgroundResource(R.drawable.btn_query);
				isLanuching = false;
				
				mode = 0x00;
				isStart = 0x01;
			}
			
			
		}
		
		if(currentMode==modeEnum.gravity)
		{
			if(!isLanuching)
			{
				mode = 0x01;
				isStart = 0x00;
				lanuchBtn.setText(R.string.stop);
				lanuchBtn.setBackgroundResource(R.drawable.btn_style_red);
				isLanuching = true;
			}
			else
			{
				mode = 0x01;
				isStart = 0x01;
				lanuchBtn.setText(R.string.start);
				lanuchBtn.setBackgroundResource(R.drawable.btn_query);
				isLanuching = false;
			}
			
			
		}
		
		if(currentMode==modeEnum.avoid)
		{
			if(!isLanuching)
			{
				mode = 0x02;
				isStart = 0x00;
				lanuchBtn.setText(R.string.stop);
				lanuchBtn.setBackgroundResource(R.drawable.btn_style_red);
				isLanuching = true;
			}
			else
			{
				mode = 0x02;
				isStart = 0x01;
				lanuchBtn.setText(R.string.start);
				lanuchBtn.setBackgroundResource(R.drawable.btn_query);
				isLanuching = false;
			}
			
			
		}
		if(currentMode==modeEnum.trace)
		{
			if(!isLanuching)
			{
				mode = 0x03;
				isStart = 0x00;
				lanuchBtn.setText(R.string.stop);
				lanuchBtn.setBackgroundResource(R.drawable.btn_style_red);
				isLanuching = true;
			}
			else
			{
				mode = 0x03;
				isStart = 0x01;
				lanuchBtn.setText(R.string.start);
				lanuchBtn.setBackgroundResource(R.drawable.btn_query);
				isLanuching = false;
			}
			
			
		}
		if(currentMode==modeEnum.follow)
		{
			if(!isLanuching)
			{
				mode = 0x04;
				isStart = 0x00;
				lanuchBtn.setText(R.string.stop);
				lanuchBtn.setBackgroundResource(R.drawable.btn_style_red);
				isLanuching = true;
			}
			else
			{
				mode = 0x04;
				isStart = 0x01;
				lanuchBtn.setText(R.string.start);
				lanuchBtn.setBackgroundResource(R.drawable.btn_query);
				isLanuching = false;
			}
		
			
		}
		if(currentMode==modeEnum.push)
		{
			if(!isLanuching)
			{
				mode = 0x05;
				isStart = 0x00;
				lanuchBtn.setText(R.string.stop);
				lanuchBtn.setBackgroundResource(R.drawable.btn_style_red);
				isLanuching = true;
			}
			else
			{
				mode = 0x05;
				isStart = 0x01;
				lanuchBtn.setText(R.string.start);
				lanuchBtn.setBackgroundResource(R.drawable.btn_query);
				isLanuching = false;
			}
			
			
		}
		if(currentMode==modeEnum.drop)
		{
			if(!isLanuching)
			{
				mode = 0x06;
				isStart = 0x00;
				lanuchBtn.setText(R.string.stop);
				lanuchBtn.setBackgroundResource(R.drawable.btn_style_red);
				isLanuching = true;
			}
			else
			{
				mode = 0x06;
				isStart = 0x01;
				lanuchBtn.setText(R.string.start);
				lanuchBtn.setBackgroundResource(R.drawable.btn_query);
				isLanuching = false;
			}
			
			
		}
		
		
		
		network.setMode(mode, isStart);
		
		
		
	}

	/*
	private void startVoice()
	{
		Log.d("lichao","startVoice");
		 Map<String, Object> params = new LinkedHashMap<String, Object>();
		    String event = null;
			String json = null; 
	        event = SpeechConstant.ASR_START; // 替换成测试的event

	    //    if (enableOffline)
	        {
	            params.put(SpeechConstant.DECODER, 2);
	        }
	        params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT,500);
	        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);

	        params.put(SpeechConstant.DISABLE_PUNCTUATION,false);


	        json = new JSONObject(params).toString(); 
            asr.send(event, json, null, 0, 0);
		
	}
*/

	private void setModeTo(modeEnum cm) {

		if(cm==modeEnum.manul)
		{
			
			hintText.setText(R.string.hinttouch);
			
			//startBtn.setVisibility(View.GONE);
		}
		
		if(cm==modeEnum.gravity)
		{
			
			hintText.setVisibility(View.VISIBLE);
			hintText.setText(R.string.hintgravity);
			//startBtn.setVisibility(View.VISIBLE);
		}
		
		if(cm==modeEnum.avoid)
		{
			
			hintText.setVisibility(View.VISIBLE);
			hintText.setText(R.string.hintavoid);
			//startBtn.setVisibility(View.VISIBLE);
		}
		if(cm==modeEnum.trace)
		{
			
			hintText.setVisibility(View.VISIBLE);
			hintText.setText(R.string.hinttrace);
			//startBtn.setVisibility(View.VISIBLE);
		}
		if(cm==modeEnum.follow)
		{
			
			hintText.setVisibility(View.VISIBLE);
			hintText.setText(R.string.hintfollow);
			//startBtn.setVisibility(View.VISIBLE);
		}
		if(cm==modeEnum.push)
		{
			
			hintText.setVisibility(View.VISIBLE);
			hintText.setText(R.string.hintpush);
			//startBtn.setVisibility(View.VISIBLE);
		}
		if(cm==modeEnum.drop)
		{
			
			hintText.setVisibility(View.VISIBLE);
			hintText.setText(R.string.hintdrop);
			//startBtn.setVisibility(View.VISIBLE);
		}
		
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		
		if(!network.isConnected)
		{
			Toast.makeText(this.getActivity(), R.string.connectHint, Toast.LENGTH_SHORT).show();
			return;
		}
		switch (buttonView.getId())
		{  
	        case R.id.frontled_switch:  
	            if(isChecked){  
	            	network.switchLed(0x04,0x00);
	                
	            }else {  
	            	network.switchLed(0x04,0x01);
	               
	            }  
	            break;  
	        case R.id.rearled_switch:  
	            if(isChecked){  
	            	network.switchLed(0x05,0x00);
	              
	            }else {  
	            	network.switchLed(0x05,0x01);
	            }  
	            break;  
	        case R.id.leftled_switch:  
	            if(isChecked){  
	            	network.switchLed(0x06,0x00);
	            }else {  
	            network.switchLed(0x06,0x01);
	            }  
	            break; 
	        case R.id.rightled_switch:  
	            if(isChecked){  
	            	network.switchLed(0x07,0x00);
	            }else {  
	            	network.switchLed(0x07,0x01);
	            }  
	            break; 
	        default:  
	            break;  
    }  
		
	}

	
/*
	private void processOnDirPressed(int id) {
		
		int speed = 150;
		switch(id)
		{
		case R.id.up_btn:
			network.motorRun(0x00, speed);
			break;
		case R.id.down_btn:
			network.motorRun(0x01, speed);
			break;
		case R.id.left_btn:
			network.motorTurn(0x00);
			break;
		case R.id.right_btn:
			network.motorTurn(0x01);
			break;
		}
	}
*/
	public void onLedClick(View v)
	{
		if(!network.isConnected)
		{
			Toast.makeText(this.getActivity(), R.string.connectHint, Toast.LENGTH_SHORT).show();
			return;
		}
		switch (v.getId())
		{  
	        case R.id.frontled_layout:  
	        	frontLed.setChecked(!frontLed.isChecked());
	            if(frontLed.isChecked()){  
	            	network.switchLed(0x04,0x00);
	                
	            }else {  
	            	network.switchLed(0x04,0x01);
	               
	            }  
	            break;  
	        case R.id.rearled_layout:  
	        	rearLed.setChecked(!rearLed.isChecked());
	            if(rearLed.isChecked()){  
	            	network.switchLed(0x05,0x00);
	                
	            }else {  
	            	network.switchLed(0x05,0x01);
	               
	            }  
	            break;  
	        case R.id.leftled_layout:  
	        	leftLed.setChecked(!leftLed.isChecked());
	            if(leftLed.isChecked()){  
	            	network.switchLed(0x06,0x00);
	                
	            }else {  
	            	network.switchLed(0x06,0x01);
	               
	            }  
	            break; 
	        case R.id.rightled_layout:  
	        	rightLed.setChecked(!rightLed.isChecked());
	            if(rightLed.isChecked()){  
	            	network.switchLed(0x07,0x00);
	                
	            }else {  
	            	network.switchLed(0x07,0x01);
	               
	            }   
	            break; 
	        default:  
	            break;  
    }  
		
	}
	
	boolean isTouched = false;

	int lastX=0,lastY=0;
	int curRunSpeed = 0,curTurnSpeed = 0;
	
	@Override
	public boolean onTouch(View v, MotionEvent event) 
	{

		boolean isMoveValid;
		int eventaction = event.getAction();
	
		int curTouchX = (int) event.getRawX();
		 
		int curTouchY = (int) event.getRawY();
		
		//Log.d("lichao","action is "+eventaction);
		
		int[] location = new int[2];
        touchBackgroundBtn.getLocationOnScreen(location);
        
        int r = (touchBackgroundBtn.getRight()-touchBackgroundBtn.getLeft())/2;
        
        int[] locationBall = new int[2];
        ballImageView.getLocationOnScreen(locationBall);
        int br = (ballImageView.getRight()-ballImageView.getLeft())/2;
        
        int vCenterX = location[0]+r;
        int vCenterY = location[1]+r;
        
        
        
        int distanceX = Math.abs(vCenterX-curTouchX);
       
        int distanceY = Math.abs(vCenterY-curTouchY);
       
        int distanceZ = (int) Math.sqrt(Math.pow(distanceX,2)+Math.pow(distanceY,2));

        

        
        if(distanceZ>((r*4)/5))
        	isMoveValid = false;
        else
        	isMoveValid = true;
        
        
		
		switch (eventaction) 
		{
		 
		case MotionEvent.ACTION_DOWN: 
			if(isMoveValid)
			{
				isTouched = true;
				lastX = curTouchX;
				lastY = curTouchY;
				int dx = curTouchX - locationBall[0]-br;
				int dy = curTouchY - locationBall[1]-br;
				moveBallTo(dx,dy);
				
				ballImageView.getLocationOnScreen(locationBall);
				int ballCenterX = locationBall[0]+br;
		        int ballCenterY = locationBall[1]+br;
				
				sendCmdToCar((ballCenterY-vCenterY),(ballCenterX-vCenterX),(r-br));
				
			}
			else
				return false;
			break;
		case MotionEvent.ACTION_MOVE:
			if(isMoveValid)
			{
				int dx = curTouchX - lastX;
				int dy = curTouchY - lastY;
				lastX = curTouchX;
				lastY = curTouchY;
				moveBallTo(dx,dy);
				
				ballImageView.getLocationOnScreen(locationBall);
				int ballCenterX = locationBall[0]+br;
		        int ballCenterY = locationBall[1]+br;
				sendCmdToCar((ballCenterY-vCenterY),(ballCenterX-vCenterX),(r-br));
			}
			else
			{
				//isTouched = false;
			}
			
			break;
		case MotionEvent.ACTION_UP:
			//if(isMoveValid)
			{
				int dx = vCenterX - locationBall[0]-br;
				int dy = vCenterY - locationBall[1]-br;
				moveBallTo(dx,dy);
				if(curRunSpeed!=0||curTurnSpeed!=0)
				{
					network.motorStop();
				
					curRunSpeed = 0;
					curTurnSpeed = 0;
					
					Log.d("lichao","motor stop");
				}
			}
			

			break;
		}
		return true;
	}
	
	

	private void sendCmdToCar(int distinceX, int distinceY,int maxDistince) 
	{
		
		if(Math.abs(distinceX)>=Math.abs(distinceY))
		{
			int speedLevel = Math.abs(distinceX)/(maxDistince/5);
			int speed = (speedLevel)*50;
			if(speed!=curRunSpeed)
			{
				curRunSpeed = speed;
				
				if(speed==0)
				{
					network.motorStop();
					Log.d("lichao","motor stop");
					return;
					
				}
				if(distinceX<0)
				{
					network.motorRun(0x00,Math.abs(speed));
					Log.d("lichao","motor run forward,speed="+speed);
					
				}
				else
				{
					network.motorRun(0x01,Math.abs(speed));
					Log.d("lichao","motor run backward,speed="+Math.abs(speed));
				}
				
			}
		}
		else
		{
			int speedLevel = Math.abs(distinceY)/(maxDistince/5);
			int speed = (speedLevel)*50;
			if(speed!=curTurnSpeed)
			{
				curTurnSpeed = speed;
				if(speed==0)
				{
					network.motorStop();
					Log.d("lichao","motor stop");
					return;
					
				}
				if(distinceY<0)
				{
					network.motorTurn(0x00 ,Math.abs(speed));
					
					Log.d("lichao","motor turn left,speed="+speed);
				}
				else
				{
					network.motorTurn(0x01,Math.abs(speed));
					Log.d("lichao","motor trun right,speed="+Math.abs(speed));
				}
				
			}
			
		}
		
	}

	private void moveBallTo(int dx, int dy) 
	{
		
		int l = ballImageView.getLeft() + dx;     
		int b = ballImageView.getBottom() + dy;     
		int r = ballImageView.getRight() + dx;     
		int t = ballImageView.getTop() + dy;     
		 
		ballImageView.layout(l, t, r, b);     
  
		ballImageView.postInvalidate();    
	}
/*
	public void processVoiceEvent(String name, String params) 
	{
		Log.d("lichao","params is :"+params);
		Log.d("lichao","name is :"+name);

        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL))
        {
        	JSONObject json;
			try 
			{
				json = new JSONObject(params);
			
				if(json.getString("result_type").equals("final_result"))
				{
					String result = json.getString("best_result");
					Log.d("lichao","reco result is : "+result);
					Toast.makeText(this.getActivity(), result, Toast.LENGTH_SHORT).show();
					
					processVcmd(result);
				}
				
			} catch (JSONException e) {
				Log.d("lichao","reco failure ");
			}
			
            
            
        }
      
		
	}

	String vcmd[] = {"前进","后退","左转","右转","停车","前灯","后灯","左灯","右灯","加速","减速","播放音乐"};
	
	int curVstatus = -1;//0-forward 1-backward 2-turnleft 3-turnright
	int curVspeed = 150;
	private void processVcmd(String result)
	{
		if(result.equals("前进"))
		{
			
			curVstatus = 0;
			network.motorRun(0x00,curVspeed);
		}
		if(result.equals("后退"))
		{
			
			curVstatus = 1;
			network.motorRun(0x01,curVspeed);
		}
		if(result.equals("左转"))
		{
			
			curVstatus = 2;
			network.motorTurn(0x00,curVspeed);
		}
		if(result.equals("右转"))
		{
			
			curVstatus = 3;
			network.motorTurn(0x01,curVspeed);
		}
		if(result.equals("停车"))
		{
			curVstatus = -1;
			network.motorStop();
		}
		if(result.equals("前灯"))
		{
			frontLed.setChecked(!frontLed.isChecked());
            if(frontLed.isChecked()){  
            	network.switchLed(0x04,0x00);
                
            }else {  
            	network.switchLed(0x04,0x01);
               
            }  
	
		}
		if(result.equals("后灯"))
		{
			rearLed.setChecked(!rearLed.isChecked());
            if(rearLed.isChecked()){  
            	network.switchLed(0x05,0x00);
                
            }else {  
            	network.switchLed(0x05,0x01);
               
            }  
	
		}
		if(result.equals("左灯"))
		{
			leftLed.setChecked(!leftLed.isChecked());
            if(leftLed.isChecked()){  
            	network.switchLed(0x06,0x00);
                
            }else {  
            	network.switchLed(0x06,0x01);
               
            }  
	
		}
		if(result.equals("右灯"))
		{
			rightLed.setChecked(!rightLed.isChecked());
            if(rightLed.isChecked()){  
            	network.switchLed(0x07,0x00);
                
            }else {  
            	network.switchLed(0x07,0x01);
               
            }  
	
		}
		if(result.equals("加速"))
		{
			curVspeed+=50;
			if(curVspeed>250) curVspeed = 250;
			
			switch(curVstatus)
			{
			case 0:
				network.motorRun(0x00,curRunSpeed);
				break;
			case 1:
				network.motorRun(0x01,curRunSpeed);
				break;
			case 2:
				network.motorTurn(0x00,curRunSpeed);
				break;
			case 3:
				network.motorTurn(0x01,curRunSpeed);
				break;
				
			}
			
	
		}
		if(result.equals("减速"))
		{
			curVspeed-=50;
			if(curVspeed<50) curVspeed = 50;
			
			switch(curVstatus)
			{
			case 0:
				network.motorRun(0x00,curRunSpeed);
				break;
			case 1:
				network.motorRun(0x01,curRunSpeed);
				break;
			case 2:
				network.motorTurn(0x00,curRunSpeed);
				break;
			case 3:
				network.motorTurn(0x01,curRunSpeed);
				break;
				
			}
		}
		
		
	}
*/
	boolean isPlayMusic = false;
	public void onPlayMusicBtn()
	{
		if(!network.isConnected)
		{
			Toast.makeText(this.getActivity(), R.string.connectHint, Toast.LENGTH_SHORT).show();
			return;
		}
		if(isPlayMusic)
		{
			playMusicBtn.setText(R.string.playmusic);
			playMusicBtn.setBackgroundResource(R.drawable.btn_query);
			isPlayMusic = false;
			network.playMusic(0x01);
		}
		else
		{
			playMusicBtn.setText(R.string.stopplay);
			playMusicBtn.setBackgroundResource(R.drawable.btn_style_red);
			isPlayMusic = true;
			network.playMusic(0x00);
		}
	}

	public void resetAll()
	{
		lanuchBtn.setText(R.string.start);
		lanuchBtn.setBackgroundResource(R.drawable.btn_query);
		isLanuching = false;
		frontLed.setChecked(false);
		rearLed.setChecked(false);
		leftLed.setChecked(false);
		rightLed.setChecked(false);
		
		playMusicBtn.setText(R.string.playmusic);
		playMusicBtn.setBackgroundResource(R.drawable.btn_query);
		isPlayMusic = false;
		
		sonarDistince.setText("0");
		
		stopGetDistinceTimer();
		
	}

	Timer distTimer;
	
	private class getDistinceTask extends TimerTask
	{

		@Override
		public void run()
		{
			
			network.getDistince();
        	
       	}
	}

	public void startGetDistinceTimer()
	{
		stopGetDistinceTimer();
		distTimer = new Timer();
		distTimer.schedule(new getDistinceTask(), 0,1000);

		
	}
	public void stopGetDistinceTimer()
	{
		if(distTimer!=null)
		{
			distTimer.cancel();
			distTimer=null;
		}
	}
	
	
	

}
