package minimalism.voalearning.newslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import minimalism.voalearning.databinding.NewsItemLayoutBinding


class NewsListAdapter(): RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsListAdapter.NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NewsItemLayoutBinding.inflate(inflater, parent, false)

        return NewsViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: NewsListAdapter.NewsViewHolder, position: Int) {

    }

    class NewsViewHolder(binding: NewsItemLayoutBinding): RecyclerView.ViewHolder(binding.root){

    }

}