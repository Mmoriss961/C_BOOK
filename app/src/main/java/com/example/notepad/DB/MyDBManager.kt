package com.example.notepad.DB

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.example.notepad.Custom

class MyDBManager( context: Context) {
    val myDBHelper = MyDBHelper(context)
    var db: SQLiteDatabase? = null

    fun openDb(){
        db = myDBHelper.writableDatabase
    }
    fun insertToDB( title: String, content: String, uri:String){
        val values = ContentValues().apply{
            put(MyDB.COLUMN_NAME_TITLE, title)
            put(MyDB.COLUMN_NAME_CONTENT, content)
            put(MyDB.COLUMN_NAME_IMAGE_URI, uri)
        }
        db?.insert(MyDB.TABLE_NAME,null,values)
    }

    fun updateTtem( title: String, content: String, uri:String,id:Int){
        val selection = BaseColumns._ID + "=$id"
        val values = ContentValues().apply{
            put(MyDB.COLUMN_NAME_TITLE, title)
            put(MyDB.COLUMN_NAME_CONTENT, content)
            put(MyDB.COLUMN_NAME_IMAGE_URI, uri)
        }
        db?.update(MyDB.TABLE_NAME,values,selection,null)
    }
    fun removeItemFromDB(id:String){
        val selection = BaseColumns._ID + "=$id"
        db?.delete(MyDB.TABLE_NAME,selection,null)
    }

    fun readDbData(searchText:String):ArrayList<Custom>{
        val dataList = ArrayList<Custom>()
        val selection = "${MyDB.COLUMN_NAME_TITLE} like ?"
        val cursor = db?.query(
            MyDB.TABLE_NAME,null,selection, arrayOf("%$searchText%"),null,null,null)

            while(cursor?.moveToNext()!!){
                val custom = Custom()

                custom.title = cursor.getString(cursor.getColumnIndex(MyDB.COLUMN_NAME_TITLE))
                custom.desc = cursor.getString(cursor.getColumnIndex(MyDB.COLUMN_NAME_CONTENT))
                custom.URI = cursor.getString(cursor.getColumnIndex(MyDB.COLUMN_NAME_IMAGE_URI))
                custom.id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
                dataList.add(custom)

            }

        cursor.close()
        return dataList
    }
     fun closeDb(){
         myDBHelper.close()
     }
}