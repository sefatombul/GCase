package com.sefatombul.gcase.adapters.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sefatombul.gcase.R
import com.sefatombul.gcase.data.model.search.Sort
import com.sefatombul.gcase.databinding.LayoutItemRowSortListBinding

class SortListAdapter : RecyclerView.Adapter<SortListAdapter.ViewHolder>() {
    private var _list = ArrayList<Sort>()
    val list: List<Sort> get() = _list
    lateinit var context: Context

    fun setList(tempList: List<Sort>) {
        _list = ArrayList(tempList)
        notifyDataSetChanged()
    }

    private var _selectedItem: Sort? = null
    val selectedItem get() = _selectedItem

    class ViewHolder(val binding: LayoutItemRowSortListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            LayoutItemRowSortListBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val currentItem = list[position]
            tv.text = currentItem.text
            if (currentItem.isChecked) {
                iv.setImageDrawable(
                    ContextCompat.getDrawable(
                        context, R.drawable.background_checked_radio_button
                    )
                )
            } else {
                iv.setImageDrawable(
                    ContextCompat.getDrawable(
                        context, R.drawable.background_unchecked_radio_button
                    )
                )
            }

            root.setOnClickListener {
                _list.forEach {
                    it.isChecked = false
                }
                _list[position].isChecked = true
                _selectedItem = _list[position]
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount() = list.size

}