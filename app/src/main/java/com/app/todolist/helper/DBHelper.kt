package com.app.todolist.helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.app.todolist.model.ItemModel
import com.app.todolist.model.ListModel

class DBHelper(context: Context) : SQLiteOpenHelper(context, "MyDB", null, 1) {

    val LIST_TABLE_NAME = "Lists"
    val LIST_COL_PRIORITY = "priority" // 0
    val LIST_COL_NAME = "name" //1
    val LIST_COL_STATUS = "status" // 2

    val ITEM_TABLE_NAME = "Items"
    val ITEM_COL_ID = "id" // 0
    val ITEM_COL_LIST_ID = "list_id" //1
    val ITEM_COL_NAME = "name" // 2
    val ITEM_COL_PRICE = "price" // 3
    val ITEM_COL_STATUS = "status" //4

    override fun onCreate(db: SQLiteDatabase?) {
        val createListTable = "CREATE TABLE $LIST_TABLE_NAME ($LIST_COL_PRIORITY INTEGER PRIMARY KEY AUTOINCREMENT,$LIST_COL_NAME VARCHAR(256), $LIST_COL_STATUS INTEGER)"
        db?.execSQL(createListTable)
        val createItemTable = "CREATE TABLE $ITEM_TABLE_NAME ($ITEM_COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,$ITEM_COL_LIST_ID INTEGER ,$ITEM_COL_NAME VARCHAR(256), $ITEM_COL_PRICE INTEGER, $ITEM_COL_STATUS INTEGER)"
        db?.execSQL(createItemTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }

    fun createList(item: ListModel) {
        val db = this.writableDatabase

        val cv = ContentValues()
        cv.put(LIST_COL_NAME, item.listName)
        cv.put(LIST_COL_STATUS, item.itemStatus)

        val res = db.insert(LIST_TABLE_NAME, null, cv)
    }

    fun createListItem(listID: Int, listItems: ArrayList<ItemModel>){
        val db = this.writableDatabase
        for (i in 0 until listItems.size) {
            val cv1 = ContentValues()
            cv1.put(ITEM_COL_NAME, listItems[i].itemName)
            cv1.put(ITEM_COL_PRICE, listItems[i].itemPrice)
            cv1.put(ITEM_COL_STATUS, listItems[i].itemStatus)
            cv1.put(ITEM_COL_LIST_ID, listID)
            db.insert(ITEM_TABLE_NAME, null, cv1)
        }
    }

    fun addItem(item: ItemModel) {
        val db = this.writableDatabase
        val cv1 = ContentValues()
        cv1.put(ITEM_COL_LIST_ID, item.priorityListId)
        cv1.put(ITEM_COL_NAME, item.itemName)
        cv1.put(ITEM_COL_PRICE, item.itemPrice)
        cv1.put(ITEM_COL_STATUS, item.itemStatus)
        db.insert(ITEM_TABLE_NAME, null, cv1)
    }

    fun getList(): ArrayList<ListModel> {

        val list = ArrayList<ListModel>()
        val db = this.readableDatabase
        val query = "Select * from $LIST_TABLE_NAME"
        val res = db.rawQuery(query, null)

        if (res.moveToFirst()) {
            do {
                list.add(ListModel(res.getString(0).toInt(), res.getString(1), res.getString(2).toInt()))
            } while (res.moveToNext())
        }

        res.close()
        db.close()
        return list
    }

    fun getItems(priorityId: Int): ArrayList<ItemModel> {
        val list = ArrayList<ItemModel>()
        val db = this.readableDatabase
        val query = "Select * from $ITEM_TABLE_NAME where $ITEM_COL_LIST_ID = $priorityId"
        val res = db.rawQuery(query, null)
        if (res.moveToFirst()) {
            do {
                list.add(ItemModel(priorityId, res.getString(0), res.getString(2), res.getString(3).toInt(), res.getString(4).toInt()))
            } while (res.moveToNext())
        }

        res.close()
        db.close()
        return list
    }

    fun deleteItem(itemID: String) {
        val db = this.writableDatabase
        db.delete(ITEM_TABLE_NAME, "$ITEM_COL_ID=?", arrayOf(itemID))
        db.close()
    }

    fun deleteList(priorityId: Int) {
        val db = this.writableDatabase
        db.delete(LIST_TABLE_NAME, "$LIST_COL_PRIORITY=?", arrayOf(priorityId.toString()))
        db.close()
    }

    fun updateStatus(itemID: Int, priorityId: Int) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(ITEM_COL_STATUS, 1)
        db.update(ITEM_TABLE_NAME, cv, "$ITEM_COL_ID = ?", arrayOf(itemID.toString()))
        db.close()
    }

    fun updateItem(item: ItemModel) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(ITEM_COL_LIST_ID, item.priorityListId)
        cv.put(ITEM_COL_NAME, item.itemName)
        cv.put(ITEM_COL_PRICE, item.itemPrice)
        cv.put(ITEM_COL_STATUS, item.itemStatus)
        db.update(ITEM_TABLE_NAME, cv, "$ITEM_COL_ID =? AND $ITEM_COL_LIST_ID =?", arrayOf(item.itemId, item.priorityListId.toString()))
        db.close()
    }

    fun updateListStatus(listID: Int) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(LIST_COL_STATUS, 1)

        db.update(LIST_TABLE_NAME, cv, "$LIST_COL_PRIORITY =?", arrayOf(listID.toString()))
        db.close()
    }

    fun updateListName(listName: String, listID: Int) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(LIST_COL_NAME, listName)

        db.update(LIST_TABLE_NAME, cv, "$LIST_COL_PRIORITY =?", arrayOf(listID.toString()))
        db.close()
    }
}