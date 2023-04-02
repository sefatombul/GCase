package com.sefatombul.gcase.ui.search.organization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sefatombul.gcase.R
import com.sefatombul.gcase.adapters.RecentSearchListAdapter
import com.sefatombul.gcase.data.local.RecentSearchModel
import com.sefatombul.gcase.data.local.RecentSearchType
import com.sefatombul.gcase.databinding.FragmentOrganizationSearchBinding
import com.sefatombul.gcase.ui.MainActivity
import com.sefatombul.gcase.utils.*
import com.sefatombul.gcase.viewmodels.RecentSearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrganizationSearchFragment : Fragment() {
    var _binding: FragmentOrganizationSearchBinding? = null
    val binding: FragmentOrganizationSearchBinding get() = _binding!!

    /**
     * Araması yapılan text tutulur
     * */
    private var searchText: String? = null

    private val recentSearchListAdapter: RecentSearchListAdapter by lazy { RecentSearchListAdapter() }
    val recentSearchViewModel: RecentSearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrganizationSearchBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as? MainActivity)?.hideBottomNavigation()
        edittextSetup()
        setupRecyclerview()
        handleCLickEventsListener()
        subscribeObservers()
        getRecentSearchList()

    }

    /**
     * Son arananlar listesi için veriler local db'den alındı.
     * @author Sefa TOMBUL
     * @since 02/04/2023
     * @param limit En son arananlar listesinde kaç kelime listelenecek
     **/
    private fun getRecentSearchList() {
        recentSearchViewModel.getRecentSearchLocal(5, RecentSearchType.ORGANIZATION.type)
    }

    private fun subscribeObservers() {
        binding.apply {
            recentSearchViewModel.apply {
                deleteRecentSearchWithWordTextResponse.observeCall(requireActivity(),
                    viewLifecycleOwner,
                    error = {},
                    loading = {},
                    success = {},
                    finally = {
                        searchText?.let { st ->
                            /**
                             * Veritabanında aranan kelime varsa başarılı şekilde silindiğinde
                             * Veya bir silme işlemi gerçekleşmesi gerekmediğinde bu adımda
                             * Olağan dışı şekilde silme işleminin hata dönmesi durumunda
                             * Aranan kelime detay ekranına geçmeden önce veritabanına eklenir.
                             * Bunun amacı programın search işlemine devam etmesi isteniyor
                             * Son aramalarım programın ana akısını bozmamalı
                             * */
                            recentSearchViewModel.insertRecentSearch(
                                RecentSearchModel(0, st, RecentSearchType.ORGANIZATION.type)
                            )
                        }
                        clearDeleteRecentSearchWithWordTextResponse()
                    })

                deleteAllTextResponse.observeCall(requireActivity(),
                    viewLifecycleOwner,
                    error = {},
                    loading = {},
                    success = { response ->
                        response?.let {
                            getRecentSearchList()
                        }
                    },
                    finally = {
                        clearDeleteAllTextResponseResponse()
                    })

                insertRecentSearchResponse.observeCall(requireActivity(),
                    viewLifecycleOwner,
                    error = {},
                    loading = {},
                    success = { },
                    finally = {
                        /**
                         * Aranan kelime veritabanına eklendikten sonra veya
                         * ekleme işlemi başarısız bile olsa detay ekranına yönlendirme sağlanır.
                         **/
                        searchText?.let { it1 -> navigateSearchOrganizationListFragmenment(it1) }
                        clearInsertRecentSearchResponse()
                    })

                getRecentSearchLocalResponse.observeCall(requireActivity(),
                    viewLifecycleOwner,
                    error = {
                        /**
                         * Bir hata ile karşılaşıldığında recentSearch listesi boş olarak güncellenir.
                         * */
                        recentSearchListAdapter.set(arrayListOf())
                    },
                    loading = {},
                    success = { response ->
                        /**
                         * recentSearch listesi güncellenir.
                         * */
                        response?.let { list ->
                            recentSearchListAdapter.set(arrayListOf(*list.toTypedArray()))
                        } ?: run {
                            recentSearchListAdapter.set(arrayListOf())
                        }
                    },
                    finally = {
                        clearGetRecentSearchLocalResponse()
                        recentSearchListControl()
                    })
            }
        }
    }

    /**
     * Eğer son arananlar listesi dolu liste liste gösterilecek, clear alanı gösterilecek
     * Eğer liste boş ise empty tasarımı gösterilecek, liste ve clear alanı gizlenecek
     * */
    private fun recentSearchListControl() {
        binding.apply {
            if (recentSearchListAdapter.list.isNotEmpty()) {
                clRecentSearch.show()
                rvRecentSearch.show()
                clEmptyList.remove()
            } else {
                clRecentSearch.remove()
                rvRecentSearch.remove()
                clEmptyList.show()
            }
        }
    }

    private fun setupRecyclerview() {
        binding.apply {
            rvRecentSearch.adapter = recentSearchListAdapter
        }
    }


    private fun handleCLickEventsListener() {
        binding.apply {
            ivClear.setOnClickListener {
                etSearch.setText("")
                searchText = null
            }

            ivBack.setOnClickListener {
                binding.etSearch.hideKeyboard()
                findNavController().backStackCustom()
            }

            tvClear.setOnClickListener {
                recentSearchViewModel.deleteAllText(RecentSearchType.ORGANIZATION.type)
            }
        }

        recentSearchListAdapter.setOnClickListener { item, position ->
            item.word?.let {
                handleRecentSearch(it)
            }
        }
    }

    private fun handleRecentSearch(text: String) {
        if (!text.isNullOrBlank()) {
            searchText = text
            binding.etSearch.hideKeyboard()
            /**
             * Daha önce aynı kelime arandığından dolayı veritabanını verimli kullanmak için aranan kelime db'den silinir.
             * */
            recentSearchViewModel.deleteRecentSearchWithWordText(
                text, RecentSearchType.ORGANIZATION.type
            )
        }
    }

    private fun edittextSetup() {
        binding.apply {
            etSearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    handleRecentSearch(etSearch.text.toString())
                    return@OnEditorActionListener true
                }
                false
            })

            etSearch.addTextChangedListener {
                if (etSearch.text.isNotEmpty()) {
                    ivClear.show()
                } else {
                    ivClear.remove()
                }
            }

            searchText = null
            etSearch.setText("")
            etSearch.requestFocus()
            etSearch.showKeyboard()
        }
    }

    private fun navigateSearchOrganizationListFragmenment(text: String) {
        val bundle = Bundle().apply {
            putString(Constants.SEARCH_KEY_BUNDLE, text)
        }
        findNavController().safeNavigate(
            R.id.action_organizationSearchFragment_to_searchOrganizationListFragment, bundle
        )
    }
}