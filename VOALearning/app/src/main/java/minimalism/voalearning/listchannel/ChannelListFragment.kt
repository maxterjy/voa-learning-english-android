package minimalism.voalearning.listchannel


import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.util.MutableInt
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
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


class ChannelListFragment : Fragment(), ChannelAdapter.OnChannelClickListener {


    lateinit var mChannelList: ArrayList<ChannelInfo>
    lateinit var mBinding: FragmentListChannelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBinding = FragmentListChannelBinding.inflate(
            inflater, container, false)
        fetchChannelListFromPodcast()

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.title = "VOA Learning English"

        return mBinding.root
    }

    private fun fetchChannelListFromPodcast() {
        val queue = Volley.newRequestQueue(context)
        val url = "https://learningenglish.voanews.com/podcasts"
        val stringRequest = StringRequest(Request.Method.GET, url,
            Response.Listener<String> {response ->
                mBinding.loadingChannelBar.visibility = View.INVISIBLE

                initChannelList(response)
            },
            Response.ErrorListener {err ->
                Log.i("thach", "fetchChannelListFromPodcast err: ${err}")
            })

        queue.add(stringRequest)
    }

    private fun initChannelList(data: String?) {
        val task = InitChannelListAsyncTask(this)
        task.execute(data)
    }

    override fun onChannelItemClick(itemIndex: Int) {
        val title = mChannelList[itemIndex].mTitle
        val zoneId = mChannelList[itemIndex].mZoneId

        findNavController().navigate(ChannelListFragmentDirections.actionListChannelFragmentToNewsListFragment(zoneId, title))
    }
}
