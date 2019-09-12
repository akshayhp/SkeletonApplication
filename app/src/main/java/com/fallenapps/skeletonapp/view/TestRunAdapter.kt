package com.fallenapps.skeletonapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fallenapps.skeletonapp.R
import com.fallenapps.skeletonapp.model.ApiResponse

class TestRunAdapter(var items:ArrayList<ApiResponse>): RecyclerView.Adapter<TestRunAdapter.TestViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {

        return TestViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_response_result,parent,false))
    }

    override fun getItemCount(): Int {return items.size}


    override fun onBindViewHolder(holder: TestViewHolder, position: Int) { holder.bind(items[position])}


    class TestViewHolder(v:View) : RecyclerView.ViewHolder(v){

        fun bind(item:ApiResponse){
            itemView.findViewById<TextView>(R.id.textView_time).text = item.responseTime
            itemView.findViewById<TextView>(R.id.textView_data).text = item.dataLength
            itemView.findViewById<TextView>(R.id.textView_type).text = item.source
        }
    }
}