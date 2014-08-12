package android.apps;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.apps.service.PlayerService;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

/**
 * 前段时间刚学习Android时就觉得MediaPlayer是个很神奇的东西，就试着做了个简单的音乐播放器。
 *
 */
public class TestMediaPlayer extends ListActivity {
	/* 前段时间刚学习Android时就觉得MediaPlayer是个很神奇的东西，就试着做了个简单的音乐播放器。
	 * 支持后台播放、进度条拖放、音量控制、读取sdCard音乐文件进行播放等。
	 * */
	private Button playButton = null;
	private Button mFrontButton = null;
	private Button mLastButton = null;
	private Button exit = null;
	/* 声明音量管理器 */
	private AudioManager mAudioManager = null;

	/* 定义进度条 */
	public static SeekBar audioSeekBar = null;

	/* 定义音量大小 */
	private SeekBar audioVolume = null;

	/* 定于一个数据播放列表，用来存放从指定文件中搜索到的文件 */
	public static List<String> mMusicList = new ArrayList<String>();

	/* 定义音乐存放路径 */
	private static final String MUSIC_PATH = new String(Environment.getExternalStorageDirectory().getAbsolutePath());

	/* 定义在播放列表中的当前选择项 */
	public static int currentListItme = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		/* 更新列表 */
		musicList();
		/* 得到控件 */
		playButton = (Button) findViewById(R.id.StartMusic);
		mFrontButton = (Button) findViewById(R.id.FrontButton);
		mLastButton = (Button) findViewById(R.id.LastMusic);
		audioVolume = (SeekBar) findViewById(R.id.audioVolume);
		exit = (Button)findViewById(R.id.exit);
		audioSeekBar = (SeekBar) findViewById(R.id.seekbar01);
		
		System.out.println("111111111111==="+MUSIC_PATH);
		/* 播放、暂停监听 */
		playButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				playMusic(AppConstant.PlayerMag.PAUSE);
			}
		});
		/* 监听下一首 */
		mFrontButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nextMusic();
			}
		});
		/* 监听上一首 */
		mLastButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				FrontMusic();
			}
		});
		/*退出监听*/
		exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(TestMediaPlayer.this, PlayerService.class);
				stopService(intent);//停止Service
				try {
					TestMediaPlayer.this.finish();//关闭当前Activity
				} catch (Throwable e) {
					e.printStackTrace();
				}
			
				
			}
		});
		/* 播放进度监听 */
		audioSeekBar.setOnSeekBarChangeListener(new SeekBarChangeEvent());
		/*退出后再次进去程序时，进度条保持持续更新*/
		if(PlayerService.mMediaPlayer!=null){
			//设置进度条最大值
			TestMediaPlayer.audioSeekBar.setMax(PlayerService.mMediaPlayer.getDuration());
			audioSeekBar.setProgress(PlayerService.mMediaPlayer.getCurrentPosition());
		}
		
		
		/* 得到当前音量对象 */
		mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		/* 把当前音量值赋给进度条 */
		audioVolume.setProgress(mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC));
		/* 监听音量 */
		audioVolume.setOnSeekBarChangeListener(new AudioVolumeChangeEvent());
	}

	public void playMusic(int action) {
		Intent intent = new Intent();
		intent.putExtra("MSG", action);
		intent.setClass(TestMediaPlayer.this, PlayerService.class);
		
		/* 启动service service要在AndroidManifest.xml注册如：<service></service>*/
		
		startService(intent);
	}

	/* 检测sdcard MP3文件并加入到List列表 */
	public void musicList() {
		// 先清除list中的缓存
		mMusicList.clear();
		/* 从指定的路径中读取文件，并与播放列表关联 */
		File home = new File(MUSIC_PATH);
		/* 读取指定类型的文件，在本程序，指定播放类型为mp3 */
		if (home.listFiles(new MusicFilter()).length > 0) {
			/* 读取文件 */
			for (File file : home.listFiles(new MusicFilter())) {
				mMusicList.add(file.getName());
			}
			/* 播放文件与播放列表关联 */
			ArrayAdapter<String> musicList = new ArrayAdapter<String>(
					TestMediaPlayer.this, R.layout.musictime, mMusicList);
			setListAdapter(musicList);
		}
	}

	/* 音量监听 */
	class AudioVolumeChangeEvent implements SeekBar.OnSeekBarChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {

			// mAudioManager.adjustVolume(AudioManager.ADJUST_LOWER, 0);
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress,
					0);

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}

	}

	/* 音乐选择监听 */
	@Override
	protected void onListItemClick(android.widget.ListView l, View v,
			int position, long id) {
		super.onListItemClick(l, v, position, id);
		currentListItme = position;
		playMusic(AppConstant.PlayerMag.PLAY_MAG);
	}

	/* 播放下一首 */
	private void nextMusic() {
		if (++currentListItme >= mMusicList.size()) {
			Toast.makeText(TestMediaPlayer.this, "已到最后一首歌曲", Toast.LENGTH_SHORT)
					.show();
			currentListItme = mMusicList.size() - 1;
		} else {
			playMusic(AppConstant.PlayerMag.PLAY_MAG);
		}
	}

	/* 播放上一首歌曲 */
	private void FrontMusic() {
		if (--currentListItme >= 0) {
			playMusic(AppConstant.PlayerMag.PLAY_MAG);

		} else {
			Toast.makeText(TestMediaPlayer.this, "已到第一首歌曲", Toast.LENGTH_SHORT)
					.show();
			currentListItme = 0;
		}
	}
}

/* 播放文件选择类 */
class MusicFilter implements FilenameFilter {
	public boolean accept(File dir, String name) {
		/* 指定扩展名类型 ，可以加其他的音乐格式*/
		return (name.endsWith(".mp3"));
	}
}

/* 拖放进度监听 ，别忘了Service里面还有个进度条刷新*/
class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		/*假设改变源于用户拖动*/
		if (fromUser) {
			PlayerService.mMediaPlayer.seekTo(progress);// 当进度条的值改变时，音乐播放器从新的位置开始播放
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		PlayerService.mMediaPlayer.pause(); // 开始拖动进度条时，音乐暂停播放
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		PlayerService.mMediaPlayer.start(); // 停止拖动进度条时，音乐开始播放
	}
}