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
import minimalism.voalearning.databinding.FragmentAudioPlayBinding


class AudioPlayFragment : Fragment() {

    lateinit var mFragmentBinding: FragmentAudioPlayBinding
    lateinit var mArgs: AudioPlayFragmentArgs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("thach", "AudioPlayFragment onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("thach", "AudioPlayFragment onCreateView")

        mFragmentBinding = FragmentAudioPlayBinding.inflate(inflater, container, false)
//        mArgs = AudioPlayFragmentArgs.fromBundle(arguments)

//        val urlStrl = mArgs.newsInfo.mAudioUrl
//        AudioPlayServiceHolder.mAudioService?.startAudioFromURL(urlStrl)

        return mFragmentBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}