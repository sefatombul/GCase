package com.sefatombul.gcase.ui.search.person

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sefatombul.gcase.data.model.search.GetUserResponseModel
import com.sefatombul.gcase.databinding.FragmentSearchPersonDetailBinding
import com.sefatombul.gcase.ui.MainActivity
import com.sefatombul.gcase.utils.*
import com.sefatombul.gcase.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchPersonDetailFragment : Fragment() {
    var _binding: FragmentSearchPersonDetailBinding? = null
    val binding: FragmentSearchPersonDetailBinding get() = _binding!!
    val searchViewModel: SearchViewModel by viewModels()

    var isPopBackStack = false

    /**
     * Detay bilgisi alınmak istenen kullanıcı
     * */
    private var user: String? = null

    /**
     * Person detayı için tutulan obje
     * */
    private var model: GetUserResponseModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleArguments()
    }

    private fun handleArguments() {
        arguments?.let {
            user = it.getString(Constants.USER_BUNDLE_KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        if (_binding == null) {
            _binding = FragmentSearchPersonDetailBinding.inflate(
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
        if (!isPopBackStack) getPerson()

    }

    private fun handleClickEventsListener() {
        binding.apply {
            ivBack.setOnClickListener {
                findNavController().backStackCustom()
            }
        }
    }

    private fun subscribeObversers() {
        searchViewModel.apply {
            getUserResponse.observeCall(requireActivity(),
                viewLifecycleOwner,
                error = {},
                success = { response ->
                    response?.let { result ->
                        model = result
                        populateUI()
                    }
                },
                finally = {
                    clearGetUserResponse()
                })
        }
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

    private fun getPerson() {
        binding.apply {
            if (!user.isNullOrBlank()) {
                searchViewModel.getUser(
                    user!!
                )
            }
        }
    }

}