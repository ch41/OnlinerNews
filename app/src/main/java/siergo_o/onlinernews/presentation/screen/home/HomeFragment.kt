package siergo_o.onlinernews.presentation.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.tabs.TabLayoutMediator
import dagger.android.support.DaggerFragment
import siergo_o.onlinernews.R
import siergo_o.onlinernews.databinding.FragmentHomeBinding
import siergo_o.onlinernews.domain.news.model.RssChannel
import siergo_o.onlinernews.presentation.screen.BaseFragment
import siergo_o.onlinernews.presentation.utils.SimpleTextWatcher
import siergo_o.onlinernews.presentation.utils.hideKeyboard
import siergo_o.onlinernews.presentation.utils.showKeyboard
import javax.inject.Inject

class HomeFragment : DaggerFragment(), BaseFragment, HomeFragmentContract.Ui {

    companion object {
        private const val KEY_IS_SEARCH_SHOWN = "isSearchShown"
    }

    @Inject
    lateinit var homeFragmentPresenter: HomeFragmentPresenter
    private var isSearchShown: Boolean = false
    private var _viewBinding: FragmentHomeBinding? = null
    private val viewBinding: FragmentHomeBinding
        get() = _viewBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            FragmentHomeBinding.inflate(inflater, container, false).also { _viewBinding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        homeFragmentPresenter.start(this)
        viewBinding.layoutContent.apply {
            toolbar.apply {
                setNavigationOnClickListener {
                    navigationIcon = null
                    searchNews.apply {
                        text = null
                        visibility = View.GONE
                        hideKeyboard(context, view)
                    }
                    buttonSearch.visibility = View.VISIBLE
                    onlinerLogo.visibility = View.VISIBLE
                    isSearchShown = false
                }
            }
            buttonSearch.setOnClickListener {
                toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
                it.visibility = View.GONE
                onlinerLogo.visibility = View.GONE
                searchNews.apply {
                    addTextChangedListener(searchTextWatcher)
                    visibility = View.VISIBLE
                    showKeyboard(context, view)
                }
                isSearchShown = true
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding.layoutContent.searchNews.removeTextChangedListener(searchTextWatcher)
        _viewBinding = null
        homeFragmentPresenter.stop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_IS_SEARCH_SHOWN, isSearchShown)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        isSearchShown = savedInstanceState?.getBoolean(KEY_IS_SEARCH_SHOWN)?:false
        if (isSearchShown) {
            viewBinding.layoutContent.buttonSearch.performClick()
        }
    }

    override fun setViewPager(news: List<RssChannel>) {
        viewBinding.layoutContent.apply {
            viewpager.adapter = ViewPagerAdapter(this@HomeFragment)
            TabLayoutMediator(this.tablayout, this.viewpager) { tab, position ->
                viewBinding.layoutContent.viewpager.setCurrentItem(tab.position, true)
                tab.text = when (position) {
                    0 -> context?.getString(R.string.tech)
                    1 -> context?.getString(R.string.people)
                    2 -> context?.getString(R.string.auto)
                    else -> ""
                }
            }.attach()
        }
    }

    override fun showLoading(show: Boolean) {
        if (show) {
            viewBinding.apply {
                layoutLoading.root.visibility = View.VISIBLE
                layoutContent.root.visibility = View.GONE
            }
        } else viewBinding.apply {
            layoutLoading.root.visibility = View.GONE
            layoutContent.root.visibility = View.VISIBLE
        }
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }

    private val searchTextWatcher = SimpleTextWatcher { text ->
        homeFragmentPresenter.search(text.toString())
    }
}