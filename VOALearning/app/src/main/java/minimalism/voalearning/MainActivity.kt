package minimalism.voalearning

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import minimalism.voalearning.audioplayer.AudioPlayServiceHolder
import minimalism.voalearning.audioplayer.AudioService

class MainActivity : AppCompatActivity() {

    lateinit var mService: AudioService

    val mServiceConnection = object: ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i("thach", "mServiceConnection onServiceConnected")
            val binder = service as AudioService.AudioBinder
            mService = binder.getService()

            AudioPlayServiceHolder.mAudioService = mService

//            mService.playAudioFromURL("https://av.voanews.com/clips/VLE/2019/06/20/b49db470-2c73-4d08-a809-36560d44c5b6_hq.mp3")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i("thach", "mServiceConnection onServiceDisconnected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindAudioService()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun bindAudioService() {
        val intent = Intent(this, AudioService::class.java)
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)
    }
}
