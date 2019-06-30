package minimalism.voalearning

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import minimalism.voalearning.audioplayer.AudioPlayServiceHolder
import minimalism.voalearning.audioplayer.AudioService
import minimalism.voalearning.databinding.ActivityMainBinding
import minimalism.voalearning.newslist.NewsInfo
import minimalism.voalearning.newslist.NewsListFragment
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity(), NewsListFragment.OnNewsListener, AudioService.OnAudioCompletedListener {
    lateinit var mService: AudioService
    lateinit var mBinding: ActivityMainBinding
    var mIsPlaying = false

    val mHandler = Handler()
    var mUpdateUiRunnable = Runnable {
        updateUi()
    }

    val mServiceConnection = object: ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i("thach", "mServiceConnection onServiceConnected")
            val binder = service as AudioService.AudioBinder
            mService = binder.getService()
            mService.mAudioCompletedListener = WeakReference(this@MainActivity)

            AudioPlayServiceHolder.mAudioService = mService
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i("thach", "mServiceConnection onServiceDisconnected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        bindAudioService()

        mBinding.btnPlay.setOnClickListener {
            if (mIsPlaying)
                pauseNews()
            else
                resumeNews()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun bindAudioService() {
        val intent = Intent(this, AudioService::class.java)
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onNewsItemClicked(news: NewsInfo) {
        if (mBinding.panelAudioControl.visibility != View.VISIBLE)
            mBinding.panelAudioControl.visibility = View.VISIBLE

        startNews(news)
    }

    private fun startNews(news: NewsInfo) {
        mBinding.tvCurrentNewsTitle.setText(news.mTitle)
        mBinding.tvDuration.setText(news.mDuration.substring(3))
        mBinding.btnPlay.setBackgroundResource(R.drawable.ic_pause)

        mService.startAudioFromURL(news.mAudioUrl)

        updateUi()
    }

    private fun resumeNews() {
        mBinding.btnPlay.setBackgroundResource(R.drawable.ic_pause)
        mIsPlaying = true

        mService.resumeAudio()

        updateUi()
    }

    private fun pauseNews() {
        mBinding.btnPlay.setBackgroundResource(R.drawable.ic_play)
        mIsPlaying = false

        mService.pauseAudio()

        mHandler.removeCallbacks(mUpdateUiRunnable)
    }

    override fun onAudioCompleted() {
        pauseNews()
        mBinding.panelAudioControl.visibility = View.INVISIBLE
    }

    fun updateUi() {
        val currentTime = mService.mMediaPlayer?.currentPosition
        val currentTimeStr = String.format("%02d:%02d",
            currentTime?.div(1000)?.div(60),
            currentTime?.div(1000)?.rem(60))

        mBinding.tvCurrentTime.setText(currentTimeStr)

        mHandler.postDelayed(mUpdateUiRunnable, 1000)
    }
}
