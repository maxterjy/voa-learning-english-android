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
        holder.binding.tvNewsTitle.setText(mNewsList[position].mTitle)
        holder.binding.tvNewsDuration.setText(mNewsList[position].mDuration)
    }

    class NewsViewHolder(var binding: NewsItemLayoutBinding): RecyclerView.ViewHolder(binding.root){

    }

}