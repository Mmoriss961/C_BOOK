package com.example.notepad

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notepad.DB.MyAdapter
import com.example.notepad.DB.MyDBManager
import kotlinx.android.synthetic.main.activity_main.*
import androidx.appcompat.widget.SearchView

class MainActivity : AppCompatActivity() {


    val myDBManager = MyDBManager(this)
    val myAdapter = MyAdapter(ArrayList(),this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

    }

    override fun onResume() {
        super.onResume()
        myDBManager.openDb()
        fillAdapter()

    }
    override fun onDestroy() {
        super.onDestroy()
        myDBManager.closeDb()
    }

    fun onClickNew(view: View) {
        var i = Intent(this, EditActivity::class.java)
        startActivity(i)
    }

    fun init(){

        rcView.layoutManager=LinearLayoutManager(this)
        val swapHelper =getSwapMg()
        swapHelper.attachToRecyclerView(rcView)
        rcView.adapter = myAdapter
    }

    fun fillAdapter(){

        val list = myDBManager.readDbData("")
         myAdapter.updateAdapter(list)
        if(list.size>0){
            tvNoElements.visibility = View.GONE
        }
        else{
            tvNoElements.visibility = View.VISIBLE
        }
    }

    private fun getSwapMg():ItemTouchHelper{
        return ItemTouchHelper(object:ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                myAdapter.removeItem(viewHolder.adapterPosition,myDBManager)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater=menuInflater
        inflater.inflate(R.menu.main,menu)

        val manager= getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.menu_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {
                val list = myDBManager.readDbData(text!!)
                myAdapter.updateAdapter(list)
                return true
            }

        })



        return super.onCreateOptionsMenu(menu)
    }


}

