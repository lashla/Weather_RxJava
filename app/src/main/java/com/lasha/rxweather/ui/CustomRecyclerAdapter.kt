package com.lasha.rxweather.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lasha.rxweather.R
import com.lasha.rxweather.domain.model.ForecastItem
import com.squareup.picasso.Picasso
import okhttp3.internal.notify
import javax.inject.Inject

class CustomRecyclerAdapter @Inject constructor(private val context: Context, private val mList: MutableList<ForecastItem>): RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.forecast_view, parent, false)
        return ViewHolder(view)
    }

    fun insertItem(data: ForecastItem){
        mList.add(data)
        notifyItemInserted(mList.lastIndex)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = mList[position]
        holder.tvTime.text = itemsViewModel.timeText
        holder.tvTimeTemp.text = itemsViewModel.tempText
        Picasso.get()
               .load(itemsViewModel.image)
               .error(androidx.constraintlayout.widget.R.drawable.abc_btn_check_to_on_mtrl_000)
               .into(holder.ivTempOfTime)
    }

    override fun getItemCount(): Int {
        return mList.count()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val tvTimeTemp: TextView = itemView.findViewById(R.id.tvTimeTemp)
        val ivTempOfTime: ImageView = itemView.findViewById(R.id.ivTempOfTime)

    }

}