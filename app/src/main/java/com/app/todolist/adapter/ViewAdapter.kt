package com.app.todolist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.todolist.R
import com.app.todolist.databinding.AdapterViewListBinding
import com.app.todolist.model.ItemModel
import com.chauthai.swipereveallayout.ViewBinderHelper

class ViewAdapter(var mList: ArrayList<ItemModel>) : RecyclerView.Adapter<ViewAdapter.ItemViewHolder>() {

    lateinit var binding: AdapterViewListBinding
    lateinit var mListener: IItemListener
    var viewBindHelper: ViewBinderHelper = ViewBinderHelper()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ItemViewHolder {
        viewBindHelper.setOpenOnlyOne(true)
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_view_list, parent, false)
        return ItemViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        viewBindHelper.bind(holder.binding.srLayout, mList[position].itemId.toString())
        holder.binding.model = mList[position]
        holder.binding.executePendingBindings()

        holder.binding.rlEdit.setOnClickListener {
            holder.binding!!.srLayout.close(true)
            mListener.onClickEdit(position)
        }
        holder.binding.rlDelete.setOnClickListener {
            holder.binding!!.srLayout.close(true)
            mListener.onClickDelete(position)
        }

    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: AdapterViewListBinding = DataBindingUtil.bind(view)!!
    }

    fun setMyListener(listener: IItemListener) {
        mListener = listener
    }

    interface IItemListener {
        fun onClickEdit(position: Int)
        fun onClickDelete(position: Int)
    }
}