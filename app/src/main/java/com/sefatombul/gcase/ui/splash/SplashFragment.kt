package com.sefatombul.gcase.ui.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sefatombul.gcase.R
import com.sefatombul.gcase.databinding.FragmentHomeBinding
import com.sefatombul.gcase.databinding.FragmentSplashBinding
import com.sefatombul.gcase.utils.*
import com.sefatombul.gcase.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SplashFragment : Fragment() {
    var _binding: FragmentSplashBinding? = null
    val binding: FragmentSplashBinding get() = _binding!!

    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    val authViewModel: AuthViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionControl()
        subscribeObervers()
    }

    private fun subscribeObervers() {
        authViewModel.apply {
            getAccessTokenWithRefreshTokenResponse.observeCall(
                requireActivity(),
                viewLifecycleOwner,
                error = {
                    navigateLogin()
                },
                isAutoShowLoading = false,
                success = { result ->
                    preferencesRepository.deletePreferences(Constants.REFRESH_TOKEN)
                    preferencesRepository.deletePreferences(Constants.ACCESS_TOKEN)
                    result?.let { text ->
                        try {
                            val split1 = text.split("&")
                            for (item in split1) {
                                val split2 = item.split("=")
                                if (!split2.isNullOrEmpty() && split2[0] == "error") {
                                    preferencesRepository.deletePreferences(Constants.REFRESH_TOKEN)
                                    preferencesRepository.deletePreferences(Constants.ACCESS_TOKEN)
                                    navigateLogin()
                                    break
                                } else {
                                    if (!split2.isNullOrEmpty() && split2[0] == "access_token") {
                                        preferencesRepository.setStringPreferences(
                                            Constants.ACCESS_TOKEN, split2[1]
                                        )
                                    } else if (!split2.isNullOrEmpty() && split2[0] == "refresh_token") {
                                        preferencesRepository.setStringPreferences(
                                            Constants.REFRESH_TOKEN, split2[1]
                                        )
                                    }
                                }
                            }
                            if (!preferencesRepository.getStringPreferences(Constants.REFRESH_TOKEN)
                                    .isNullOrBlank()
                                && !preferencesRepository.getStringPreferences(Constants.REFRESH_TOKEN)
                                    .isNullOrBlank()
                            ) {
                                navigateHome()
                            } else {
                                navigateLogin()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            preferencesRepository.deletePreferences(Constants.REFRESH_TOKEN)
                            preferencesRepository.deletePreferences(Constants.ACCESS_TOKEN)
                            navigateLogin()
                        }
                    } ?: run {
                        navigateLogin()
                    }
                },
                finally = {
                    clearGetAccessTokenWithRefreshTokenResponse()
                }
            )
        }
    }

    private fun navigateHome() {
        handlerPostDelay(requireContext(), 1000L) {
            findNavController().safeNavigate(R.id.action_splashFragment_to_homeFragment)
        }
    }

    private fun sessionControl() {
        val refreshToken = preferencesRepository.getStringPreferences(Constants.REFRESH_TOKEN)
        if (!refreshToken.isNullOrBlank()) {
            authViewModel.getAccessTokenWithRefreshTokenResponse(refreshToken)
        } else {
            navigateLogin()
        }
    }

    private fun navigateLogin() {
        handlerPostDelay(requireContext(), 1000L) {
            findNavController().safeNavigate(R.id.action_splashFragment_to_loginFragment)
        }
    }
}