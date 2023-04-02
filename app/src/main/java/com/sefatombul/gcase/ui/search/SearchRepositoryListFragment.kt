package com.sefatombul.gcase.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sefatombul.gcase.R
import com.sefatombul.gcase.adapters.search.SearchRepositoryListAdapter
import com.sefatombul.gcase.databinding.FragmentSearchRepositoryListBinding
import com.sefatombul.gcase.ui.MainActivity
import com.sefatombul.gcase.utils.Constants
import com.sefatombul.gcase.utils.backStackCustom
import com.sefatombul.gcase.utils.observeCall
import com.sefatombul.gcase.viewmodels.RecentSearchViewModel
import com.sefatombul.gcase.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchRepositoryListFragment : Fragment() {
    var _binding: FragmentSearchRepositoryListBinding? = null
    val binding: FragmentSearchRepositoryListBinding get() = _binding!!

    var isPopBackStack = false

    val searchViewModel: SearchViewModel by viewModels()

    private val pageSize = 30
    private var page = 1
    private var total = 0
    private var searchText: String? = null

    val searchRepositoryListAdapter: SearchRepositoryListAdapter by lazy { SearchRepositoryListAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleArguments()
    }

    private fun handleArguments() {
        arguments?.let {
            searchText = it.getString(Constants.SEARCH_KEY_BUNDLE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        if (_binding == null) {
            _binding = FragmentSearchRepositoryListBinding.inflate(
                inflater, container, false
            )
        } else {
            isPopBackStack = true
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as? MainActivity)?.hideBottomNavigation()
        recyclerviewSetup()
        subscribeObversers()
        handleClickEventsListener()
        if (!isPopBackStack) performRepositorySearch()
    }

    private fun handleClickEventsListener() {
        binding.apply {
            ivBack.setOnClickListener {
                findNavController().backStackCustom()
            }

            searchRepositoryListAdapter.apply {
                setOnClickListener { item, position ->
                    val bundle = Bundle().apply {
                        putString(Constants.REPOSITORY_USER, item.owner?.login)
                        putString(Constants.REPOSITORY_NAME, item.name)
                    }
                    findNavController().navigate(
                        R.id.action_searchRepositoryListFragment_to_searchRepositoryDetailFragment,
                        bundle
                    )
                }
            }
        }
    }

    private fun recyclerviewSetup() {
        binding.apply {
            rvRepositories.adapter = searchRepositoryListAdapter
            val layoutManager = rvRepositories.layoutManager as LinearLayoutManager
            ContextCompat.getDrawable(requireContext(), R.drawable.divider)?.let {
                val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
                itemDecorator.setDrawable(it)
                rvRepositories.addItemDecoration(itemDecorator)
            }

            rvRepositories.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    setupRecyclerViewPagination(layoutManager)
                }
            })
        }
    }

    fun setupRecyclerViewPagination(layoutManager: LinearLayoutManager) {
        binding.apply {
            val totalItemCount = layoutManager.itemCount
            val lastVisible: Int = layoutManager.findLastVisibleItemPosition()
            val endHasBeenReached = lastVisible + 10 >= totalItemCount

            if (totalItemCount > 0 && endHasBeenReached) {
                if (searchRepositoryListAdapter.list.size < total && page * pageSize < total && searchRepositoryListAdapter.list.size >= page * pageSize) {
                    page += 1
                    performRepositorySearch()
                }
            }
        }
    }

    private fun performRepositorySearch() {
        binding.apply {
            if (!searchText.isNullOrBlank()) {
                searchViewModel.searchRepository(
                    searchText!!, pageSize, page
                )
            }
        }
    }

    private fun subscribeObversers() {
        searchViewModel.apply {
            searchRepositoryResponse.observeCall(requireActivity(),
                viewLifecycleOwner,
                error = {},
                loading = {
                    if (page == 1) {
                        (requireActivity() as? MainActivity)?.showLoading()
                    }
                },
                isAutoShowLoading = false,
                success = { response ->
                    response?.let { result ->
                        total = result.totalCount ?: 0
                        if (page == 1) {
                            searchRepositoryListAdapter.setList(result.items)
                        } else if (page > 1) {
                            searchRepositoryListAdapter.addList(result.items)
                        }
                    }
                },
                finally = {
                    clearSearchRepositoryResponse()
                    if (page == 1) {
                        (requireActivity() as? MainActivity)?.hideLoading()
                    }
                })
        }
    }

}