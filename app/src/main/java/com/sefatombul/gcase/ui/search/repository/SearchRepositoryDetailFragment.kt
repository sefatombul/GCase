package com.sefatombul.gcase.ui.search.repository

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sefatombul.gcase.R
import com.sefatombul.gcase.data.model.search.GetRepositoryResponseModel
import com.sefatombul.gcase.databinding.FragmentSearchRepositoryDetailBinding
import com.sefatombul.gcase.ui.MainActivity
import com.sefatombul.gcase.utils.*
import com.sefatombul.gcase.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchRepositoryDetailFragment : Fragment() {
    var _binding: FragmentSearchRepositoryDetailBinding? = null
    val binding: FragmentSearchRepositoryDetailBinding get() = _binding!!
    val searchViewModel: SearchViewModel by viewModels()

    var isPopBackStack = false

    /**
     * Detay bilgisi alınmak istenen repositorinin hangi kullanıcıya ait olduğu bilgisi
     * */
    private var user: String? = null

    /**
     * Detay bilgisi alınmak istenen repositorinin ismi
     * */
    private var repoName: String? = null

    /**
     * Repository detayı için tutulan obje
     * */
    private var model: GetRepositoryResponseModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleArguments()
    }

    private fun handleArguments() {
        arguments?.let {
            user = it.getString(Constants.REPOSITORY_USER)
            repoName = it.getString(Constants.REPOSITORY_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        if (_binding == null) {
            _binding = FragmentSearchRepositoryDetailBinding.inflate(
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
        subscribeObversers()
        handleClickEventsListener()
        if (!isPopBackStack) getRepository()
    }

    private fun handleClickEventsListener() {
        binding.apply {
            ivBack.setOnClickListener {
                findNavController().backStackCustom()
            }

            clProfile.setOnClickListener {
                val bundle = Bundle().apply {
                    putString(Constants.USER_BUNDLE_KEY, model?.owner?.login)
                }
                findNavController().navigate(
                    R.id.action_searchRepositoryDetailFragment_to_searchPersonDetailFragment, bundle
                )
            }
        }
    }


    private fun subscribeObversers() {
        searchViewModel.apply {
            getRepositoryResponse.observeCall(requireActivity(),
                viewLifecycleOwner,
                error = {},
                success = { response ->
                    response?.let { result ->
                        model = result
                        populateUI()
                    }
                },
                finally = {
                    clearSearchRepositoryResponse()
                })
        }
    }

    private fun populateUI() {
        binding.apply {
            model?.let { item ->
                ivProfile.loadImage(
                    item.owner?.avatarUrl
                )
                tvUserName.text = item.owner?.login
                tvRepositoryName.text = item.name
                tvStarCount.text = numberShortText(item.stargazersCount ?: 0)
                tvForkCount.text = numberShortText(item.forksCount ?: 0)
                tvBranch.text = item.defaultBranch
                tvOpenIssuesCount.text = numberShortText(item.openIssuesCount ?: 0)
                tvWatcherCount.text = numberShortText(item.subscribersCount ?: 0)

                /**
                 * Description bilgisi null ise tasarımda ayrılmıs olan alan gizlenir.
                 * Description bilgisi null değil ise bilgi ilgili alana yazılır
                 * */
                if (item.description == null) {
                    tvRepositoryDesc.remove()
                } else {
                    tvRepositoryDesc.text = item.description
                }
                /**
                 * Homepage url bilgisi null ise tasarımda ayrılmıs olan alan gizlenir.
                 * Homepage url bilgisi null değil ise bilgi ilgili alana yazılır
                 * */
                if (item.homepage.isNullOrBlank()) {
                    clHomeUrl.remove()
                } else {
                    clHomeUrl.show()
                    tvUrl.text = item.homepage
                }
                if (item.fork == true) {
                    clFromFork.show()
                } else {
                    clFromFork.remove()
                }
                if (item.license != null) {
                    tvLicenseCount.text = item.license?.spdxId
                } else {
                    clLicenseLayout.remove()
                }
            }
        }
    }

    private fun getRepository() {
        binding.apply {
            if (!user.isNullOrBlank() && !repoName.isNullOrBlank()) {
                searchViewModel.getRepository(
                    user!!, repoName!!
                )
            }
        }
    }

}