package minimalism.voalearning.audioplayer

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.util.Log

class AudioService : Service() {

    var mMediaPlayer: MediaPlayer?       = null

    inner class AudioBinder : Binder() {
        fun getService(): AudioService {
            return this@AudioService
        }
    }

    val mBinder = AudioBinder()

    override fun onCreate() {
        Log.i("thach", "AudioService onCreate")
        super.onCreate()
    }

    override fun onBind(intent: Intent): IBinder {
        Log.i("thach", "AudioService onBind")
        return mBinder
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.i("thach", "AudioService onTaskRemoved")

        mMediaPlayer?.stop()
        mMediaPlayer?.release()
    }

    fun playAudioFromURL(urlStr: String) {
        mMediaPlayer?.run {
            if (isPlaying) {
                pause()
                release()
            }
        }

        val uri = Uri.parse(urlStr)

        mMediaPlayer = MediaPlayer()
        mMediaPlayer?.apply {
            setDataSource(applicationContext, uri)
            setOnPreparedListener {
                start()
            }
            prepareAsync()
        }
    }

    fun pauseAudio() {
        mMediaPlayer?.pause()
    }

    fun resumeAudio() {
        mMediaPlayer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("thach", "AudioPlayService onDestroy")
    }
}
