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
        handleClickEventsListener()
        subscribeObservers()

//        val token = preferencesRepository.getStringPreferences(Constants.REFRESH_TOKEN)
//        if (!token.isNullOrBlank()) {
//            authViewModel.getAccessTokenWithRefreshTokenResponse(token)
//        }
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
                            findNavController().safeNavigate(R.id.action_loginFragment_to_homeFragment)
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

    private fun handleClickEventsListener() {
        binding.apply {
            mcvLogin.setOnClickListener {
                isRedirectAuthUrl = true
                val viewIntent = Intent("android.intent.action.VIEW", Uri.parse(AUTHORIZE_URL))
                startActivity(viewIntent)
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