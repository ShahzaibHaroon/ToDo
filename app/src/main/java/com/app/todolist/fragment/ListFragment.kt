package com.app.todolist.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.todolist.R
import com.app.todolist.adapter.ListAdapter
import com.app.todolist.databinding.FragmentListsBinding
import com.app.todolist.helper.ActivityBase
import com.app.todolist.helper.BaseFragment
import com.app.todolist.helper.DBHelper
import com.app.todolist.model.ListModel


class ListFragment : BaseFragment(), View.OnClickListener, ListAdapter.IListingListener {

    lateinit var binding: FragmentListsBinding
    private var mList = ArrayList<ListModel>()
    var dbHelper: DBHelper = DBHelper(ActivityBase.activity)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lists, container, false)
        setListener()
        return binding.root
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser) {
            Handler(Looper.myLooper()!!).postDelayed({
                getList()
            }, 2000)
        }
    }

    private fun getList() {
        binding.pbLoading.visibility = View.GONE
        val list = dbHelper.getList()
        if (list.isEmpty()) {
            binding.isData = false
        }
        else {
            binding.isData = true
            list.sortBy { it.priority }
            mList.clear()
            mList.addAll(list)
            setAdapter()
        }
    }

    private fun setAdapter() {
        val linearLayoutManager = LinearLayoutManager(ActivityBase.activity)
        binding.rvList.layoutManager = linearLayoutManager
        val adapter = ListAdapter(mList)
        binding.rvList.adapter = adapter
        adapter.setMyListener(this)
    }

    private fun setListener() {
        binding.rlCreateList.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlAddItem -> {

            }
        }
    }

    override fun onClickDuplicate(position: Int) {
        callFragmentWithReplace(R.id.mainContainer, CreateEditListFragment.newInstance(mList[position].priority, mList[position].listName, true, false), "CreateEditListFragment")
    }

    override fun onClickEdit(position: Int) {
        callFragmentWithReplace(R.id.mainContainer, CreateEditListFragment.newInstance(mList[position].priority, mList[position].listName, true, false), "CreateEditListFragment")
    }
}