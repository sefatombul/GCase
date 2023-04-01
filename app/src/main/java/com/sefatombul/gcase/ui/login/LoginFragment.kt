package com.sefatombul.gcase.ui.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sefatombul.gcase.R
import com.sefatombul.gcase.data.model.AccessToken
import com.sefatombul.gcase.databinding.FragmentLoginBinding
import com.sefatombul.gcase.ui.MainActivity
import com.sefatombul.gcase.utils.Constants
import com.sefatombul.gcase.utils.Constants.AUTHORIZE_URL
import com.sefatombul.gcase.utils.PreferencesRepository
import com.sefatombul.gcase.utils.observeCall
import com.sefatombul.gcase.utils.safeNavigate
import com.sefatombul.gcase.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : Fragment() {
    var _binding: FragmentLoginBinding? = null
    val binding: FragmentLoginBinding get() = _binding!!

    /**
     * Kullanıcının oturum açması için Auth Url'e yönlendirme durumu tutulur
     * */
    var isRedirectAuthUrl: Boolean = false

    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as? MainActivity)?.hideBottomNavigation()
        handleClickEventsListener()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        authViewModel.apply {
            getAccessTokenResponse.observeCall(requireActivity(),
                viewLifecycleOwner,
                error = {},
                loading = {},
                success = { result ->
                    result?.let { accessToken ->
                        if (accessToken.error == null) {
                            preferencesRepository.setStringPreferences(
                                Constants.ACCESS_TOKEN, accessToken.accessToken
                            )
                            preferencesRepository.setStringPreferences(
                                Constants.REFRESH_TOKEN, accessToken.refreshToken
                            )

                            authViewModel.setAuthTokenClear()
                            navigateHome()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.login_failed),
                                Toast.LENGTH_SHORT
                            ).show()
                            //// TODO : AUT CODE SORUNLU
                        }
                    }
                })

            getAccessTokenWithRefreshTokenResponse.observeCall(
                requireActivity(),
                viewLifecycleOwner,
                error = {
                    navigateAuthUrl()
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
                                    navigateAuthUrl()
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
                                navigateAuthUrl()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            preferencesRepository.deletePreferences(Constants.REFRESH_TOKEN)
                            preferencesRepository.deletePreferences(Constants.ACCESS_TOKEN)
                            navigateAuthUrl()
                        }
                    } ?: run {
                        navigateAuthUrl()
                    }
                },
                finally = {
                    clearGetAccessTokenWithRefreshTokenResponse()
                }
            )

            getAuthTokenResponse.observeCall(
                requireActivity(),
                viewLifecycleOwner,
                error = {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.login_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                    /// TODO : Auth token almada sorun olusmussa
                },
                loading = {},
                success = { reponse ->
                    reponse?.let { body ->
                        val resString = body.string()
                        if (resString == "0") {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.login_failed),
                                Toast.LENGTH_SHORT
                            ).show()
                            /// TODO : Auth token almada sorun olusmussa
                        } else {
                            authViewModel.getAccessToken(resString)
                        }
                    } ?: run {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.login_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                        /// TODO : Auth token almada sorun olusmussa
                    }
                },
                finally = {
                    clearGetAuthTokenResponse()
                    isRedirectAuthUrl = false
                }
            )
        }
    }

    private fun navigateAuthUrl() {
        isRedirectAuthUrl = true
        val viewIntent = Intent("android.intent.action.VIEW", Uri.parse(AUTHORIZE_URL))
        startActivity(viewIntent)
    }

    private fun navigateHome() {
        findNavController().safeNavigate(R.id.action_loginFragment_to_homeFragment)
    }

    private fun handleClickEventsListener() {
        binding.apply {
            mcvLogin.setOnClickListener {
                val token = preferencesRepository.getStringPreferences(Constants.REFRESH_TOKEN)
                if (!token.isNullOrBlank()) {
                    authViewModel.getAccessTokenWithRefreshTokenResponse(token)
                }else {
                   navigateAuthUrl()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isRedirectAuthUrl) {
            authViewModel.getAuthToken()
        }
    }

}