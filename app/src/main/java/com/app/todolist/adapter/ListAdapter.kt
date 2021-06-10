package com.app.todolist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.todolist.R
import com.app.todolist.databinding.AdapterMainListBinding
import com.app.todolist.model.ListModel
import com.chauthai.swipereveallayout.ViewBinderHelper

class ListAdapter(private var mList: ArrayList<ListModel>) : RecyclerView.Adapter<ListAdapter.ItemViewHolder>() {

    lateinit var binding: AdapterMainListBinding
    lateinit var mListener: IListingListener
    var viewBindHelper: ViewBinderHelper = ViewBinderHelper()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ItemViewHolder {
        viewBindHelper.setOpenOnlyOne(true)
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_main_list, parent, false)
        return ItemViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        viewBindHelper.bind(holder.binding.srLayout, mList[position].priority.toString())
        holder.binding.model = mList[position]
        holder.binding.executePendingBindings()

        holder.binding.rlDuplicate.setOnClickListener {
            mListener.onClickDuplicate(position)
        }
        holder.binding.rlEdit.setOnClickListener {
            mListener.onClickEdit(position)
        }

    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: AdapterMainListBinding = DataBindingUtil.bind(view)!!
    }

    fun setMyListener(listener: IListingListener) {
        mListener = listener
    }

    interface IListingListener {
        fun onClickDuplicate(position: Int)
        fun onClickEdit(position: Int)
    }
}