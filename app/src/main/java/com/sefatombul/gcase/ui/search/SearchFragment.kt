package com.sefatombul.gcase.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.sefatombul.gcase.R
import com.sefatombul.gcase.databinding.FragmentRepositoryListBinding
import com.sefatombul.gcase.databinding.FragmentSearchBinding
import com.sefatombul.gcase.ui.MainActivity
import com.sefatombul.gcase.utils.remove
import com.sefatombul.gcase.utils.show
import com.sefatombul.gcase.viewmodels.RepositoryViewModel

class SearchFragment : Fragment() {
    var _binding: FragmentSearchBinding? = null
    val binding: FragmentSearchBinding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as? MainActivity)?.hideBottomNavigation()
        edittextSetup()
    }

    private fun edittextSetup() {
        binding.apply {
            etSearch.addTextChangedListener { text ->
                if (etSearch.text.isNotEmpty()) {
                    ivClear.show()
                } else {
                    ivClear.remove()
                }
            }
        }
    }
}