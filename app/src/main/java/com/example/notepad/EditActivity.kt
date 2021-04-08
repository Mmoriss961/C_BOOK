package com.example.notepad

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.example.notepad.DB.MyAdapter
import com.example.notepad.DB.MyDBManager
import com.example.notepad.DB.MyIntentConstants
import kotlinx.android.synthetic.main.edit_activity.*

class EditActivity : AppCompatActivity() {

    var id = 0
    var isEditState = false
    val imageRequestCode = 10
    var tempImageUri ="empty"
    val myDBManager = MyDBManager(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_activity)
        getMyIntent()
    }

    override fun onResume() {
        super.onResume()
        myDBManager.openDb()

    }
    override fun onDestroy() {
        super.onDestroy()
        myDBManager.closeDb()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == imageRequestCode){
            imMainImg.setImageURI(data?.data)
            tempImageUri = data?.data.toString()
            contentResolver.takePersistableUriPermission(data?.data!!,Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    fun onClickAddImg(view: View) {
        MainImgLayout.visibility = View.VISIBLE
        fbAddImg.visibility = View.GONE
    }

    fun onClickDelImg(view: View) {
        MainImgLayout.visibility = View.GONE
        fbAddImg.visibility = View.VISIBLE
        tempImageUri ="empty"
    }

    fun onClickChooseImg(view: View) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent,imageRequestCode)

    }
    fun onClickSave(view: View) {
        val myTitle =edTltle.text.toString()
        val myDesc =edDesc.text.toString()
        if(myTitle !=""&& myDesc != "") {
            if(isEditState){
                myDBManager.updateTtem(myTitle,myDesc,tempImageUri,id)

            }
            else{
            myDBManager.insertToDB(myTitle,myDesc,tempImageUri)
            }
        }
        finish()
    }

    fun onEditEnable(view: View){
        edTltle.isEnabled=true
        edDesc.isEnabled=true
        fbSave.visibility = View.VISIBLE
        fbOnEditEnable.visibility = View.GONE
        fbAddImg.visibility=View.VISIBLE
        if(tempImageUri == "empty")return
        imButtonEditImg.visibility=View.VISIBLE
        imButtonDelImg.visibility=View.VISIBLE


    }

    fun getMyIntent(){
        fbOnEditEnable.visibility = View.GONE
        val i = intent
        if(i!=null){
            if(i.getStringExtra(MyIntentConstants.I_TITLE_KEY) != null){
                fbAddImg.visibility = View.GONE
                fbSave.visibility = View.GONE
                edTltle.setText(i.getStringExtra(MyIntentConstants.I_TITLE_KEY))
                isEditState=true
                edTltle.isEnabled=false
                edDesc.isEnabled=false
                fbOnEditEnable.visibility = View.VISIBLE
                edDesc.setText(i.getStringExtra(MyIntentConstants.I_DESC_KEY))
                id=i.getIntExtra(MyIntentConstants.I_ID_KEY,0)
                if(i.getStringExtra(MyIntentConstants.I_URI_KEY) !="empty"){

                    MainImgLayout.visibility = View.VISIBLE
                    tempImageUri = i.getStringExtra(MyIntentConstants.I_URI_KEY)!!
                    imMainImg.setImageURI(Uri.parse(tempImageUri))
                    imButtonDelImg.visibility=View.GONE
                    imButtonEditImg.visibility=View.GONE
                }
            }
        }
    }
}