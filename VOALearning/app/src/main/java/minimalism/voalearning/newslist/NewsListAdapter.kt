package minimalism.voalearning.newslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import minimalism.voalearning.databinding.NewsItemLayoutBinding


class NewsListAdapter(var mNewsList: ArrayList<NewsInfo>): RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsListAdapter.NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NewsItemLayoutBinding.inflate(inflater, parent, false)

        return NewsViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return mNewsList.size
    }

    override fun onBindViewHolder(holder: NewsListAdapter.NewsViewHolder, position: Int) {
        holder.binding.tvNewsTitle.setText(getTitle(mNewsList[position].mTitle))
        holder.binding.tvNewsDate.setText(getDate(mNewsList[position].mTitle))
        holder.binding.tvNewsDuration.setText(getDuration(mNewsList[position].mDuration))
    }

    private fun getDuration(str: String): String {
        return str.substring(3)
    }

    private fun getDate(str: String): String {
        var pos = str.lastIndexOf('-')
        if (pos >= 0) {
            return str.substring(pos+2)
        }

        return ""
    }

    private fun getTitle(str: String): String {
        var pos = str.lastIndexOf('-')
        if (pos >= 0) {
            return str.substring(0, pos)
        }

        return str
    }

    class NewsViewHolder(var binding: NewsItemLayoutBinding): RecyclerView.ViewHolder(binding.root){

    }
}