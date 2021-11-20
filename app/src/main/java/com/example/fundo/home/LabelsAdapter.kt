package com.example.fundo.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.fundo.R
import com.example.fundo.model.Labels
import java.util.*
import kotlin.collections.ArrayList

class LabelsAdapter(private var labelList: ArrayList<Labels>) :
    RecyclerView.Adapter<LabelsAdapter.MyViewHolder>() {
    private lateinit var mListener: onItemClickListener
    interface onItemClickListener {
        fun onItemClick(position: Int)
        fun editClick(position: Int,data: String)
    }
    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.label_list, parent, false)
        return LabelsAdapter.MyViewHolder(itemView, mListener)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = labelList[position]
        holder.inputLabel.setText(currentItem.labelName.toString())
    }

    override fun getItemCount(): Int {
        return labelList.size
    }


    class MyViewHolder(itemView: View, listener: LabelsAdapter.onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {

        val inputLabel: EditText = itemView.findViewById(R.id.label)
        val delete: ImageView = itemView.findViewById(R.id.deleteButton)
        val edit: ImageView = itemView.findViewById(R.id.editButton)
        init {
            delete.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
            edit.setOnClickListener {
                var updated = inputLabel.text.toString()
                listener.editClick(adapterPosition,updated)
            }

        }
    }


}