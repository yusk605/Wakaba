package com.example.wakaba.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wakaba.R
import com.example.wakaba.fragment.WakabaListFragmentDirections
import com.example.wakaba.room.Wakaba

class WakabaRecycleAdapter(
    diffUtil: DiffUtil.ItemCallback<Wakaba> =  object: DiffUtil.ItemCallback<Wakaba>(){
        override fun areItemsTheSame(oldItem: Wakaba, newItem: Wakaba): Boolean {
            return oldItem==newItem
        }
        override fun areContentsTheSame(oldItem: Wakaba, newItem: Wakaba): Boolean {
            return oldItem==newItem
        }
    }
):ListAdapter<Wakaba,WakabaRecycleAdapter.ItemHolder>(diffUtil) {

     var deleteListener:((Wakaba)->Unit)?=null
     var updateListener:((Wakaba)->Unit)?=null

    inner class ItemHolder(val view:View):RecyclerView.ViewHolder(view){
        val wakabaTitle: TextView=view.findViewById(R.id.wakaba_title)
        val wakabaRating:RatingBar=view.findViewById(R.id.wakaba_Rating)
        val wakabaDate:TextView=view.findViewById(R.id.wakaba_date)
        val checkTask:ImageView=view.findViewById(R.id.check_task)
        init {
            itemView.setOnClickListener {
                Navigation.findNavController(view)
                    .navigate(WakabaListFragmentDirections
                        .actionWakabaListFragmentToContentFragment(getItem(adapterPosition)))
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return  ItemHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.wakaba_item,parent,false)
        )
    }
    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.apply {
            wakabaTitle.text        =   getItem(position).wakabaTitle
            wakabaRating.rating     =   getItem(position).wakabaRating.toFloat()
            wakabaDate.text         =   getItem(position).wakabaDate
           when (getItem(position).checkFlag){
               0->{ checkTask.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_24) }
               else->{checkTask.setImageResource(R.drawable.ic_baseline_check_24)}
           }
        }
    }
    fun getItemAt(position: Int):Wakaba= getItem(position)

}