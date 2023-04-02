package com.sefatombul.gcase.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sefatombul.gcase.data.model.search.Items
import com.sefatombul.gcase.databinding.LayoutItemRowRepositoryListBinding
import com.sefatombul.gcase.utils.loadImage

class RepositoryListAdapter : RecyclerView.Adapter<RepositoryListAdapter.ViewHolder>() {
    private var _list = ArrayList<Items>()
    val list: List<Items> get() = _list
    lateinit var context: Context

    fun setList(tempList: List<Items>) {
        _list = ArrayList(tempList)
        notifyDataSetChanged()
    }

    fun addList(tempList: List<Items>) {
        tempList.forEach {
            _list.add(it)
        }
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: LayoutItemRowRepositoryListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            LayoutItemRowRepositoryListBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val currentItem = list[position]
            ivProfile.loadImage(
                currentItem.owner?.avatarUrl
            )
            tvUser.text = currentItem.owner?.login
            tvRepository.text = currentItem.name

            root.setOnClickListener {
                onClickListener?.let { f ->
                    f(currentItem, position)
                }
            }
        }
    }

    override fun getItemCount() = list.size

    private var onClickListener: ((item: Items, position: Int) -> Unit)? = null
    fun setOnClickListener(listener: ((item: Items, position: Int) -> Unit)) {
        onClickListener = listener
    }


}