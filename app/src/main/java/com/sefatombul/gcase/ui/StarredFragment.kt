package com.sefatombul.gcase.ui

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
import com.sefatombul.gcase.databinding.FragmentStarredBinding
import com.sefatombul.gcase.utils.*
import com.sefatombul.gcase.viewmodels.RepositoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StarredFragment : Fragment() {
    var _binding: FragmentStarredBinding? = null
    val binding: FragmentStarredBinding get() = _binding!!

    /**
     * Search Repository List ekranı ilk defa acıldıysa false değerini alır.
     * Bir sonraki fragmentta geri gelinme işlemi yapıldıysa true değerini alır
     * */
    var isPopBackStack = false

    val repositoryViewModel: RepositoryViewModel by viewModels()

    /**
     * Her sayfalama için gösterilecek item sayısı
     * */
    private val pageSize = 30

    /**
     * Gösterilen itemların hangi sayfa dilimine ait olduğu
     * */
    private var page = 1

    val searchRepositoryListAdapter: SearchRepositoryListAdapter by lazy { SearchRepositoryListAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        if (_binding == null) {
            _binding = FragmentStarredBinding.inflate(
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
        if (!isPopBackStack) performRepository()
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
                        R.id.action_starredFragment_to_searchRepositoryDetailFragment,
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
                if (searchRepositoryListAdapter.list.size >= page * pageSize) {
                    page += 1
                    performRepository()
                }
            }
        }
    }

    private fun performRepository() {
        repositoryViewModel.getStarredRepositories(
            pageSize = pageSize,
            page = page,
        )
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
        repositoryViewModel.apply {
            getStarredRepositoriesResponse.observeCall(requireActivity(),
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
                        if (page == 1) {
                            /**
                             * Adaptör listesi temizlenir gelen veriler temiz liste üzerine verilir
                             * */
                            searchRepositoryListAdapter.setList(result)
                        } else if (page > 1) {
                            /**
                             * Adaptör listesi temizlenmez. Sayfalama işlemi sonucudur
                             * Gelen veriler var olan listenin üzerine eklenir
                             * */
                            searchRepositoryListAdapter.addList(result)
                        }
                    }
                },
                finally = {
                    clearGetStarredRepositoriesResponse()
                    if (page == 1) {
                        (requireActivity() as? MainActivity)?.hideLoading()
                    }
                    repositoryListControl()
                })
        }
    }

}