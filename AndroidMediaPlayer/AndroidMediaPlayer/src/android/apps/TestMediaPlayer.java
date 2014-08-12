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
 * ǰ��ʱ���ѧϰAndroidʱ�;���MediaPlayer�Ǹ�������Ķ��������������˸��򵥵����ֲ�������
 *
 */
public class TestMediaPlayer extends ListActivity {
	/* ǰ��ʱ���ѧϰAndroidʱ�;���MediaPlayer�Ǹ�������Ķ��������������˸��򵥵����ֲ�������
	 * ֧�ֺ�̨���š��������Ϸš��������ơ���ȡsdCard�����ļ����в��ŵȡ�
	 * */
	private Button playButton = null;
	private Button mFrontButton = null;
	private Button mLastButton = null;
	private Button exit = null;
	/* �������������� */
	private AudioManager mAudioManager = null;

	/* ��������� */
	public static SeekBar audioSeekBar = null;

	/* ����������С */
	private SeekBar audioVolume = null;

	/* ����һ�����ݲ����б�������Ŵ�ָ���ļ������������ļ� */
	public static List<String> mMusicList = new ArrayList<String>();

	/* �������ִ��·�� */
	private static final String MUSIC_PATH = new String(Environment.getExternalStorageDirectory().getAbsolutePath());

	/* �����ڲ����б��еĵ�ǰѡ���� */
	public static int currentListItme = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		/* �����б� */
		musicList();
		/* �õ��ؼ� */
		playButton = (Button) findViewById(R.id.StartMusic);
		mFrontButton = (Button) findViewById(R.id.FrontButton);
		mLastButton = (Button) findViewById(R.id.LastMusic);
		audioVolume = (SeekBar) findViewById(R.id.audioVolume);
		exit = (Button)findViewById(R.id.exit);
		audioSeekBar = (SeekBar) findViewById(R.id.seekbar01);
		
		System.out.println("111111111111==="+MUSIC_PATH);
		/* ���š���ͣ���� */
		playButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				playMusic(AppConstant.PlayerMag.PAUSE);
			}
		});
		/* ������һ�� */
		mFrontButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nextMusic();
			}
		});
		/* ������һ�� */
		mLastButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				FrontMusic();
			}
		});
		/*�˳�����*/
		exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(TestMediaPlayer.this, PlayerService.class);
				stopService(intent);//ֹͣService
				try {
					TestMediaPlayer.this.finish();//�رյ�ǰActivity
				} catch (Throwable e) {
					e.printStackTrace();
				}
			
				
			}
		});
		/* ���Ž��ȼ��� */
		audioSeekBar.setOnSeekBarChangeListener(new SeekBarChangeEvent());
		/*�˳����ٴν�ȥ����ʱ�����������ֳ�������*/
		if(PlayerService.mMediaPlayer!=null){
			//���ý��������ֵ
			TestMediaPlayer.audioSeekBar.setMax(PlayerService.mMediaPlayer.getDuration());
			audioSeekBar.setProgress(PlayerService.mMediaPlayer.getCurrentPosition());
		}
		
		
		/* �õ���ǰ�������� */
		mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		/* �ѵ�ǰ����ֵ���������� */
		audioVolume.setProgress(mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC));
		/* �������� */
		audioVolume.setOnSeekBarChangeListener(new AudioVolumeChangeEvent());
	}

	public void playMusic(int action) {
		Intent intent = new Intent();
		intent.putExtra("MSG", action);
		intent.setClass(TestMediaPlayer.this, PlayerService.class);
		
		/* ����service serviceҪ��AndroidManifest.xmlע���磺<service></service>*/
		
		startService(intent);
	}

	/* ���sdcard MP3�ļ������뵽List�б� */
	public void musicList() {
		// �����list�еĻ���
		mMusicList.clear();
		/* ��ָ����·���ж�ȡ�ļ������벥���б���� */
		File home = new File(MUSIC_PATH);
		/* ��ȡָ�����͵��ļ����ڱ�����ָ����������Ϊmp3 */
		if (home.listFiles(new MusicFilter()).length > 0) {
			/* ��ȡ�ļ� */
			for (File file : home.listFiles(new MusicFilter())) {
				mMusicList.add(file.getName());
			}
			/* �����ļ��벥���б���� */
			ArrayAdapter<String> musicList = new ArrayAdapter<String>(
					TestMediaPlayer.this, R.layout.musictime, mMusicList);
			setListAdapter(musicList);
		}
	}

	/* �������� */
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

	/* ����ѡ����� */
	@Override
	protected void onListItemClick(android.widget.ListView l, View v,
			int position, long id) {
		super.onListItemClick(l, v, position, id);
		currentListItme = position;
		playMusic(AppConstant.PlayerMag.PLAY_MAG);
	}

	/* ������һ�� */
	private void nextMusic() {
		if (++currentListItme >= mMusicList.size()) {
			Toast.makeText(TestMediaPlayer.this, "�ѵ����һ�׸���", Toast.LENGTH_SHORT)
					.show();
			currentListItme = mMusicList.size() - 1;
		} else {
			playMusic(AppConstant.PlayerMag.PLAY_MAG);
		}
	}

	/* ������һ�׸��� */
	private void FrontMusic() {
		if (--currentListItme >= 0) {
			playMusic(AppConstant.PlayerMag.PLAY_MAG);

		} else {
			Toast.makeText(TestMediaPlayer.this, "�ѵ���һ�׸���", Toast.LENGTH_SHORT)
					.show();
			currentListItme = 0;
		}
	}
}

/* �����ļ�ѡ���� */
class MusicFilter implements FilenameFilter {
	public boolean accept(File dir, String name) {
		/* ָ����չ������ �����Լ����������ָ�ʽ*/
		return (name.endsWith(".mp3"));
	}
}

/* �ϷŽ��ȼ��� ��������Service���滹�и�������ˢ��*/
class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		/*����ı�Դ���û��϶�*/
		if (fromUser) {
			PlayerService.mMediaPlayer.seekTo(progress);// ����������ֵ�ı�ʱ�����ֲ��������µ�λ�ÿ�ʼ����
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		PlayerService.mMediaPlayer.pause(); // ��ʼ�϶�������ʱ��������ͣ����
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		PlayerService.mMediaPlayer.start(); // ֹͣ�϶�������ʱ�����ֿ�ʼ����
	}
}