package com.sefatombul.gcase.ui.repository

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.sefatombul.gcase.R
import com.sefatombul.gcase.databinding.FragmentRepositoryDetailBinding
import com.sefatombul.gcase.databinding.FragmentRepositoryListBinding
import com.sefatombul.gcase.ui.MainActivity
import com.sefatombul.gcase.viewmodels.AuthViewModel
import com.sefatombul.gcase.viewmodels.RepositoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepositoryListFragment : Fragment() {
    var _binding: FragmentRepositoryListBinding? = null
    val binding: FragmentRepositoryListBinding get() = _binding!!

    val repositoryViewModel: RepositoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRepositoryListBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as? MainActivity)?.hideBottomNavigation()
    }
}