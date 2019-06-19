package minimalism.voalearning.newslist


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import minimalism.voalearning.databinding.FragmentNewsListBinding

class NewsListFragment : Fragment() {

    lateinit var mBinding: FragmentNewsListBinding
    lateinit var mNewsAdapter: NewsListAdapter

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
}
