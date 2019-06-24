package minimalism.voalearning.audioplayer

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.util.Log

class AudioService : Service() {

    lateinit var mMediaPlayer: MediaPlayer

    inner class AudioBinder: Binder() {
        fun getService(): AudioService{
            return this@AudioService
        }
    }

    val mBinder = AudioBinder()

    override fun onCreate() {
        super.onCreate()

        mMediaPlayer = MediaPlayer()
    }

    override fun onBind(intent: Intent): IBinder {
        Log.i("thach", "AudioBinder onBind")
        return mBinder
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.i("thach", "AudioBinder onBind")

        mMediaPlayer.stop()
        mMediaPlayer.release()
    }

    fun playAudioFromURL(urlStr: String) {
        val uri = Uri.parse(urlStr)

        mMediaPlayer.apply {
            setDataSource(applicationContext, uri)
            setOnPreparedListener {
                start()
            }
            prepareAsync()
        }
    }
}
