package com.sefatombul.gcase.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.sefatombul.gcase.R
import com.sefatombul.gcase.data.model.ProfileResponseModel
import com.sefatombul.gcase.databinding.FragmentProfileBinding
import com.sefatombul.gcase.databinding.FragmentSplashBinding
import com.sefatombul.gcase.utils.loadImage
import com.sefatombul.gcase.utils.numberShortText
import com.sefatombul.gcase.utils.observeCall
import com.sefatombul.gcase.utils.remove
import com.sefatombul.gcase.viewmodels.AuthViewModel
import com.sefatombul.gcase.viewmodels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    val binding: FragmentProfileBinding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModels()
    private var model: ProfileResponseModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as? MainActivity)?.showBottomNavigation()
        subscribeObvers()
        getProfileData()
    }

    private fun populateUI() {
        binding.apply {
            model?.let { person ->
                ivProfile.loadImage(person.avatarUrl)
                tvFullName.text = person.name
                tvUserName.text = person.login
                if (person.bio.isNullOrBlank()) {
                    tvPersonDesc.remove()
                } else {
                    tvPersonDesc.text = person.bio
                }
                if (person.company.isNullOrBlank()) {
                    clCompany.remove()
                } else {
                    tvCompany.text = person.company
                    tvLocation.text = person.location
                }
                if (person.blog.isNullOrBlank()) {
                    clBlogUrl.remove()
                } else {
                    tvBlogUrl.text = person.blog
                }

                if (person.email.isNullOrBlank()) {
                    clEmail.remove()
                } else {
                    tvEmail.text = person.email
                }

                if (person.twitterUsername.isNullOrBlank()) {
                    clTwitter.remove()
                } else {
                    tvTwitter.text = person.twitterUsername
                }

                tvFollowerCount.text = numberShortText(person.followers ?: 0)
                tvFollowingCount.text = numberShortText(person.following ?: 0)
            }
        }
    }

    private fun getProfileData() {
        profileViewModel.getUserProfile()
    }

    private fun subscribeObvers() {
        profileViewModel.apply {
            getUserProfileResponse.observeCall(
                requireActivity(),
                viewLifecycleOwner,
                error = {},
                loading = {},
                success = { response ->
                    model = response
                    populateUI()
                },
                finally = {
                    clearGetUserProfileResponse()
                },
            )
        }
    }

}