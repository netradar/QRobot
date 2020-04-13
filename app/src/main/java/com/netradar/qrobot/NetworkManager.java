package com.netradar.qrobot;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.util.Log;

public class NetworkManager {
	
	boolean isConnected = false;

	Socket socket;
	
	boolean recvFlag,sendFlag;
	
	MainActivity act;
	
	InputStream inputStream;
	
	OutputStream outputStream;
	
	Thread	recvThread,sendThread;
	
	byte[] setModeCommand = {0x01,0x01};
	
	List<byte[]>	sendBuffer;
	
	public class SendRunnable implements Runnable
	{

		@Override
		public void run() 
		{
			while(sendFlag)
			{
				
				byte[] info=null;
				synchronized (sendBuffer)
				{
					
					if(sendBuffer.isEmpty()||sendBuffer.size()==0)
					try 
					{
						sendBuffer.wait();
						continue;
					} catch (InterruptedException e) {
						
					}
					else
					{
						info=sendBuffer.get(0);
						sendBuffer.remove(0);
						sendBuffer.notify();
					}
				}
				if(info!=null)
					sendData(info);
				
				
			}

			Log.d("lichao","sendMsg thread exit");
		}
			
		
		
	}
	
	public class RecvRunnable implements Runnable
	{

		@Override
		public void run() 
		{
			
			
			
				byte[] buffer = new byte[20]; 
				int length = 0;
				boolean isClose=false;
				
				try {
					isClose = socket.isClosed();
					
					while(recvFlag&&!isClose&&!socket.isInputShutdown()  
					        && ((length = inputStream.read(buffer)) != -1))
					{
						
						
						if (length > 0)
						{  
					       // act.onReceive(buffer); 
					        Intent i=new Intent("car.bd.message");
					        i.putExtra("isClose", false);
							i.putExtra("buffer", buffer);
							act.sendBroadcast(i);
					    }
						else
						{  
					        
					    }  
						
						
					}
				} catch (IOException e) {
					
					isConnected = false;
					e.printStackTrace();
				}
			
		
				if(recvFlag)
				{
					closeAll();
					
				}
		}
			
		
		
	}
	public NetworkManager(MainActivity activity)
	{
		act = activity;
		isConnected = false;
		
		sendBuffer=new ArrayList<byte[]>();
		
	}
	public int connect()
	{
		try {
			socket = new Socket("192.168.4.1", 12345);
			socket.setSoTimeout(0);
			
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
			
			recvThread = new Thread(new RecvRunnable());
			sendThread = new Thread(new SendRunnable());
			
			recvFlag = true;
			sendFlag = true;
			
			recvThread.start();
			sendThread.start();
			
			putMsg(setModeCommand);
			//switchLed(0,0);
			
			
		} catch (UnknownHostException e)
		{
			return -1;
			
		} catch (IOException e) {
			return -1;
			
		}
		
		
		return 0;
	}
	
	public void switchLed(int led,int on)
	{
		Log.d("lichao","before switchLed");
	//	if(!isConnected)
	//		return;
		byte[] command = {(byte) led,(byte) on};

		putMsg(command);
		Log.d("lichao","after switchLed");
	}
	public void motorRun(int dir,int speed)
	{
		if(!isConnected)
			return;
		byte[] command = {0x02,(byte) dir,(byte) speed};

		putMsg(command);
	}
	public void motorStop()
	{
		if(!isConnected)
			return;
		byte[] command = {0x03};

		putMsg(command);
	}
	public void motorTurn(int side,int speed)
	{
		if(!isConnected)
			return;
		byte[] command = {0x09,(byte)side,(byte)speed};

		putMsg(command);
	}
	public void setMode(int mode,int onOff)
	{
		if(!isConnected)
			return;
		byte[] command = {0x0a,(byte)mode,(byte)onOff};

		putMsg(command);
	}
	public void playMusic(int isPlay)
	{
		if(!isConnected)
			return;
		byte[] command = {0x08,(byte)isPlay};

		putMsg(command);
	}
	public void getDistince()
	{
		if(!isConnected)
			return;
		byte[] command = {0x0c};

		putMsg(command);
	}
	public void closeConnect()
	{
		if(!isConnected)
			return;
		byte[] command = {0x0b};

		putMsg(command);
	}
	
	public void putMsg(byte[] data)
	{
		synchronized (sendBuffer)
		{
			sendBuffer.add(data);
			sendBuffer.notify();
		}
	}
	public int sendData(byte[] data)
	{
		if(!socket.isClosed()&&socket.isConnected())
		{
			try {
				
				outputStream.write(data);
				Log.d("lichao","send data");
			//	outputStream.flush();
			} catch (UnsupportedEncodingException e) {
				return -1;
			} catch (IOException e) {
				return -1;
			}
		}
		return 0;
	}
	public void closeAll()
	{
		recvFlag = false;
		sendFlag = false;
		
		isConnected = false;
		
		synchronized(sendBuffer)
		{
			sendBuffer.notify();
		}
		recvThread.interrupt();
		if(socket.isConnected())
		{
			
			try {
				socket.close();
			} catch (IOException e) {
				
			}
		}
		
		 Intent i=new Intent("car.bd.message");
	     i.putExtra("isClose", true);
		
		 act.sendBroadcast(i);
	}

}
