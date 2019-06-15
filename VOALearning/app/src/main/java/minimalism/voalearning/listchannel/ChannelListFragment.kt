package minimalism.voalearning.listchannel


import android.os.Build
import android.os.Bundle
import android.text.Html
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
import minimalism.voalearning.databinding.FragmentListChannelBinding
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

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

        val binding = FragmentListChannelBinding.inflate(inflater, container, false)

        binding.rcChannel.apply {
            adapter = ChannelAdapter(mChannelList)
        }

        return binding.root
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
                Log.i("thach", "fetchChannelListFromPodcast err: ${err}")
            })

        queue.add(stringRequest)
    }

    private fun initChannelList(data: String) {
        val markTitleOpen = "class=\"img-wrap\" title=\""
        val markTitleClose = "\">"

        val markImageOpen = "<img data-src=\""
        val markImageClose = "\" src="

        val markZoneOpen = "zoneId="
        val markZoneClose = "\">"

        val scanner = Scanner(data)
        var title = ""
        var imageUrl = ""
        var zoneId = ""
        var lo = 0
        var hi = 0
        var line = ""

        while (scanner.hasNext()) {
            line = scanner.nextLine()

            lo = line.indexOf(markTitleOpen)
            if (lo >= 0) {
                hi = line.indexOf(markTitleClose)
                title = line.substring(lo + markTitleOpen.length, hi)
            }

            lo = line.indexOf(markImageOpen)
            if (lo >= 0) {
                hi = line.indexOf(markImageClose)
                imageUrl = line.substring(lo + markImageOpen.length, hi)
            }

            lo = line.indexOf(markZoneOpen)
            if (lo >= 0) {
                hi = line.indexOf(markZoneClose)
                zoneId = line.substring(lo + markZoneOpen.length, hi)


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    title = Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY).toString()
                    zoneId = Html.fromHtml(zoneId, Html.FROM_HTML_MODE_LEGACY).toString()
                }
                else {
                    title = Html.fromHtml(title).toString()
                    zoneId = Html.fromHtml(zoneId).toString()
                }

//                Log.i("thach", "$title")
//                Log.i("thach", "$imageUrl")
//                Log.i("thach", "$zoneId")
//                Log.i("thach", "--------")

                mChannelList.add( ChannelInfo(title, imageUrl, zoneId))
            }
        }
    }
}
