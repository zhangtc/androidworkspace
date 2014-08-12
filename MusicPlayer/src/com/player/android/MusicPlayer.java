package com.player.android;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 
 * @author ���ֲ�����
 * 
 *
 */

public class MusicPlayer extends Activity {
    /** Called when the activity is first created. */
	
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
	
	private TextView textViewShowMusicName=null;
	private TextView textViewShowPlayTime=null;
	private TextView textViewShowAllTime=null;
	private Button btnStart=null;
	private Button btnEnd=null;
	private Button btnStop=null;
	private Button btnUp=null;
	private Button btnNext=null;
	private Button btnShowMusicList=null;
	private SeekBar seekBar=null;
	//���������ļ�
	
	public static final String SERVICE=
		"SERVICE_ACTION";
	public static final String MUSIC=
		"MUSIC_ACTION";
	private Builder b;
	
	
	//ָ����ǰ���ŵĸ����±�
	private int num=0;
	//����һ���Զ���ȫ�ֱ���
	MusicPlayerReceive musicPlayerReceive;
	private boolean bl=false;
	private boolean blStart=false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        textViewShowMusicName=(TextView)findViewById(R.id.textviewshowmusicname);
        textViewShowPlayTime=(TextView)findViewById(R.id.textviewstarttime);
        textViewShowAllTime=(TextView)findViewById(R.id.textviewalltime);
        btnStart=(Button)findViewById(R.id.buttonplay);
        btnEnd=(Button)findViewById(R.id.btnend);
        btnStop=(Button)findViewById(R.id.btnstop);
        btnUp=(Button)findViewById(R.id.btnup);
        btnNext=(Button)findViewById(R.id.btnnext);       
        btnShowMusicList=(Button)findViewById(R.id.btnshowmusiclist);
        //�󶨰�ť������
        btnStart.setOnClickListener(new ButtonListener());
        btnEnd.setOnClickListener(new ButtonListener());
        btnStop.setOnClickListener(new ButtonListener());
        btnUp.setOnClickListener(new ButtonListener());
        btnNext.setOnClickListener(new ButtonListener());
        btnShowMusicList.setOnClickListener(new ButtonListener());
        //ʵ����һ��Builder����
        b=new AlertDialog.Builder(this);
        //��ȫ�ֱ�������ʵ����
        musicPlayerReceive=new MusicPlayerReceive();
        //����һ������ע���IntentFilter
        IntentFilter filter=new IntentFilter();
        //ָ��BroadcastReceiver������Action
        filter.addAction(MUSIC);
        //ע��ȫ�ּ�����BroadcastReceiver
        registerReceiver(musicPlayerReceive, filter);
        
        seekBar=(SeekBar)findViewById(R.id.seekbarmusic);
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				if(fromUser){
					//mediaPlayer.seekTo(progress);
					//textViewStartTime.setText(String.
					//		valueOf(ShowTime(progress)));
					Intent intent=new Intent(SERVICE);
					intent.putExtra("seekbar", progress);
					intent.putExtra("control", UPDATE_BAR);
					sendBroadcast(intent);
				}
			}
		});
        ListView listView=new ListView(this);
       
        
      //ʵ�����������
       // strMusicName=new String[myMusicList.size()];
        //textViewShowMusicName.setText(myMusicList.get(num));
        
        //����MusicDervice
        Intent intent=new Intent(MusicPlayer.this,MusicService.class);
        startService(intent);
        //���ñ���ͼƬ
        getWindow().setBackgroundDrawableResource(R.drawable.qw);
        
        
    }
    
    class ButtonListener implements OnClickListener{
    	Intent intent=new Intent(SERVICE);
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.buttonplay:		//��ʼ��ť����
				intent.putExtra("control", DOWN_BTN_PLAY);
				intent.putExtra("num", num);
				break;
			case R.id.btnend:			//ֹͣ��ť����
				intent.putExtra("control", DOWN_BTN_END);
				System.out.println("����˽�����ť");
				break;	
			case R.id.btnstop:			//��ͣ��ť����
				intent.putExtra("control", DOWN_BTN_STOP);
				break;
			case R.id.btnup:			//��һ�װ�ť����
				intent.putExtra("control", DOWN_BTN_UP);
				intent.putExtra("num", num);
				break;
			case R.id.btnnext:			//��һ�װ�ť����
				intent.putExtra("control", DOWN_BTN_NEXT);
				intent.putExtra("num", num);
				break;
			case R.id.btnshowmusiclist://�����б�ť����
				intent.putExtra("control", DOWN_BTN_SHOW_MUSIC_LIST);
			
				bl=true;
				break;
			}
			sendBroadcast(intent);
		}

	
    	
    }
    
    public class MusicPlayerReceive extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			int cuo=intent.getIntExtra("start", -1);
			switch(cuo){
			case UPDATE_BTN_LIST:
				int size=intent.getIntExtra("size", -1);
				String[] strMusicNameList=new String[size];
				strMusicNameList=intent.getStringArrayExtra("strMusicName");
				System.out.println(strMusicNameList[0]);
				System.out.println("hello");
				b.setTitle("�����������");
				b.setIcon(R.drawable.adde);
				
				b.setItems(strMusicNameList,new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intentDialog=new Intent(SERVICE);
						intentDialog.putExtra("control", DOWN_LIST);
						
						intentDialog.putExtra("num", which);
						sendBroadcast(intentDialog);
					}
				});
				b.setPositiveButton("ȡ��", null);
				b.create().show();
				bl=false;
				break;
			case UPDATE_BTN:
				String strMusicName=intent.getStringExtra("musicname");

					num=intent.getIntExtra("playNum", -1);
					textViewShowAllTime.setText(intent.getStringExtra("alltime"));				
					textViewShowMusicName.setText(strMusicName);
					seekBar.setMax(intent.getIntExtra("intalltime", -1));
				break;
			
			case UPDATE_START:
				textViewShowPlayTime.setText(intent.getStringExtra("playtime"));
				textViewShowAllTime.setText(intent.getStringExtra("alltime"));
				textViewShowMusicName.setText(intent.getStringExtra("musicname"));
				seekBar.setMax(intent.getIntExtra("intalltime", -1));
				num=intent.getIntExtra("playnum", -1);
				System.out.println(num);
				break;
			case UPDATE_TIMER:
	
				textViewShowPlayTime.setText(intent.getStringExtra("playtime"));
				seekBar.setProgress(intent.getIntExtra("intplaytime", -1));
				
			}
			
		}
    	
    }
    
}