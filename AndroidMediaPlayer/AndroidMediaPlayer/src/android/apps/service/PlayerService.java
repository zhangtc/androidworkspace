package android.apps.service;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;
import android.apps.*;

public class PlayerService extends Service implements Runnable,
		MediaPlayer.OnCompletionListener {
	/* ����һ����ý����� */
	public static MediaPlayer mMediaPlayer = null;
	// �Ƿ���ѭ��
	private static boolean isLoop = false;
	// �û�����
	private int MSG;

	/* ����Ҫ���ŵ��ļ���·�� */
	private static final String MUSIC_PATH = new String(Environment.getExternalStorageDirectory().getAbsolutePath());

	@Override
	public IBinder onBind(Intent intent) {
		return null;// ����İ�û���ã���ƪ����������ν�activity��service�󶨵Ĵ���
	}

	@Override
	public void onCreate() {
		super.onCreate();
		if (mMediaPlayer != null) {
			mMediaPlayer.reset();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
		mMediaPlayer = new MediaPlayer();
		/* ���������Ƿ���� */
		mMediaPlayer.setOnCompletionListener(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}

		System.out.println("service onDestroy");
	}
    /*����serviceʱִ�еķ���*/
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		/*�õ���startService�����Ķ���������Ĭ�ϲ��������������Զ���ĳ���*/
		MSG = intent.getIntExtra("MSG", AppConstant.PlayerMag.PLAY_MAG);
		if (MSG == AppConstant.PlayerMag.PLAY_MAG) {
			playMusic();
		}
		if (MSG == AppConstant.PlayerMag.PAUSE) {
			if (mMediaPlayer.isPlaying()) {// ���ڲ���
				mMediaPlayer.pause();// ��ͣ
			} else {// û�в���
				mMediaPlayer.start();
			}
		}

		return super.onStartCommand(intent, flags, startId);
	}

	public void playMusic() {
		try {
			/* ���ö�ý�� */
			mMediaPlayer.reset();
			/* ��ȡmp3�ļ� */
			mMediaPlayer.setDataSource(MUSIC_PATH
					+ TestMediaPlayer.mMusicList
							.get(TestMediaPlayer.currentListItme));
			/* ׼������ */
			mMediaPlayer.prepare();
			/* ��ʼ���� */
			mMediaPlayer.start();
			/* �Ƿ���ѭ�� */
			mMediaPlayer.setLooping(isLoop);
			// ���ý��������ֵ
			TestMediaPlayer.audioSeekBar.setMax(PlayerService.mMediaPlayer
					.getDuration());
			new Thread(this).start();
		} catch (IOException e) {
		}

	}

	// ˢ�½�����
	@Override
	public void run() {
		int CurrentPosition = 0;// ����Ĭ�Ͻ�������ǰλ��
		int total = mMediaPlayer.getDuration();//
		while (mMediaPlayer != null && CurrentPosition < total) {
			try {
				Thread.sleep(1000);
				if (mMediaPlayer != null) {
					CurrentPosition = mMediaPlayer.getCurrentPosition();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			TestMediaPlayer.audioSeekBar.setProgress(CurrentPosition);
		}

	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		/* �����굱ǰ�������Զ�������һ�� */

		if (++TestMediaPlayer.currentListItme >= TestMediaPlayer.mMusicList
				.size()) {
			Toast.makeText(PlayerService.this, "�ѵ����һ�׸���", Toast.LENGTH_SHORT)
					.show();
			TestMediaPlayer.currentListItme--;
			TestMediaPlayer.audioSeekBar.setMax(0);
		} else {
			playMusic();
		}
	}
}