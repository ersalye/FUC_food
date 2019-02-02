package com.shang.fcu_food.DetailShop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.ads.AdView
import com.shang.fcu_food.Data.*
import com.shang.fcu_food.DetailMenu.DetailMenuActivity
import com.shang.fcu_food.Dialog.EditShopDialog
import com.shang.fcu_food.Dialog.ImageViewDialog
import com.shang.fcu_food.Main.GlideApp
import com.shang.fcu_food.R
import com.shang.fcu_food.Unit.AdmobUnit
import com.shang.fcu_food.Unit.FileStorageUnit
import com.shang.fcu_food.Unit.FirebaseUnits
import kotlinx.android.synthetic.main.cardview_detailshop.view.*
import org.jetbrains.anko.toast
import java.io.File

class DetailShopVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var shopNameTv = itemView.findViewById<TextView>(R.id.shopNameTv)
    var shopPhoneTv = itemView.findViewById<TextView>(R.id.shopPhoneTv)
    var shopOpenTv = itemView.findViewById<TextView>(R.id.shopOpenTv)
    var shopStarTv = itemView.findViewById<TextView>(R.id.shopStarTv)
    var shopMapIg = itemView.findViewById<ImageView>(R.id.shopMapIg)
    var shopMenu = itemView.findViewById<RecyclerView>(R.id.shopMenu)
    var shopPictureImg = itemView.findViewById<ImageView>(R.id.shopPictureImg)
    var shopAdView=itemView.findViewById<AdView>(R.id.shopAdView)
    var shopEditImg=itemView.findViewById<ImageView>(R.id.shopEditImg)

    fun bind(shop_tag: String, model: Shop, activity: DetailShopActivity) {
        when (shop_tag) {
            BreakfastShop.tag -> model as BreakfastShop
            DinnerShop.tag -> model as DinnerShop
            SnackShop.tag -> model as SnackShop
            DrinkShop.tag -> model as DrinkShop
        }
        itemView.shopNameTv.text = model.name
        itemView.shopOpenTv.text = model.time
        itemView.shopStarTv.text = String.format("%.1f", model.star)
        itemView.shopPhoneTv.text = model.phone


        var path=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            .toString()+"/${shop_tag}_${model.name}_${model.name}.jpg"

        GlideApp.with(itemView.context)
            .load(FirebaseUnits.storage_getImageRef(shop_tag,model.name,model.name))
            .error(R.drawable.ic_shop)
            .into(itemView.shopPictureImg)



        itemView.shopMapIg.setOnClickListener {
            itemView.context.toast("功能尚未實作")
        }

        itemView.shopPictureImg.setOnClickListener {
            ImageViewDialog.getInstance(shop_tag,model.name,model.name)
                .show(activity.supportFragmentManager, ImageViewDialog.TAG)
        }

        itemView.shopMenu.layoutManager = GridLayoutManager(itemView.context, 2)
        itemView.shopMenu.adapter =
                SimpleMenuAdapter(
                    let { model.menu },
                    shop_tag,
                    model.name,
                    getItemClick(shop_tag, model.id.toString(), model.name, itemView.context)
                )

        itemView.shopEditImg.setOnClickListener {
            EditShopDialog.getInstance(model,shop_tag).show(activity.supportFragmentManager,EditShopDialog.TAG)
        }

        AdmobUnit.getInstance(itemView.context)?.show(itemView.shopAdView)
    }

    //傳遞shop的id和type 還有position
    fun getItemClick(tag: String, id: String, name: String, context: Context): OnItemClickHandler {
        var itemClick = object : OnItemClickHandler {
            override fun onItemClick(bundle: Bundle) {
                bundle.putString(DataConstant.SHOP_NAME, name)
                bundle.putString(DataConstant.SHOP_TYPE_TAG, tag)
                bundle.putString(DataConstant.SHOP_ID, id)
                var intent = Intent(context, DetailMenuActivity::class.java).apply { this.putExtras(bundle) }
                context.startActivity(intent)
            }
        }
        return itemClick
    }



}