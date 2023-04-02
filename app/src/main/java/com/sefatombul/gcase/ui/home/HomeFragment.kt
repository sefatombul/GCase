package com.sefatombul.gcase.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sefatombul.gcase.R
import com.sefatombul.gcase.data.model.RevokeAccessRequestModel
import com.sefatombul.gcase.databinding.FragmentHomeBinding
import com.sefatombul.gcase.databinding.FragmentLoginBinding
import com.sefatombul.gcase.ui.MainActivity
import com.sefatombul.gcase.utils.Constants
import com.sefatombul.gcase.utils.PreferencesRepository
import com.sefatombul.gcase.utils.observeCall
import com.sefatombul.gcase.utils.safeNavigate
import com.sefatombul.gcase.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {
    var _binding: FragmentHomeBinding? = null
    val binding: FragmentHomeBinding get() = _binding!!

    @Inject
    lateinit var preferencesRepository: PreferencesRepository
    val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as? MainActivity)?.showBottomNavigation()
        handleClickEventsListener()
        subscribeObservers()
    }

    private fun handleClickEventsListener() {
        binding.apply {
            ivSetting.setOnClickListener {
                (requireActivity() as? MainActivity)?.openSideMenu() {
                    authViewModel.revokeAccess(
                        RevokeAccessRequestModel(
                            preferencesRepository.getStringPreferences(
                                Constants.ACCESS_TOKEN
                            )
                        )
                    )
                }
            }

            clSearchRepositoryLayout.setOnClickListener {
                findNavController().safeNavigate(R.id.action_homeFragment_to_searchFragment)
            }

            clRepositoriesLayout.setOnClickListener {
                findNavController().safeNavigate(R.id.action_homeFragment_to_repositoryListFragment)
            }

            clSearchPersonLayout.setOnClickListener {
                findNavController().safeNavigate(R.id.action_homeFragment_to_personSearchFragment2)
            }

            clSearchOrganizationLayout.setOnClickListener {
                findNavController().safeNavigate(R.id.action_homeFragment_to_organizationSearchFragment)
            }

            clStarredRepositoriesLayout.setOnClickListener {
                findNavController().safeNavigate(R.id.action_homeFragment_to_starredFragment)
            }
        }
    }

    private fun subscribeObservers() {
        authViewModel.apply {
            revokeAccessResponse.observeCall(requireActivity(), viewLifecycleOwner, error = {
                /// todo : çıkıs basarısız ise ne olacak
            }, loading = {}, success = {
                preferencesRepository.deletePreferences(Constants.ACCESS_TOKEN)
                preferencesRepository.deletePreferences(Constants.REFRESH_TOKEN)
                findNavController().safeNavigate(R.id.action_homeFragment_to_loginFragment)
            }, finally = {
                clearRevokeAccessResponse()
            })
        }
    }
}