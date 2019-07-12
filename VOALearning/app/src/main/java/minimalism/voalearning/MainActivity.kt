package minimalism.voalearning

import android.app.ActionBar
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_main.*
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
        createNotificationChannel()

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

        val param = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        param.setMargins(0, 0, 0, 128)
        mBinding.navContainer.layoutParams = param


        showNotification()
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

        val param = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        param.setMargins(0, 0, 0, 0)
        mBinding.navContainer.layoutParams = param
    }

    fun updateUi() {
        val currentTime = mService.mMediaPlayer?.currentPosition
        val currentTimeStr = String.format("%02d:%02d",
            currentTime?.div(1000)?.div(60),
            currentTime?.div(1000)?.rem(60))

        mBinding.tvCurrentTime.setText(currentTimeStr)

        mHandler.postDelayed(mUpdateUiRunnable, 1000)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "default channel"
            val descriptionText = "voa notification"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel("voa_notification_channel", "voa", importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification() {
        val intent = Intent(this, MainActivity::class.java).apply {
            addCategory(Intent.ACTION_MAIN)
            addCategory(Intent.CATEGORY_LAUNCHER)
            addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        var builder = NotificationCompat.Builder(this, "voa_notification_channel")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentTitle("VOA Audio Player")
            .setContentText("The application is playing audio")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(100, builder.build())
        }
    }
}
