package com.example.notepad.DB

import android.content.Context
import android.content.Intent
import android.icu.text.CaseMap
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notepad.Custom
import com.example.notepad.EditActivity
import com.example.notepad.R
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.rc_item.view.*

class MyAdapter(listMain:ArrayList<Custom>,contextM:Context):RecyclerView.Adapter<MyAdapter.MyHolder>(){
    var listArray = listMain
    var context = contextM

    class MyHolder(itemView: View,contextV: Context) : RecyclerView.ViewHolder(itemView) {

        val tvTitle:TextView = itemView.findViewById(R.id.tvTitle)
        val context =contextV

        fun setData(item:Custom){
            tvTitle.text = item.title
            itemView.setOnClickListener{
                val intent = Intent(context,EditActivity::class.java).apply {

                    putExtra(MyIntentConstants.I_TITLE_KEY,item.title)
                    putExtra(MyIntentConstants.I_DESC_KEY,item.desc)
                    putExtra(MyIntentConstants.I_URI_KEY,item.URI)
                    putExtra(MyIntentConstants.I_ID_KEY,item.id)
                }
                context.startActivity(intent)
            }
        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater = LayoutInflater.from(parent.context )
        return MyHolder(inflater.inflate(R.layout.rc_item,parent,false),context)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val currentItem=listArray[position]
        holder.itemView.tvTitle.text = currentItem.title
        holder.itemView.tvDesc.text = currentItem.desc


        holder.setData(listArray.get(position))


        if(currentItem.URI!="empty")
            holder.itemView.imageView.setImageURI( Uri.parse(currentItem.URI))
        else {
            holder.itemView.imageView.setImageResource(R.mipmap.ic_eat_1_round);
       // holder.itemView.rcView.setOnClickListener {
            //val action=ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
            // holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {

        return listArray.size

    }
    open fun updateAdapter(listItems:List<Custom>){

        listArray.clear()
        listArray.addAll(listItems)
        notifyDataSetChanged()
    }

    fun removeItem(pos:Int,dbManager: MyDBManager){
        dbManager.removeItemFromDB(listArray[pos].id.toString())
        listArray.removeAt(pos)
        notifyItemRangeChanged(0,listArray.size)
        notifyItemRemoved(pos)

    }
}