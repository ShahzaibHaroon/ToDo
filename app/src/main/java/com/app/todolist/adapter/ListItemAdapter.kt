package com.app.todolist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.todolist.R
import com.app.todolist.databinding.AdapterListItemBinding
import com.app.todolist.model.ItemModel
import com.chauthai.swipereveallayout.ViewBinderHelper

class ListItemAdapter(var mList: ArrayList<ItemModel>) : RecyclerView.Adapter<ListItemAdapter.ItemViewHolder>() {

    lateinit var binding: AdapterListItemBinding
    lateinit var mListener: IItemListener
    var viewBindHelper: ViewBinderHelper = ViewBinderHelper()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ItemViewHolder {
        viewBindHelper.setOpenOnlyOne(true)
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_list_item, parent, false)
        return ItemViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        viewBindHelper.bind(holder.binding!!.srLayout, mList[position].itemId.toString())
        holder.binding.model = mList[position]
        holder.binding.executePendingBindings()

        holder.binding.rlDone.setOnClickListener {
            holder.binding.srLayout.close(true)
            mListener.onClickDone(position)
        }
        holder.binding.rlDelete.setOnClickListener {
            holder.binding!!.srLayout.close(true)
            mListener.onClickDelete(position)
        }

    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: AdapterListItemBinding = DataBindingUtil.bind(view)!!
    }

    fun setMyListener(listener: IItemListener) {
        mListener = listener
    }

    interface IItemListener {
        fun onClickDone(position: Int)
        fun onClickDelete(position: Int)
    }
}