package com.sefatombul.gcase.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.sefatombul.gcase.R
import com.sefatombul.gcase.data.model.search.GetRepositoryResponseModel
import com.sefatombul.gcase.databinding.FragmentRepositoryDetailBinding
import com.sefatombul.gcase.databinding.FragmentSearchBinding
import com.sefatombul.gcase.databinding.FragmentSearchRepositoryDetailBinding
import com.sefatombul.gcase.databinding.FragmentSearchRepositoryListBinding
import com.sefatombul.gcase.ui.MainActivity
import com.sefatombul.gcase.utils.*
import com.sefatombul.gcase.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchRepositoryDetailFragment : Fragment() {
    var _binding: FragmentSearchRepositoryDetailBinding? = null
    val binding: FragmentSearchRepositoryDetailBinding get() = _binding!!
    val searchViewModel: SearchViewModel by viewModels()

    private var user: String? = null
    private var repoName: String? = null
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
        _binding = FragmentSearchRepositoryDetailBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as? MainActivity)?.hideBottomNavigation()
        subscribeObversers()
        getRepository()
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
                if (item.description == null) {
                    tvRepositoryDesc.remove()
                } else {
                    tvRepositoryDesc.text = item.description
                }
                if (item.homepage.isNullOrBlank()) {
                    clHomeUrl.remove()
                } else {
                    clHomeUrl.show()
                    tvUrl.text = item.homepage
                }

                tvStarCount.text = numberShortText(item.stargazersCount ?: 0)
                tvForkCount.text = numberShortText(item.forksCount ?: 0)
                if (item.fork == true) {
                    clFromFork.show()
                } else {
                    clFromFork.remove()
                }
                tvBranch.text = item.defaultBranch
                tvOpenIssuesCount.text = numberShortText(item.openIssuesCount ?: 0)
                tvWatcherCount.text = numberShortText(item.subscribersCount ?: 0)
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