package com.shang.fcu_food.Data

import com.shang.fcu_food.DataBind
import com.shang.fcu_food.R

class UserComment {
    var uid: String = ""           //使用者的UID或是帳號
    var comment: String = ""      //使用者給予的評價
    var star: Double = -1.0        //使用者給予的星數

    constructor()

    constructor(uid:String,comment:String,star:String){
        this.uid=uid
        this.star=star.toDouble()
        this.comment=comment
    }

    fun toMap():MutableMap<String,Any>{
        var map= mutableMapOf<String,Any>()
        map.put("uid",uid)
        map.put("star",star)
        map.put("comment",comment)
        return map
    }

    fun getUserName(uid:String):String{
        var name = DataBind.allUser.get(uid)?.name
        if (name != null)
            return name
        return "小明"
    }

    fun getUserPicture(uid: String): Int {
        var user = DataBind.allUser.get(uid)
        when (user?.picture) {
            "1" -> return R.drawable.ic_cat
            "2" -> return R.drawable.ic_dog
            "3" -> return R.drawable.ic_boy
            "4" -> return R.drawable.ic_girl
        }
        return R.drawable.ic_user
    }
}