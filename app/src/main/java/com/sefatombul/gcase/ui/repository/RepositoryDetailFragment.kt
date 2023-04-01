package com.sefatombul.gcase.ui.repository

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.sefatombul.gcase.R
import com.sefatombul.gcase.databinding.FragmentHomeBinding
import com.sefatombul.gcase.databinding.FragmentRepositoryDetailBinding
import com.sefatombul.gcase.ui.MainActivity
import com.sefatombul.gcase.utils.PreferencesRepository
import com.sefatombul.gcase.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RepositoryDetailFragment : Fragment() {
    var _binding: FragmentRepositoryDetailBinding? = null
    val binding: FragmentRepositoryDetailBinding get() = _binding!!

    val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRepositoryDetailBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as? MainActivity)?.hideBottomNavigation()
    }
}