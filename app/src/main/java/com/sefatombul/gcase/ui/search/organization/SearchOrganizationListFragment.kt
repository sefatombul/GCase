package com.sefatombul.gcase.ui.search.organization

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
import com.sefatombul.gcase.adapters.search.SearchPersonListAdapter
import com.sefatombul.gcase.data.local.UserSearchTypeEnum
import com.sefatombul.gcase.data.model.search.Sort
import com.sefatombul.gcase.databinding.FragmentSearchOrganizationListBinding
import com.sefatombul.gcase.ui.MainActivity
import com.sefatombul.gcase.ui.search.sort.SortBottomSheetFragment
import com.sefatombul.gcase.utils.*
import com.sefatombul.gcase.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchOrganizationListFragment : Fragment() {
    var _binding: FragmentSearchOrganizationListBinding? = null
    val binding: FragmentSearchOrganizationListBinding get() = _binding!!

    val sortBottomSheet: SortBottomSheetFragment by lazy { SortBottomSheetFragment() }
    var sort: Sort = Sort("Best Match", null, true)
    var order: Sort = Sort("Highest number of matches", "desc", true)

    /**
     * Search Organization List ekranı ilk defa acıldıysa false değerini alır.
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

    private val searchOrganizationListAdapter: SearchPersonListAdapter by lazy { SearchPersonListAdapter() }
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
            _binding = FragmentSearchOrganizationListBinding.inflate(
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
        if (!isPopBackStack) performOrganizationSearch()
    }

    private fun showSortBottomSheet() {
        val sortList = arrayListOf(
            Sort("Best Match", null, false),
            Sort("Followers", "followers", false),
            Sort("Repositories", "repositories", false),
            Sort("Joined", "joined", false),
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
            performOrganizationSearch()
        }

        if (!sortBottomSheet.isAdded) {
            sortBottomSheet.show(
                requireActivity().supportFragmentManager, "sortBottomSheet"
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
            searchOrganizationListAdapter.apply {
                setOnClickListener { item, position ->
                    val bundle = Bundle().apply {
                        putString(Constants.USER_BUNDLE_KEY, item.login)
                    }
                    findNavController().navigate(
                        R.id.action_searchOrganizationListFragment_to_searchPersonDetailFragment,
                        bundle
                    )
                }
            }
        }
    }

    private fun recyclerviewSetup() {
        binding.apply {
            rvOrganization.adapter = searchOrganizationListAdapter
            val layoutManager = rvOrganization.layoutManager as LinearLayoutManager
            ContextCompat.getDrawable(requireContext(), R.drawable.divider)?.let {
                val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
                itemDecorator.setDrawable(it)
                rvOrganization.addItemDecoration(itemDecorator)
            }

            rvOrganization.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                if (searchOrganizationListAdapter.list.size < total && page * pageSize < total && searchOrganizationListAdapter.list.size >= page * pageSize) {
                    page += 1
                    performOrganizationSearch()
                }
            }
        }
    }

    private fun performOrganizationSearch() {
        binding.apply {
            if (!searchText.isNullOrBlank()) {
                searchViewModel.searchUser(
                    searchText = searchText!!,
                    pageSize = pageSize,
                    page = page,
                    sort = sort.key,
                    order = order.key ?: "desc",
                    type = UserSearchTypeEnum.ORGANIZATION.type
                )
            }
        }
    }

    /**
     * Organization listesi boş ise empty text görünür olacak
     * liste dolu ise empty text gizlenecek
     * */
    private fun organizationListControl() {
        binding.apply {
            if (searchOrganizationListAdapter.list.isNullOrEmpty()) {
                tvNothingSee.show()
            } else {
                tvNothingSee.remove()
            }
        }
    }

    private fun subscribeObversers() {
        searchViewModel.apply {
            searchUserResponse.observeCall(requireActivity(),
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
                            searchOrganizationListAdapter.setList(result.items)
                        } else if (page > 1) {
                            /**
                             * Adaptör listesi temizlenmez. Sayfalama işlemi sonucudur
                             * Gelen veriler var olan listenin üzerine eklenir
                             * */
                            searchOrganizationListAdapter.addList(result.items)
                        }
                    }
                },
                finally = {
                    clearSearchUserResponse()
                    if (page == 1) {
                        (requireActivity() as? MainActivity)?.hideLoading()
                    }
                    organizationListControl()
                })
        }
    }

}