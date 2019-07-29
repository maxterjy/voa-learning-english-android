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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import androidx.lifecycle.Observer
import minimalism.voalearning.R
import minimalism.voalearning.databinding.FragmentListChannelBinding
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class ChannelListFragment : Fragment(), ChannelAdapter.OnChannelClickListener {

    lateinit var mBinding: FragmentListChannelBinding
    lateinit var mViewModel: ChannelListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("thach", "ChannelListFragment onCreate")

        val factory = ChannelListViewModelFactory(activity!!.application)
        mViewModel = ViewModelProviders.of(this, factory).get(ChannelListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBinding = FragmentListChannelBinding.inflate(
            inflater, container, false)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.title = "VOA Learning English"


        if (mViewModel.mIsFetchFinished.value == true) {
            showChannels()
        }
        else {
            mViewModel.mIsFetchFinished.observe(this, Observer { isFinished ->
                if (isFinished) {
                    showChannels()
                }
            })
        }

        return mBinding.root
    }

    override fun onChannelItemClick(itemIndex: Int) {
        val title = mViewModel.mChannelList[itemIndex].mTitle
        val zoneId = mViewModel.mChannelList[itemIndex].mZoneId

        findNavController().navigate(ChannelListFragmentDirections.actionListChannelFragmentToNewsListFragment(zoneId, title))
    }

    fun showChannels() {
        mBinding.loadingChannelBar.visibility = View.INVISIBLE

        val adapter = ChannelAdapter(mViewModel.mChannelList)
        adapter.mChannelClickListener = this
        mBinding.rcChannel.adapter = adapter
    }
}
