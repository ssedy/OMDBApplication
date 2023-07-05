package com.sarahsedy.omdbapplication

import android.provider.Settings.Global.getString
import android.provider.Settings.Secure.getString
import android.provider.Settings.System.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

internal class RecordAdapter(
    private var cellList: ArrayList<CellItem>,
    val clickListener: (CellItem, Int) -> Unit
) :
    RecyclerView.Adapter<RecordAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var cellID: TextView = view.findViewById(R.id.cellID)
        var cellTitle: TextView = view.findViewById(R.id.cellTitle)
        var cellYear: TextView = view.findViewById(R.id.cellYear)
        var cellType: TextView = view.findViewById(R.id.cellType)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.cell_layout, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val cellItem = cellList[position]

        holder.cellID.text =
            holder.itemView.context.getString(R.string.txt_cell_ID, cellItem.imdbID)
        holder.cellTitle.text =
            holder.itemView.context.getString(R.string.txt_cell_title, cellItem.title)
        holder.cellYear.text =
            holder.itemView.context.getString(R.string.txt_cell_year, cellItem.year)
        holder.cellType.text =
            holder.itemView.context.getString(R.string.txt_cell_type, cellItem.type)

        holder.itemView.setOnClickListener { clickListener(cellItem, position) }
    }

    override fun getItemCount(): Int {
        return cellList.size
    }
}