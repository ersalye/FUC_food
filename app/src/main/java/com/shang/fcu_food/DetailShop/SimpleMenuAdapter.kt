package com.shang.fcu_food.DetailShop

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.shang.fcu_food.Data.Menu
import com.shang.fcu_food.DataBind
import com.shang.fcu_food.DetailMenu.DetailMenuActivity
import com.shang.fcu_food.R
import kotlinx.android.synthetic.main.cardview_simplemenu.view.*

class SimpleMenuAdapter(var menuList: MutableList<Menu>,var onItemClick:OnItemClickHandler) :
    RecyclerView.Adapter<SimpleMenuVH>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SimpleMenuVH {
        var view = LayoutInflater.from(p0.context).inflate(R.layout.cardview_simplemenu, p0, false)
        return SimpleMenuVH(view)
    }

    override fun getItemCount(): Int = menuList.size

    override fun onBindViewHolder(holder: SimpleMenuVH, position: Int) {
        holder.bind(holder, position, menuList,onItemClick)
    }
}

class SimpleMenuVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var simpleMenuName = itemView.findViewById<TextView>(R.id.simpleMenuName)
    var simpleMenuPrice = itemView.findViewById<TextView>(R.id.simpleMenuPrice)
    var simpleMenuStar = itemView.findViewById<TextView>(R.id.simpleMenuStar)
    var simpleMenuImg = itemView.findViewById<ImageView>(R.id.simpleMenuImg)

    fun bind(holder: SimpleMenuVH, position: Int, model: MutableList<Menu>,onItemClick:OnItemClickHandler) {
        holder.itemView.simpleMenuName.setText(model.get(position).name)
        holder.itemView.simpleMenuPrice.setText(model.get(position).price.toString() + "元")
        holder.itemView.simpleMenuStar.setText(model.get(position).star.toString())
        holder.itemView.setOnClickListener {
            DataBind.menu=model //梆定menu資料
            var bundle = Bundle().apply {
                this.putInt(DetailMenuActivity.POSITION, position)
            }
            onItemClick.onItemClick(bundle)
        }


    }
}
