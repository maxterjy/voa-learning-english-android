package minimalism.voalearning.listchannel


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import minimalism.voalearning.R
import minimalism.voalearning.databinding.ChannelItemLayoutBinding

class ChannelAdapter(var mChannelList: ArrayList<ChannelInfo>) : RecyclerView.Adapter<ChannelAdapter.ChannelViewHolder>() {

    interface OnChannelClickListener {
        fun onChannelItemClick(itemIndex: Int)
    }

    var mChannelClickListener: OnChannelClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ChannelItemLayoutBinding.inflate(
            inflater, parent, false)

        return ChannelViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return mChannelList.size
    }

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        holder.binding.tvTitle.setText(mChannelList[position].mTitle)
        holder.binding.tvZoneid.setText("Zone ${mChannelList[position].mZoneId}")

        Picasso.get().load(mChannelList[position].mImageUrl).into(holder.binding.ivChannelIcon)
    }

    //ViewHolder
    inner class ChannelViewHolder(val binding: ChannelItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener({view ->
                mChannelClickListener?.onChannelItemClick(adapterPosition)
            })
        }
    }
}