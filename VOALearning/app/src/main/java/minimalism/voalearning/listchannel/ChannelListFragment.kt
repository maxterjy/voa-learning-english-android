package minimalism.voalearning.listchannel


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import minimalism.voalearning.R
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ListChannelFragment : Fragment() {

    var mChannelList: ArrayList<ChannelInfo> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        fetchChannelListFromPodcast()

        val itemView = inflater.inflate(R.layout.fragment_list_channel, container, false)

        var rcChannel:RecyclerView = itemView.findViewById(R.id.rc_channel)
        rcChannel.apply {
            adapter = ChannelAdapter()
        }

        return itemView
    }

    private fun fetchChannelListFromPodcast() {
        val queue = Volley.newRequestQueue(context)
        val url = "https://learningenglish.voanews.com/podcasts"
        val stringRequest = StringRequest(Request.Method.GET, url,
            Response.Listener<String> {response ->
                File("/sdcard/debug.txt").writeText(response)

                initChannelList(response)
            },
            Response.ErrorListener {err ->
                Log.i("thach", "res err: ${err}")
            })

        queue.add(stringRequest)
    }

    private fun initChannelList(data: String) {
        val markLo = "<span class=\"title\""
        val markHi = "</span>"

        var lo = data.indexOf(markLo)
        var hi = data.indexOf(markHi, lo)

        while (lo >= 0 ) {
            lo += markLo.length + 1

            var s = data.substring(lo , hi)
            s = s.replace("&#39;", "\'")
            s = s.replace("&amp;", "&")

            Log.i("thach", "item: $s")

            lo = data.indexOf(markLo, hi)
            hi = data.indexOf(markHi, lo)
        }
    }


}
