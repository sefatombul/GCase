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
import com.sefatombul.gcase.data.model.AccessToken
import com.sefatombul.gcase.databinding.FragmentLoginBinding
import com.sefatombul.gcase.utils.Constants.AUTHORIZE_URL
import com.sefatombul.gcase.utils.observeCall
import com.sefatombul.gcase.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment() {
    var _binding: FragmentLoginBinding? = null
    val binding: FragmentLoginBinding get() = _binding!!

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
        authViewModel.getAccessToken("e7066a77d70fbdcfbdfe")
    }

    private fun subscribeObservers() {
        authViewModel.apply {
            getAccessTokenResponse.observeCall(viewLifecycleOwner,
                error = {},
                loading = {},
                success = { result ->
                    result?.let { accessToken ->

                    }
                }
            )
        }
    }

    private fun handleClickEventsListener() {
        binding.apply {
            mcvLogin.setOnClickListener {
                val viewIntent =
                    Intent("android.intent.action.VIEW", Uri.parse(AUTHORIZE_URL))
                startActivity(viewIntent)
            }
        }
    }

}