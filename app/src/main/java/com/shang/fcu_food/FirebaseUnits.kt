package com.shang.fcu_food

import android.app.Activity
import android.content.Context
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.UploadTask
import com.shang.fcu_food.Data.TempMenu
import com.shang.fcu_food.Data.User
import com.shang.fcu_food.Data.UserComment
import com.shang.fcu_food.Main.GlideApp
import org.jetbrains.anko.toast

class FirebaseUnits {


    companion object {
        val AUTH_RQEUESTCODE = 100

        fun checkHasAuth(): Boolean {
            return FirebaseAuth.getInstance().currentUser != null
        }

        fun auth_Login(activity: Activity) {   //註冊與登入
            val providers =
                arrayListOf<AuthUI.IdpConfig>(AuthUI.IdpConfig.EmailBuilder().build())

            activity.startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(), AUTH_RQEUESTCODE
            )
        }

        //取得user
        fun auth_getUser(): FirebaseUser? {
            return FirebaseAuth.getInstance().currentUser
        }

        //綁定使用者資料 即時更新
        fun database_BindAllUser() {
            val userRef = FirebaseDatabase.getInstance()
                .getReference().child("user").addValueEventListener(
                    object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            Log.d("database_BindAllUser", p0.toString())
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            var userList = mutableMapOf<String, User>()
                            for (data in snapshot.children) {
                                var user = data.getValue(User::class.java)
                                user?.uid = data.key!!
                                userList.put(user!!.uid, user)
                            }
                            DataBind.allUser = userList
                        }
                    }
                )
        }


        fun storage_loadImg(
            context: Context,
            img: ImageView,
            tag: String,
            shop_name: String,
            name: String,
            option: RequestOptions
        ) {
            //option.transform(RoundedCornersTransformation(20,0))
            var ref = FirebaseStorage.getInstance()
                .getReference(tag).child(shop_name).child("$name.jpg")
            ref.downloadUrl.addOnSuccessListener {
                GlideApp.with(context).load(it).apply(option).into(img)
            }.addOnFailureListener {
                Log.d("TAG", "載入失敗" + it.toString())
            }
        }

        //database更新評論
        //database.getReference("breakfast") 一開始就是fucfood了
        //上傳資料用update 就算沒有那個節點他也會自己幫你生出來 ex:breakfast/3
        //setValue會把你整個節點都改掉
        //push他會自己生出一個hashID這個不是我要用的
        fun database_addCommemt(
            ref_path: String,
            rating: String,
            comment: String,
            uid: String,
            activity: FragmentActivity
        ) {

            val ref = FirebaseDatabase.getInstance().getReference().child(ref_path)
            var map = UserComment(uid, comment, rating).toMap()
            ref.updateChildren(map).addOnSuccessListener {
                activity.toast(R.string.CommentDialog_Success)
            }.addOnFailureListener {
                activity.toast(R.string.CommentDialog_Fail)
            }
        }

        //新增菜單
        fun database_addMenu(
            ref_path: String,
            tempMenu: TempMenu,
            activity: Activity
        ) {
            var f = FirebaseDatabase.getInstance().getReference(ref_path).push().setValue(tempMenu)
                .addOnSuccessListener {
                    activity.toast("新增成功")
                }.addOnFailureListener {
                    activity.toast("新增失敗:${it.message}")
                }
        }

        fun storage_addMenuImage(imageByte: ByteArray) {
            var ref = FirebaseStorage.getInstance().getReference().child("tempMenu/temp.jpeg")
            var mataData=StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .setContentDisposition("TEST")
                .build()
            ref.putBytes(imageByte,mataData)
                .addOnSuccessListener {
                    Log.d("TAG","SUCCESS")

                }.addOnFailureListener {
                    Log.d("TAG","Fail")
                }.addOnProgressListener{taskSnapshot ->
                    val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                    System.out.println("Upload is $progress% done")
                }

        }

    }
}