package minimalism.voalearning.listchannel


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import minimalism.voalearning.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ListChannelFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val itemView = inflater.inflate(R.layout.fragment_list_channel, container, false)

        var rcChannel:RecyclerView = itemView.findViewById(R.id.rc_channel)
        rcChannel.apply {
            adapter = ChannelAdapter()
        }

        return itemView
    }


}
