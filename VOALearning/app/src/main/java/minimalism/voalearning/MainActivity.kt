package minimalism.voalearning

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

class MainActivity : AppCompatActivity(), NewsListFragment.OnNewsListener {
    lateinit var mService: AudioService
    lateinit var mBinding: ActivityMainBinding
    var mIsPlaying = false

    val mServiceConnection = object: ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i("thach", "mServiceConnection onServiceConnected")
            val binder = service as AudioService.AudioBinder
            mService = binder.getService()

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
        mBinding.btnPlay.setBackgroundResource(R.drawable.ic_pause)

        mService.startAudioFromURL(news.mAudioUrl)
    }

    private fun resumeNews() {
        mBinding.btnPlay.setBackgroundResource(R.drawable.ic_pause)
        mIsPlaying = true

        mService.resumeAudio()
    }

    private fun pauseNews() {
        mBinding.btnPlay.setBackgroundResource(R.drawable.ic_play)
        mIsPlaying = false

        mService.pauseAudio()
    }
}
