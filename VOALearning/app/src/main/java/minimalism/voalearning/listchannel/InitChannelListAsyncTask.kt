package minimalism.voalearning.listchannel

import android.os.AsyncTask
import android.os.Build
import android.text.Html
import android.util.Log
import java.util.*
import kotlin.collections.ArrayList

class InitChannelListAsyncTask(var mFragment: ChannelListFragment): AsyncTask<String, Void, Void>() {

    lateinit var mChannelList: ArrayList<ChannelInfo>

    override fun doInBackground(vararg params: String): Void? {
        val data:String = params[0]
        initChannelList(data)

        return null
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)

        mFragment.mBinding.rcChannel.apply {
            adapter = ChannelAdapter(mChannelList)
        }
    }

    private fun initChannelList(data: String) {
        mChannelList = ArrayList()

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