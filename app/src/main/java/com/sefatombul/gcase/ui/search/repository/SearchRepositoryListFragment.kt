package com.sefatombul.gcase.ui.search.repository

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sefatombul.gcase.R
import com.sefatombul.gcase.adapters.search.SearchRepositoryListAdapter
import com.sefatombul.gcase.data.model.search.Sort
import com.sefatombul.gcase.databinding.FragmentSearchRepositoryListBinding
import com.sefatombul.gcase.ui.MainActivity
import com.sefatombul.gcase.ui.search.DateRangeBottomSheetFragment
import com.sefatombul.gcase.ui.search.sort.SortBottomSheetFragment
import com.sefatombul.gcase.utils.*
import com.sefatombul.gcase.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchRepositoryListFragment : Fragment() {
    var _binding: FragmentSearchRepositoryListBinding? = null
    val binding: FragmentSearchRepositoryListBinding get() = _binding!!

    private val sortBottomSheet: SortBottomSheetFragment by lazy { SortBottomSheetFragment() }
    private val dateRangeBottomSheetFragment: DateRangeBottomSheetFragment by lazy { DateRangeBottomSheetFragment() }
    var sort: Sort = Sort("Best Match", null, true)
    var order: Sort = Sort("Highest number of matches", "desc", true)
    var dateRange: Sort = Sort("All Time", null, true)

    /**
     * Search Repository List ekranı ilk defa acıldıysa false değerini alır.
     * Bir sonraki fragmentta geri gelinme işlemi yapıldıysa true değerini alır
     * */
    var isPopBackStack = false

    val searchViewModel: SearchViewModel by viewModels()

    /**
     * Her sayfalama için gösterilecek item sayısı
     * */
    private val pageSize = 30

    /**
     * Gösterilen itemların hangi sayfa dilimine ait olduğu
     * */
    private var page = 1

    /**
     * Aratılan değerin remote tarafta kaç item ile eşleştiği bilgisi
     * */
    private var total = 0

    /**
     * Aratılan kelime
     * */
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
        if (!isPopBackStack) recyclerviewSetup()
        subscribeObversers()
        handleClickEventsListener()
        /**
         * Ekran ilk defa acıldıysa aranan kelime için ilk sayfa bilgisi alınır
         * */
        if (!isPopBackStack) performRepositorySearch()
    }

    private fun showSortBottomSheet() {
        val sortList = arrayListOf(
            Sort("Best Match", null, false),
            Sort("Stars", "stars", false),
            Sort("Forks", "forks", false),
            Sort("Help Wanted Issues", "help-wanted-issues", false),
            Sort("Updated", "updated", false),
        )
        sortList.forEach { item ->
            if (item.key == sort.key) {
                item.isChecked = true
            }
        }
        sortBottomSheet.setSortList(
            sortList
        )

        val orderList = arrayListOf(
            Sort("Highest number of matches", "desc", false),
            Sort("Lowest number of matches", "asc", false),
        )
        orderList.forEach { item ->
            if (item.key == order.key) {
                item.isChecked = true
            }
        }

        sortBottomSheet.setOrderList(
            orderList
        )

        sortBottomSheet.applySetOnClickListener { sortSelected, orderSelected ->
            page = 1
            if (sortSelected != null) {
                sort = sortSelected
            }
            if (orderSelected != null) {
                order = orderSelected
            }
            performRepositorySearch()
        }

        if (!sortBottomSheet.isAdded) {
            sortBottomSheet.show(
                requireActivity().supportFragmentManager, "sortBottomSheet"
            )
        }
    }

    private fun showDateRangeBottomSheet() {
        val dateRangeList = arrayListOf(
            Sort("Created in the last month", "1", false),
            Sort("Created in the last 3 month", "3", false),
            Sort("Created in the last 6 month", "6", false),
            Sort("Created in the last 1 year", "12", false),
            Sort("All Time", null, false),
        )
        dateRangeList.forEach { item ->
            if (item.key == dateRange.key) {
                item.isChecked = true
            }
        }
        dateRangeBottomSheetFragment.setDateRangeList(
            dateRangeList
        )

        dateRangeBottomSheetFragment.applySetOnClickListener { dateRangeSelected ->
            page = 1
            if (dateRangeSelected != null) {
                dateRange = dateRangeSelected
            }
            performRepositorySearch()
        }

        if (!dateRangeBottomSheetFragment.isAdded) {
            dateRangeBottomSheetFragment.show(
                requireActivity().supportFragmentManager, "dateRangeBottomSheetFragment"
            )
        }
    }

    private fun handleClickEventsListener() {
        binding.apply {
            ivBack.setOnClickListener {
                findNavController().backStackCustom()
            }
            ivSort.setOnClickListener {
                showSortBottomSheet()
            }
            ivDate.setOnClickListener {
                showDateRangeBottomSheet()
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
            var range: String? = null
            when (dateRange.key) {
                "1" -> {
                    range = "created:${manipulateDate(TimeUnits.MONTHS, -1)}..${getTodayDate()}"
                }
                "3" -> {
                    range = "created:${manipulateDate(TimeUnits.MONTHS, -3)}..${getTodayDate()}"
                }
                "6" -> {
                    range = "created:${manipulateDate(TimeUnits.MONTHS, -6)}..${getTodayDate()}"
                }
                "12" -> {
                    range = "created:${manipulateDate(TimeUnits.YEARS, -1)}..${getTodayDate()}"
                }
            }
            if (!searchText.isNullOrBlank()) {
                searchViewModel.searchRepository(
                    searchText = searchText!!,
                    pageSize = pageSize,
                    page = page,
                    sort = sort.key,
                    order = order.key ?: "desc",
                    dateRange = range
                )
            }
        }
    }

    /**
     * Repository listesi boş ise empty text görünür olacak
     * liste dolu ise empty text gizlenecek
     * */
    private fun repositoryListControl() {
        binding.apply {
            if (searchRepositoryListAdapter.list.isNullOrEmpty()) {
                tvNothingSee.show()
            } else {
                tvNothingSee.remove()
            }
        }
    }

    private fun subscribeObversers() {
        searchViewModel.apply {
            searchRepositoryResponse.observeCall(requireActivity(),
                viewLifecycleOwner,
                error = {},
                loading = {
                    /**
                     * Sayfa ilk açıldıgında loading görünsün
                     * Her sayfalama için loading kapatıldı
                     * */
                    if (page == 1) {
                        (requireActivity() as? MainActivity)?.showLoading()
                    }
                },
                isAutoShowLoading = false,
                success = { response ->
                    response?.let { result ->
                        total = result.totalCount ?: 0
                        if (page == 1) {
                            /**
                             * Adaptör listesi temizlenir gelen veriler temiz liste üzerine verilir
                             * */
                            searchRepositoryListAdapter.setList(result.items)
                        } else if (page > 1) {
                            /**
                             * Adaptör listesi temizlenmez. Sayfalama işlemi sonucudur
                             * Gelen veriler var olan listenin üzerine eklenir
                             * */
                            searchRepositoryListAdapter.addList(result.items)
                        }
                    }
                },
                finally = {
                    clearSearchRepositoryResponse()
                    if (page == 1) {
                        (requireActivity() as? MainActivity)?.hideLoading()
                    }
                    repositoryListControl()
                })
        }
    }

}