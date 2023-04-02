package com.sefatombul.gcase.ui.repository

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sefatombul.gcase.R
import com.sefatombul.gcase.adapters.RepositoryListAdapter
import com.sefatombul.gcase.databinding.FragmentRepositoryListBinding
import com.sefatombul.gcase.ui.MainActivity
import com.sefatombul.gcase.utils.*
import com.sefatombul.gcase.viewmodels.RepositoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepositoryListFragment : Fragment() {
    var _binding: FragmentRepositoryListBinding? = null
    val binding: FragmentRepositoryListBinding get() = _binding!!

    var isPopBackStack = false
    val repositoryViewModel: RepositoryViewModel by viewModels()
    val repositoryListAdapter: RepositoryListAdapter by lazy { RepositoryListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        if (_binding == null) {
            _binding = FragmentRepositoryListBinding.inflate(
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
        if (!isPopBackStack) getUserRepository()
    }

    private fun handleClickEventsListener() {
        binding.apply {
            ivBack.setOnClickListener {
                findNavController().backStackCustom()
            }
            repositoryListAdapter.apply {
                setOnClickListener { item, position ->
                    val bundle = Bundle().apply {
                        putString(Constants.REPOSITORY_USER, item.owner?.login)
                        putString(Constants.REPOSITORY_NAME, item.name)
                    }
                    findNavController().navigate(
                        R.id.action_repositoryListFragment_to_searchRepositoryDetailFragment,
                        bundle
                    )
                }
            }
        }
    }
    private fun recyclerviewSetup() {
        binding.apply {
            rvRepositories.adapter = repositoryListAdapter
        }
    }

    private fun getUserRepository() {
        repositoryViewModel.userRepository()
    }

    /**
     * Repository listesi boş ise empty text görünür olacak
     * liste dolu ise empty text gizlenecek
     * */
    private fun repositoryListControl() {
        binding.apply {
            if (repositoryListAdapter.list.isNullOrEmpty()) {
                tvNothingSee.show()
            } else {
                tvNothingSee.remove()
            }
        }
    }

    private fun subscribeObversers() {
        repositoryViewModel.apply {
            userRepositoryResponse.observeCall(requireActivity(),
                viewLifecycleOwner,
                error = {},
                success = { response ->
                    response?.let { result ->
                        repositoryListAdapter.setList(result)
                    }
                },
                finally = {
                    clearUserRepositoryResponse()
                    repositoryListControl()
                })
        }
    }

}