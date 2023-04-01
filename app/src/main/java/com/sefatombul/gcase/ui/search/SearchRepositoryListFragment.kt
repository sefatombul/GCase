package com.sefatombul.gcase.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.sefatombul.gcase.R
import com.sefatombul.gcase.databinding.FragmentSearchBinding
import com.sefatombul.gcase.databinding.FragmentSearchRepositoryListBinding
import com.sefatombul.gcase.ui.MainActivity
import com.sefatombul.gcase.viewmodels.RepositoryViewModel

class SearchRepositoryListFragment : Fragment() {
    var _binding: FragmentSearchRepositoryListBinding? = null
    val binding: FragmentSearchRepositoryListBinding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchRepositoryListBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as? MainActivity)?.hideBottomNavigation()
    }
}