package com.sudoware.aqua.reminder.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sudoware.aqua.reminder.R
import com.sudoware.aqua.reminder.helpers.InfoHelper
import kotlinx.android.synthetic.main.info_item.view.*

class InformationAdapter(val context: Context, private val informationList: List<InfoHelper>, private val isThemeDark : Boolean) : RecyclerView.Adapter<InformationAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return if(isThemeDark){
            val v = LayoutInflater.from(parent.context).inflate(R.layout.info_item_dark, parent, false)
            ViewHolder(v)
        }else{
            val v = LayoutInflater.from(parent.context).inflate(R.layout.info_item, parent, false)
            ViewHolder(v)
        }

    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(context,informationList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return informationList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(context: Context,item: InfoHelper) {
            itemView.titleTextView.text=item.title
            itemView.descriptionTextView.text=item.description

            Glide.with(context).load(item.imageUrl).placeholder(R.drawable.placeholder).into(itemView.item_imageView)


        }
    }

}