package com.sefatombul.gcase.ui.search.sort

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sefatombul.gcase.R
import com.sefatombul.gcase.adapters.search.SortListAdapter
import com.sefatombul.gcase.data.model.search.Sort
import com.sefatombul.gcase.databinding.FragmentRepositoryListBinding
import com.sefatombul.gcase.databinding.FragmentSortBottomSheetBinding
import com.sefatombul.gcase.ui.MainActivity
import com.sefatombul.gcase.utils.remove
import com.sefatombul.gcase.utils.show
import com.sefatombul.gcase.viewmodels.RepositoryViewModel

class SortBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentSortBottomSheetBinding? = null
    val binding: FragmentSortBottomSheetBinding get() = _binding!!

    private val sortAdapter: SortListAdapter by lazy { SortListAdapter() }
    private val orderAdapter: SortListAdapter by lazy { SortListAdapter() }

    private var sortList = ArrayList<Sort>()
    private var orderList = ArrayList<Sort>()
    private var listener: ((sortSelected: Sort?, orderSelected: Sort?) -> Unit)? = null

    fun setSortList(tempList: ArrayList<Sort>) {
        sortList = ArrayList(tempList)
    }

    fun setOrderList(tempList: ArrayList<Sort>) {
        orderList = ArrayList(tempList)
    }

    override fun getTheme() = R.style.BottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSortBottomSheetBinding.inflate(
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
                    f(sortAdapter.selectedItem, orderAdapter.selectedItem)
                }
            }
        }
    }

    fun applySetOnClickListener(f: (sortSelected: Sort?, orderSelected: Sort?) -> Unit) {
        listener = f
    }

    private fun setupRecyclerviews() {
        binding.apply {
            rvSort.adapter = sortAdapter
            rvOrder.adapter = orderAdapter
        }
        sortAdapter.setList(sortList)
        orderAdapter.setList(orderList)
        binding.apply {
            if (orderList.isNullOrEmpty()) {
                rvOrder.remove()
                clOrderSelection.remove()
            } else {
                clOrderSelection.show()
                rvOrder.show()
            }
        }
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