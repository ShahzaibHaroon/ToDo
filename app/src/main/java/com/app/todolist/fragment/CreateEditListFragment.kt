package com.app.todolist.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.todolist.R
import com.app.todolist.adapter.ViewAdapter
import com.app.todolist.databinding.FragmentCreateEditListBinding
import com.app.todolist.helper.ActivityBase
import com.app.todolist.helper.BaseFragment
import com.app.todolist.helper.DBHelper
import com.app.todolist.model.ItemModel
import com.app.todolist.model.ListModel

class CreateEditListFragment : BaseFragment(), View.OnClickListener, ViewAdapter.IItemListener, TextWatcher {

    lateinit var binding: FragmentCreateEditListBinding
    private var mList = ArrayList<ItemModel>()
    lateinit var dbHelper: DBHelper
    lateinit var adapter: ViewAdapter
    var itemPos = 0

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: CreateEditListFragment
        var listId = 0
        lateinit var listName: String
        var isUpdate: Boolean = false
        var isDuplicate: Boolean = false

        fun newInstance(id: Int, name: String, isUp: Boolean, isList: Boolean): CreateEditListFragment {
            instance = CreateEditListFragment()
            listId = id
            listName = name
            isUpdate = isUp
            isDuplicate = isList
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_edit_list, container, false)
        setViews()
        setListener()
        return binding.root
    }

    private fun setViews() {
        dbHelper = DBHelper(ActivityBase.activity)

        if (isDuplicate) {
            val tempList = ArrayList<ItemModel>()
            tempList.addAll(dbHelper.getItems(listId))
            tempList.map { it.itemStatus = 0 }
            dbHelper.createList(ListModel(0, listName, 0))
            val list = dbHelper.getList().sortedByDescending { it.priority }
            listId = list[0].priority
            dbHelper.createListItem(listId, tempList)

            isUpdate = true
            binding.isData = true
            binding.etName.setText(listName)
            mList.clear()
            mList.addAll(dbHelper.getItems(listId))
            setAdapter()
        }
        else {
            if (isUpdate) {
                binding.isData = true
                binding.etName.setText(listName)
                mList.clear()
                mList.addAll(dbHelper.getItems(listId))
                setAdapter()
            }
            else {
                listId = 0
                binding.isData = false
            }
        }


    }

    private fun setAdapter() {
        val linearLayoutManager = LinearLayoutManager(ActivityBase.activity)
        binding.rvList.layoutManager = linearLayoutManager
        adapter = ViewAdapter(mList)
        binding.rvList.adapter = adapter
        adapter.setMyListener(this)
    }

    private fun setListener() {
        binding.btnAddItem.setOnClickListener(this)
        binding.btnSaveItem.setOnClickListener(this)
        binding.btnSaveList.setOnClickListener(this)
        binding.etItemName.addTextChangedListener(this)
        binding.etItemPrice.addTextChangedListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnAddItem -> {
                if (validateInput()) {
                    mList.add(ItemModel(listId, "", binding.etItemName.text.toString(), binding.etItemPrice.text.toString().toInt(), 0))
                    if (mList.size == 1) {
                        binding.isData = true
                        setAdapter()
                    }
                    else adapter.notifyDataSetChanged()
                    if (isUpdate) {
                        dbHelper.addItem(ItemModel(listId, "", binding.etItemName.text.toString(), binding.etItemPrice.text.toString().toInt(), 0))
                    }
                }

                binding.etItemName.setText("")
                binding.etItemPrice.setText("")
            }
            R.id.btnSaveItem -> {
                if (validateInput()) {
                    binding.btnSaveItem.visibility = View.GONE
                    mList[itemPos].itemName = binding.etItemName.text.toString()
                    mList[itemPos].itemPrice = binding.etItemPrice.text.toString().toInt()
                    adapter.notifyDataSetChanged()
                    dbHelper.updateItem(mList[itemPos])
                    binding.etItemName.setText("")
                    binding.etItemPrice.setText("")
                }


            }
            R.id.btnSaveList -> {
                if (!TextUtils.isEmpty(binding.etName.text.toString())) {
                    if (isUpdate) {
                        if (listName != binding.etName.text.toString()) {
                            dbHelper.updateListName(binding.etName.text.toString(), listId)
                        }
                        else {
                            Toast.makeText(ActivityBase.activity, "List Saved", Toast.LENGTH_LONG).show()
                            ActivityBase.activity.onBackPressed()
                        }
                    }
                    else {
                        var status = 0
                        if (mList.any {
                                it.itemStatus == 0
                            }) {
                            status = 0
                        }
                        else {
                            status = 1
                        }
                        dbHelper.createList(ListModel(0, binding.etName.text.toString(), status))
                        val list = dbHelper.getList().sortedByDescending { it.priority }
                        listId = list[0].priority
                        dbHelper.createListItem(listId, mList)
                        Toast.makeText(ActivityBase.activity, "List Saved", Toast.LENGTH_LONG).show()
                        ActivityBase.activity.onBackPressed()
                    }

                }
                else {
                    binding.etName.requestFocus()
                    Toast.makeText(ActivityBase.activity, "Enter List Name", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun checkAndSet() {
        if (TextUtils.isEmpty(binding.etItemName.text.toString())) {
            binding.btnAddItem.setBackgroundColor(ContextCompat.getColor(ActivityBase.activity, R.color.gray))
        }
        else if (TextUtils.isEmpty(binding.etItemPrice.text.toString())) {
            binding.btnAddItem.setBackgroundColor(ContextCompat.getColor(ActivityBase.activity, R.color.gray))
        }
        else binding.btnAddItem.setBackgroundColor(ContextCompat.getColor(ActivityBase.activity, R.color.orange))
    }

    private fun validateInput(): Boolean {
        if (TextUtils.isEmpty(binding.etName.text.toString())) {
            binding.etName.requestFocus()
            Toast.makeText(ActivityBase.activity, "Enter List Name", Toast.LENGTH_LONG).show()
            return false
        }
        else if (TextUtils.isEmpty(binding.etItemName.text.toString())) {
            binding.etItemName.requestFocus()
            Toast.makeText(ActivityBase.activity, "Enter Item Name", Toast.LENGTH_LONG).show()
            return false
        }
        else if (TextUtils.isEmpty(binding.etItemPrice.text.toString())) {
            binding.etItemPrice.requestFocus()
            Toast.makeText(ActivityBase.activity, "Enter Item Price", Toast.LENGTH_LONG).show()
            return false
        }
        else return true
    }

    override fun onClickEdit(position: Int) {
        binding.btnSaveItem.visibility = View.VISIBLE
        itemPos = position
        binding.etItemName.setText(mList[position].itemName)
        binding.etItemPrice.setText(mList[position].itemPrice.toString())
    }

    override fun onClickDelete(position: Int) {
        dbHelper.deleteItem(mList[position].itemId)
        mList.removeAt(position)
        adapter.notifyDataSetChanged()

        if (mList.isEmpty()) {
            dbHelper.deleteList(mList[position].priorityListId)
            Toast.makeText(ActivityBase.activity, "No Item Left in List", Toast.LENGTH_LONG).show()
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable?) {
        checkAndSet()
    }
}