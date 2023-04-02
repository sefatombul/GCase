package com.sefatombul.gcase.adapters.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sefatombul.gcase.data.model.search.UserResponseItems
import com.sefatombul.gcase.databinding.LayoutItemRowSearchPersonListBinding
import com.sefatombul.gcase.utils.loadImage

class SearchPersonListAdapter : RecyclerView.Adapter<SearchPersonListAdapter.ViewHolder>() {
    private var _list = ArrayList<UserResponseItems>()
    val list: List<UserResponseItems> get() = _list
    lateinit var context: Context

    fun setList(tempList: List<UserResponseItems>) {
        _list = ArrayList(tempList)
        notifyDataSetChanged()
    }

    fun addList(tempList: List<UserResponseItems>) {
        tempList.forEach {
            _list.add(it)
        }
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: LayoutItemRowSearchPersonListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            LayoutItemRowSearchPersonListBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val currentItem = list[position]
            ivProfile.loadImage(
                currentItem.avatarUrl
            )
            tv.text = currentItem.login

            root.setOnClickListener {
                onClickListener?.let { f ->
                    f(currentItem, position)
                }
            }
        }
    }

    override fun getItemCount() = list.size

    private var onClickListener: ((item: UserResponseItems, position: Int) -> Unit)? = null
    fun setOnClickListener(listener: ((item: UserResponseItems, position: Int) -> Unit)) {
        onClickListener = listener
    }


}