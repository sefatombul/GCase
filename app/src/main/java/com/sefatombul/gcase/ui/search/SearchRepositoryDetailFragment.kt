package com.sefatombul.gcase.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sefatombul.gcase.R
import com.sefatombul.gcase.databinding.FragmentRepositoryDetailBinding
import com.sefatombul.gcase.databinding.FragmentSearchBinding
import com.sefatombul.gcase.ui.MainActivity


class SearchRepositoryDetailFragment : Fragment() {
    var _binding: FragmentRepositoryDetailBinding? = null
    val binding: FragmentRepositoryDetailBinding get() = _binding!!


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