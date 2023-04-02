package com.sefatombul.gcase.ui.search

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sefatombul.gcase.R
import com.sefatombul.gcase.adapters.search.SortListAdapter
import com.sefatombul.gcase.data.model.search.Sort
import com.sefatombul.gcase.databinding.FragmentDateRangeBottomSheetBinding
import com.sefatombul.gcase.databinding.FragmentSortBottomSheetBinding
import com.sefatombul.gcase.ui.MainActivity
import com.sefatombul.gcase.utils.remove
import com.sefatombul.gcase.utils.show

class DateRangeBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentDateRangeBottomSheetBinding? = null
    val binding: FragmentDateRangeBottomSheetBinding get() = _binding!!

    private val dateRangeAdapter: SortListAdapter by lazy { SortListAdapter() }
    private var dateRangeList = ArrayList<Sort>()
    private var listener: ((dateRangeSelected: Sort?) -> Unit)? = null

    fun setDateRangeList(tempList: ArrayList<Sort>) {
        dateRangeList = ArrayList(tempList)
    }

    override fun getTheme() = R.style.BottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDateRangeBottomSheetBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as? MainActivity)?.hideBottomNavigation()
        setupRecyclerviews()
        handleClickEventsListener()
    }

    private fun handleClickEventsListener() {
        binding.apply {
            mcvApply.setOnClickListener {
                listener?.let { f ->
                    f(dateRangeAdapter.selectedItem)
                }
                dismiss()
            }
        }
    }

    fun applySetOnClickListener(f: (dateRangeSelected: Sort?) -> Unit) {
        listener = f
    }

    private fun setupRecyclerviews() {
        binding.apply {
            rvDateRange.adapter = dateRangeAdapter
        }
        dateRangeAdapter.setList(dateRangeList)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { bottomDialog ->
            val bottomSheetDialog = bottomDialog as BottomSheetDialog
            val bottomSheet =
                (bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?)!!
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog
    }

}