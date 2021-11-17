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
    RecyclerView.Adapter<NotesAdapter.MyViewHolder>(),Filterable {

    private lateinit var mListener: onItemClickListener

    //------------night
    var filteredNotes: ArrayList<Notes> = ArrayList()
    init {
        filteredNotes = noteList
    }
    //------------night

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

    }

    override fun getItemCount(): Int {
        return noteList.size
    }


    class MyViewHolder(itemView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.findViewById(R.id.inputTypeTitle)
        val inputContent: TextView = itemView.findViewById(R.id.inputTypeNote)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    //night
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                filteredNotes = if (charSearch.isEmpty()) {
                    noteList as ArrayList<Notes>
                } else {
                    val resultList = ArrayList<Notes>()
                    for (row in noteList) {
                        if ((row.title!!.lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT))
                                    or (row.content!!.lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT))))
                        ) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredNotes
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredNotes = results?.values as ArrayList<Notes>
                notifyDataSetChanged()
            }

        }
    }
//night




}

