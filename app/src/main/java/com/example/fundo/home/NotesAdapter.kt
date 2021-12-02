package com.example.fundo.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fundo.R
import java.util.*
import kotlin.collections.ArrayList

class NotesAdapter(private var noteList: ArrayList<Notes>) :
    RecyclerView.Adapter<NotesAdapter.MyViewHolder>(){

    private lateinit var mListener: onItemClickListener

    var filteredNotes: ArrayList<Notes> = ArrayList()
    init {
        filteredNotes = noteList
    }

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return MyViewHolder(itemView, mListener)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = noteList[position]
        holder.title.text = currentItem.title
        holder.inputContent.text = currentItem.content
        holder.label.text = currentItem.labelName
        holder.inputDate.text = currentItem.reminderDate
    }

    override fun getItemCount(): Int {
        return noteList.size
    }


    class MyViewHolder(itemView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.findViewById(R.id.inputTypeTitle)
        val inputContent: TextView = itemView.findViewById(R.id.inputTypeNote)
        val inputDate: TextView = itemView.findViewById(R.id.inputTypeDate)
        val label: TextView = itemView.findViewById(R.id.inputLabel)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}

