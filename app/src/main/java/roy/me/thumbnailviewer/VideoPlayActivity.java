package roy.me.thumbnailviewer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;

import com.orhanobut.logger.Logger;

public class VideoPlayActivity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback, MediaPlayer.OnCompletionListener {

    private ImageButton mBtnPlayPause;
    private Uri         mVideoUri;
    private MediaPlayer mMediaPlayer;
    private SurfaceView mSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedio_play);

        mBtnPlayPause = (ImageButton) findViewById(R.id.btn_PlayPause);
        mSurfaceView = (SurfaceView) findViewById(R.id.sv_video);

        mBtnPlayPause.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent != null) {
            mVideoUri = intent.getData();

        }

    }

    private void mediaPlay() {
        mMediaPlayer.start();
        mBtnPlayPause.setImageResource(R.mipmap.ic_media_pause);
    }

    private void mediaPause() {
        mMediaPlayer.pause();
        mBtnPlayPause.setImageResource(R.mipmap.ic_media_play);
    }

    @Override
    protected void onResume() {
        if (mMediaPlayer != null) {
            mediaPlay();
        } else {
            SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
            surfaceHolder.addCallback(this);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        mediaPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        if (mMediaPlayer.isPlaying()) {
            mediaPause();
        } else {
            mediaPlay();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mMediaPlayer = MediaPlayer.create(this, mVideoUri, holder);
        mMediaPlayer.setOnCompletionListener(this);
        mediaPlay();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Logger.d("视频播放完毕");
        mBtnPlayPause.setImageResource(R.mipmap.ic_media_play);
    }
}
