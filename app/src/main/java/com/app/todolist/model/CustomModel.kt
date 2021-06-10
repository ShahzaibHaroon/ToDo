package com.app.todolist.model

data class ItemModel(var priorityListId: Int, var itemId: String, var itemName: String, var itemPrice: Int, var itemStatus: Int)

data class ListModel(var priority: Int, var listName: String, var itemStatus: Int)

data class MainModel(var list: ListModel, var listItems: ArrayList<ItemModel>)