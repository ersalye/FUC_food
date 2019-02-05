package com.shang.fcu_food.Data

import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import com.shang.fcu_food.R

class DrinkShop : Shop() {
    companion object {
        val tag: String = "drink"
        var allDrinkShop: MutableList<DrinkShop> = mutableListOf<DrinkShop>()
    }

    override var errorDrawable: Int = R.drawable.ic_shop
    override fun getQuery(): DatabaseReference? {
        var query = FirebaseDatabase.getInstance().getReference().child(DrinkShop.tag)
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                DrinkShop.allDrinkShop.clear()
                for (data in snapshot.children) {
                    var drinkShop = data.getValue(DrinkShop::class.java)
                    DrinkShop.allDrinkShop.add(drinkShop!!)
                }
            }
        })
        return query
    }

    override fun getOption(): FirebaseRecyclerOptions<Shop> {
        var options = FirebaseRecyclerOptions.Builder<DrinkShop>()
            .setQuery(getQuery()!!, DrinkShop::class.java).build()
        return options as FirebaseRecyclerOptions<Shop>
    }
}