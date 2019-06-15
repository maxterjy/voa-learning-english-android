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
                Log.i("thach", "fetchChannelListFromPodcast err: ${err}")
            })

        queue.add(stringRequest)
    }

    private fun initChannelList(data: String) {
        val markTitleOpen = "class=\"img-wrap\" title=\""
        val markTitleClose = "\">"

        val markImageOpen = "<img data-src=\""
        val markImageClose = "\" src="

        val markZoneOpen = "<a class=\"link-service\" href=\""
        val markZoneClose = "\">"


        var lo = data.indexOf(markTitleOpen)
        var hi = data.indexOf(markTitleClose, lo)

        while (lo >= 0 ) {
            //get title
            lo += markTitleOpen.length

            var title = data.substring(lo , hi)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                title = Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY).toString()
            }
            else {
                title = Html.fromHtml(title).toString()
            }


            //get imageURL
            lo = data.indexOf(markImageOpen, hi)
            lo += markImageOpen.length
            hi = data.indexOf(markImageClose, lo)

            var imageUrl = data.substring(lo, hi)

            //get zoneID
            lo = data.indexOf(markZoneOpen, hi)
            lo += markZoneOpen.length
            hi = data.indexOf(markZoneClose, lo)

            var zoneId = data.substring(lo, hi)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                zoneId = Html.fromHtml(zoneId, Html.FROM_HTML_MODE_LEGACY).toString()
            }
            else {
                zoneId = Html.fromHtml(zoneId).toString()
            }

            Log.i("thach", "item: $title")
            Log.i("thach", "item: $imageUrl")
            Log.i("thach", "item: $zoneId")
            Log.i("thach", "----------------")

            mChannelList.add( ChannelInfo(title, imageUrl, zoneId))

            lo = data.indexOf(markTitleOpen, hi)
            hi = data.indexOf(markTitleClose, lo)
        }
    }
}
