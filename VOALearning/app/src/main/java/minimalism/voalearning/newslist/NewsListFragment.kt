package minimalism.voalearning.newslist


import android.os.Bundle
import android.util.Log
import android.util.Xml
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.Response.Listener
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import minimalism.voalearning.databinding.FragmentNewsListBinding
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.File
import java.io.StringReader

class NewsListFragment : Fragment() {

    lateinit var mBinding: FragmentNewsListBinding
    lateinit var mNewsAdapter: NewsListAdapter
    var ns: String? = null
    lateinit var mNewsList: ArrayList<NewsInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadNewsList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =  FragmentNewsListBinding.inflate(inflater, container, false)


        mNewsAdapter = NewsListAdapter()
        mBinding.rcNewsList.apply {
            adapter = mNewsAdapter
        }

        return mBinding.root
    }

    private fun loadNewsList() {
        val queue = Volley.newRequestQueue(context)
        val url = "https://learningenglish.voanews.com/podcast/?count=20&zoneId=986"
        val stringRequest = StringRequest(Request.Method.GET, url,
            Response.Listener<String> {response ->
//                File("/sdcard/debug.txt").writeText(response)

                parseNews(response)
            },
            Response.ErrorListener {err ->
                Log.i("thach", "loadNewsList err: ${err}")
            })

        queue.add(stringRequest)
    }

    private fun parseNews(data: String) {
        mNewsList = ArrayList()

        val stream = StringReader(data)

        val factory = XmlPullParserFactory.newInstance()
        var parser = factory.newPullParser()
        parser.setInput(stream)

        while (parser.eventType != XmlPullParser.END_DOCUMENT) {

            if (parser.eventType == XmlPullParser.START_TAG && parser.name == "item") {
                parseItem(parser)
            }


            parser.next()
        }
    }

    private fun parseItem(parser: XmlPullParser) {
        var title = ""
        var duration = ""
        var url = ""

        while (! (parser.eventType == XmlPullParser.END_TAG && parser.name == "item")) {
            if (parser.eventType == XmlPullParser.START_TAG) {
                when (parser.name) {
                    "title" -> {
                        parser.next()
                        title = parser.text

                    }

                    "itunes:duration" -> {
                        parser.next()
                        duration = parser.text
                    }

                    "enclosure" -> {
                        parser.next()
                        url = parser.getAttributeValue(ns, "url")
                    }
                }
            }

            parser.next()
        }

        mNewsList.add(NewsInfo(title, duration, url))
    }
}
