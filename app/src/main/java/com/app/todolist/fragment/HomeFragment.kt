package com.app.todolist.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.todolist.R
import com.app.todolist.adapter.ListItemAdapter
import com.app.todolist.databinding.FragmentHomeBinding
import com.app.todolist.helper.ActivityBase
import com.app.todolist.helper.BaseFragment
import com.app.todolist.helper.DBHelper
import com.app.todolist.model.ItemModel

class HomeFragment : BaseFragment(), View.OnClickListener, ListItemAdapter.IItemListener {

    lateinit var binding: FragmentHomeBinding
    private var mList = ArrayList<ItemModel>()
    private var isUpdate = false
    var dbHelper: DBHelper = DBHelper(ActivityBase.activity)
    var priorityId = 0
    var name = ""
    lateinit var adapter: ListItemAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.isData = true
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
        binding.btnMarkAll.visibility = View.GONE
        priorityId = 0
        isUpdate = false
        val list = dbHelper.getList()
        binding.pbLoading.visibility = View.GONE
        if (list.isEmpty()) {
            binding.isData = false
            binding.tvName.text = ""
        }
        else {
            val temp = list.sortedByDescending { it.priority }
            if (temp.any { it.itemStatus == 0 }) {
                binding.isData = true
                binding.btnMarkAll.visibility = View.VISIBLE
                binding.tvName.text = temp.filter { it.itemStatus == 0 }[0].listName
                priorityId = temp.filter { it.itemStatus == 0 }[0].priority
                name = temp.filter { it.itemStatus == 0 }[0].listName
                mList.clear()
                mList.addAll(dbHelper.getItems(priorityId).filter { it.priorityListId == priorityId })
                setAdapter()
                isUpdate = true
            }
            else {
                binding.isData = false
                binding.tvName.text = ""
            }
        }
    }

    private fun setAdapter() {
        val linearLayoutManager = LinearLayoutManager(ActivityBase.activity)
        binding.rvListItem.layoutManager = linearLayoutManager
        adapter = ListItemAdapter(mList)
        binding.rvListItem.adapter = adapter
        adapter.setMyListener(this)
    }

    private fun setListener() {
        binding.rlAddItem.setOnClickListener(this)
        binding.btnMarkAll.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlAddItem -> {
                callFragmentWithReplace(R.id.mainContainer, CreateEditListFragment.newInstance(priorityId, name, isUpdate, false), "CreateEditListFragment")
            }
            R.id.btnMarkAll -> markAllCompleted()
        }
    }

    private fun markAllCompleted() {
        for (i in 0 until mList.size) {
            mList[i].itemStatus = 1
            dbHelper.updateStatus(mList[i].itemId.toInt(), mList[i].priorityListId)
            adapter.notifyDataSetChanged()
        }

        dbHelper.updateListStatus(mList[0].priorityListId)
        Toast.makeText(ActivityBase.activity, "No Item Left in List", Toast.LENGTH_LONG).show()
        Handler(Looper.myLooper()!!).postDelayed({
            getList()
        }, 2000)
    }

    override fun onClickDone(position: Int) {
        mList[position].itemStatus = 1
        dbHelper.updateStatus(mList[position].itemId.toInt(), mList[position].priorityListId)
        adapter.notifyDataSetChanged()
        if (!mList.any { it.itemStatus != 0 }) {
            dbHelper.updateListStatus(mList[0].priorityListId)
            Toast.makeText(ActivityBase.activity, "No Item Left in List", Toast.LENGTH_LONG).show()
            Handler(Looper.myLooper()!!).postDelayed({
                getList()
            }, 2000)
        }

    }

    override fun onClickDelete(position: Int) {
        dbHelper.deleteItem(mList[position].itemId)
        mList.removeAt(position)
        adapter.notifyDataSetChanged()

        if (mList.isEmpty()) {
            dbHelper.deleteList(mList[position].priorityListId)
            Toast.makeText(ActivityBase.activity, "No Item Left in List", Toast.LENGTH_LONG).show()
            Handler(Looper.myLooper()!!).postDelayed({
                getList()
            }, 2000)
        }


    }
}