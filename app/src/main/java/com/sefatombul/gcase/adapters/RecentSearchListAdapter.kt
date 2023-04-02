package com.sefatombul.gcase.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sefatombul.gcase.data.local.RecentSearchModel
import com.sefatombul.gcase.databinding.LayoutItemRowRecentSearchListBinding


class RecentSearchListAdapter : RecyclerView.Adapter<RecentSearchListAdapter.ViewHolder>() {

    private var _list = arrayListOf<RecentSearchModel>()
    val list: List<RecentSearchModel> get() = _list
    fun set(tempList: ArrayList<RecentSearchModel>) {
        _list = ArrayList(tempList)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: LayoutItemRowRecentSearchListBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutItemRowRecentSearchListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        holder.binding.apply {
            tv.text = currentItem.word
            root.setOnClickListener {
                onClickListener?.let {
                    it(currentItem, position)
                }
            }
        }
    }

    override fun getItemCount() = list.size

    private var onClickListener: ((item: RecentSearchModel, position: Int) -> Unit)? = null
    fun setOnClickListener(listener: (item: RecentSearchModel, position: Int) -> Unit) {
        onClickListener = listener
    }
}