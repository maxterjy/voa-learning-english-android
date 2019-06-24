package minimalism.voalearning.audioplayer


import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import minimalism.voalearning.R


class AudioPlayFragment : Fragment() {

    lateinit var mService: AudioService

    val mServiceConnection = object: ServiceConnection{

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i("thach", "mServiceConnection onServiceConnected")
            val binder = service as AudioService.AudioBinder
            mService = binder.getService()

            mService.playAudioFromURL("https://av.voanews.com/clips/VLE/2019/06/20/b49db470-2c73-4d08-a809-36560d44c5b6_hq.mp3")
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startAudioService()
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_audio_play, container, false)
    }

    fun startAudioService() {
        val intent = Intent(context, AudioService::class.java)
        activity!!.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)
    }
}
