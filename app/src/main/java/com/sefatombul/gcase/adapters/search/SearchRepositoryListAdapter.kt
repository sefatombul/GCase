package com.sefatombul.gcase.adapters.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sefatombul.gcase.data.model.search.Items
import com.sefatombul.gcase.databinding.LayoutItemRowSearchRepositoryListBinding
import com.sefatombul.gcase.utils.loadImage
import com.sefatombul.gcase.utils.numberShortText

class SearchRepositoryListAdapter : RecyclerView.Adapter<SearchRepositoryListAdapter.ViewHolder>() {
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

    class ViewHolder(val binding: LayoutItemRowSearchRepositoryListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            LayoutItemRowSearchRepositoryListBinding.inflate(
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
            tvUserName.text = currentItem.owner?.login
            tvRepositoryName.text = currentItem.name
            tvRepositoryDesc.text = currentItem.description
            tvStar.text = numberShortText(currentItem.stargazersCount ?: 0)
            tvLanguage.text = currentItem.language

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