package minimalism.voalearning.listchannel

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import minimalism.voalearning.R

class ChannelAdapter() : RecyclerView.Adapter<ChannelAdapter.ChannelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.channel_item_layout, parent, false)

        return ChannelViewHolder(itemView)
    }

    override fun getItemCount(): Int {
       return 10
    }

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {

    }

    class ChannelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

}