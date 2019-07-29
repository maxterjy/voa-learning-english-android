package minimalism.voalearning.listchannel

import android.app.Application
import android.content.Context
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.util.*
import kotlin.collections.ArrayList

class ChannelListViewModel(val app: Application): AndroidViewModel(app) {

    lateinit var mChannelList: ArrayList<ChannelInfo>

    var mIsFetchFinished = MutableLiveData<Boolean>().apply {
        value = false
    }

    init {
        fetchChannelListFromPodcast()
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("thach", "ChannelListViewModel onCleared")
    }

    private fun fetchChannelListFromPodcast() {
        Log.i("thach", "ChannelListViewModel fetchChannelListFromPodcast")

        val queue = Volley.newRequestQueue(app.applicationContext)
        val url = "https://learningenglish.voanews.com/podcasts"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                initChannelList(response)
            },
            Response.ErrorListener { err ->
                Log.i("thach", "fetchChannelListFromPodcast err: ${err}")
            })

        queue.add(stringRequest)
    }

    private fun initChannelList(data: String){
        Log.i("thach", "ChannelListViewModel initChannelList ${Thread.currentThread().name}")
        mChannelList = ArrayList<ChannelInfo>()

        val markTitleOpen = "class=\"img-wrap\" title=\""
        val markTitleClose = "\">"

        val markImageOpen = "<img data-src=\""
        val markImageClose = "\" src="

        val markSummaryOpen = "<p class=\"perex\">"
        val markSummaryClose = "</p>"

        val markZoneOpen = "zoneId="
        val markZoneClose = "\">"

        val scanner = Scanner(data)
        var title = ""
        var imageUrl = ""
        var summary = ""
        var zoneId = ""
        var lo = 0
        var hi = 0
        var line = ""

        while (scanner.hasNext()) {
            line = scanner.nextLine()

            //get title
            lo = line.indexOf(markTitleOpen)
            if (lo >= 0) {
                hi = line.indexOf(markTitleClose)
                title = line.substring(lo + markTitleOpen.length, hi)

                continue
            }

            //get imageUrl
            lo = line.indexOf(markImageOpen)
            if (lo >= 0) {
                hi = line.indexOf(markImageClose)
                imageUrl = line.substring(lo + markImageOpen.length, hi)

                continue
            }

            //get summary
            lo = line.indexOf(markSummaryOpen)
            if (lo >= 0) {
                try {
                    hi = line.indexOf(markSummaryClose)
                    summary = line.substring(lo + markSummaryOpen.length, hi)
                }
                catch (ex: StringIndexOutOfBoundsException) {
                    summary = line.substring(lo + markSummaryOpen.length)
                }



                continue
            }

            //get zoneid
            lo = line.indexOf(markZoneOpen)
            if (lo >= 0) {
                hi = line.indexOf(markZoneClose)
                zoneId = line.substring(lo + markZoneOpen.length, hi)


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    title = Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY).toString()
                    summary = Html.fromHtml(summary, Html.FROM_HTML_MODE_LEGACY).toString()
                    zoneId = Html.fromHtml(zoneId, Html.FROM_HTML_MODE_LEGACY).toString()
                }
                else {
                    title = Html.fromHtml(title).toString()
                    summary = Html.fromHtml(summary).toString()
                    zoneId = Html.fromHtml(zoneId).toString()
                }

                Log.i("thach", "$title")
                Log.i("thach", "$imageUrl")
                Log.i("thach", "$summary")
                Log.i("thach", "$zoneId")
                Log.i("thach", "--------")

                mChannelList.add( ChannelInfo(title, imageUrl, zoneId, summary))
            }
        }

        mIsFetchFinished.value = true
    }

}

