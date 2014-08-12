package com.player.android;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class MusicService extends Service{
	private static final int DOWN_BTN_SHOW_MUSIC_LIST=0X12340;
	private static final int DOWN_BTN_PLAY=-1;
	private static final int DOWN_BTN_END=0X12342;
	private static final int DOWN_BTN_STOP=0X12343;
	private static final int DOWN_BTN_UP=0X12344;
	private static final int DOWN_BTN_NEXT=0X12345;
	private static final int DOWN_LIST=0X12346;
	private static final int UPDATE_START=0X12347;
	private static final int UPDATE_BTN=0X12348;
	private static final int UPDATE_BTN_LIST=0X12349;
	private static final int UPDATE_TIMER=0X12350;
	private static final int UPDATE_BAR=0X12351;
	private int playNum;
	ServiceReceiver serviceReceiver;
	//定义一个list列表，用来保存从SD卡读取的歌曲列表
	private List<String> myMusicList=new ArrayList<String>();
	//声明一个String数组的对象，用来保存从SD卡读取的歌曲列表
	private String[] strMusicName=null;
	//定义一个String类型的变量，用来指定SD卡路径
	private final String MUSIC_PATH=new String("/sdcard/");
	private MediaPlayer mediaPlayer=null;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	

	/* (non-Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
		serviceReceiver=new ServiceReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction(MusicPlayer.SERVICE);
		registerReceiver(serviceReceiver, filter);
		mediaPlayer=new MediaPlayer();
		
		File home=new File(MUSIC_PATH);
		//搜索MUSIC_PUSH下路径的歌曲并存放到list列表里面
        if(home.listFiles(new SelectMusicFromSDCard()).length>0){
            for(File file:home.listFiles(new SelectMusicFromSDCard())){
               // System.out.println(file.getPath()+"!!!"+file.getName());
            	myMusicList.add(file.getName());
            	
            }
        }
        strMusicName=new String[myMusicList.size()];
        try {
        	mediaPlayer.reset();
			mediaPlayer.setDataSource(MUSIC_PATH+myMusicList.get(playNum));
			mediaPlayer.prepare();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				if(++playNum>=myMusicList.size()){
					playNum=0;
				}
				
				PlayMusic(MUSIC_PATH+myMusicList.get(playNum));
				intentSend(playNum);
			}
		});
		
		final Handler myHandler=new Handler(){

			/* (non-Javadoc)
			 * @see android.os.Handler#handleMessage(android.os.Message)
			 */
			
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(msg.what==0x12333){
					Intent intent=new Intent(MusicPlayer.MUSIC);
					intent.putExtra("start", UPDATE_TIMER);
					intent.putExtra("playtime",ShowTime(mediaPlayer.getCurrentPosition()));
					intent.putExtra("intplaytime", mediaPlayer.getCurrentPosition());
			
					sendBroadcast(intent);
				}
			}
			
		};
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg=new Message();
				msg.what=0x12333;
				myHandler.sendMessage(msg);
			}
		}, 0, 1000);
		
	}

	public class ServiceReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			int control=intent.getIntExtra("control", -1);
			switch(control){
			case DOWN_BTN_SHOW_MUSIC_LIST:
				Intent sendIntent=new Intent(MusicPlayer.MUSIC);
				//sendIntent.putExtra("musicname", myMusicList.get(playNum));
				//sendIntent.putExtra("playNum", playNum);
				for(int i=0;i<myMusicList.size();i++){
					strMusicName[i]=myMusicList.get(i).toString();
				}
				sendIntent.putExtra("size", myMusicList.size());
				sendIntent.putExtra("strMusicName", strMusicName);
				sendIntent.putExtra("start", UPDATE_BTN_LIST);
				sendBroadcast(sendIntent);
				break;
			case DOWN_BTN_PLAY:
				playNum=intent.getIntExtra("num", -1);
				//PlayMusic(MUSIC_PATH+myMusicList.get(playNum));
				intentSend(playNum);
				mediaPlayer.start();
				break;
			case DOWN_BTN_END:
				if(mediaPlayer.isPlaying()){
					mediaPlayer.stop();
					try {
						mediaPlayer.reset();
						mediaPlayer.setDataSource(MUSIC_PATH+myMusicList.get(playNum));
						mediaPlayer.prepare();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				intentSend(playNum);
				break;
			case DOWN_BTN_STOP:
				if(mediaPlayer.isPlaying()){
					mediaPlayer.pause();
				}
				intentSend(playNum);
				break;
			case DOWN_BTN_UP:
				playNum=intent.getIntExtra("num", -1);
				if(--playNum<0){
					playNum=myMusicList.size()-1;
				}
				PlayMusic(MUSIC_PATH+myMusicList.get(playNum));
				intentSend(playNum);
				break;
			case DOWN_BTN_NEXT:
				playNum=intent.getIntExtra("num", -1);
				if(++playNum>=myMusicList.size()){
					playNum=0;
				}
				PlayMusic(MUSIC_PATH+myMusicList.get(playNum));
				intentSend(playNum);
				break;
			case DOWN_LIST:
				playNum=intent.getIntExtra("num", -1);
				PlayMusic(MUSIC_PATH+myMusicList.get(playNum));
				intentSend(playNum);
				break;
			case UPDATE_BAR:				
				mediaPlayer.seekTo(intent.getIntExtra("seekbar", -1));
				break;
			}
			
			
			
		}
		
		
	}
	public void intentSend(int num){
		Intent sendIntent=new Intent(MusicPlayer.MUSIC);
		sendIntent.putExtra("musicname", myMusicList.get(num));
		sendIntent.putExtra("playNum", num);
		sendIntent.putExtra("alltime", ShowTime(mediaPlayer.getDuration()));
		sendIntent.putExtra("intalltime", mediaPlayer.getDuration());
		sendIntent.putExtra("start", UPDATE_BTN);
		sendBroadcast(sendIntent);
	}
	public void PlayMusic(String path){
    	try {   		
			mediaPlayer.reset();
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare();			
    		mediaPlayer.start();
    	   			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	/* (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Intent intentStart=new Intent(MusicPlayer.MUSIC);
		intentStart.putExtra("start", UPDATE_START);
		intentStart.putExtra("playtime", ShowTime(mediaPlayer.getCurrentPosition()));
		intentStart.putExtra("alltime", ShowTime(mediaPlayer.getDuration()));
		intentStart.putExtra("playnum", playNum);
		intentStart.putExtra("musicname", myMusicList.get(playNum));
		intentStart.putExtra("intalltime", mediaPlayer.getDuration());
		sendBroadcast(intentStart);
		return super.onStartCommand(intent, flags, startId);
	}
	public String ShowTime(int time){
        time/=1000;
        int minute=time/60;
        int hour=minute/60;
        int second=time%60;
        minute%=60;
        return String.format("%02d:%02d", minute, second);
    }
	
	
}
