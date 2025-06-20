package hnu.multimedia.tinder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.ktx.Firebase
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import hnu.multimedia.tinder.auth.IntroActivity
import hnu.multimedia.tinder.auth.UserModel
import hnu.multimedia.tinder.databinding.ActivityMainBinding
import hnu.multimedia.tinder.messages.LikesActivity
import hnu.multimedia.tinder.messages.MessagesActivity
import hnu.multimedia.tinder.mypage.MyPageActivity
import hnu.multimedia.tinder.util.FirebaseRef
import hnu.multimedia.tinder.util.MyData
import hnu.multimedia.tinder.util.MyFirebaseMessagingSender
import hnu.multimedia.tinder.util.NotificationUtil

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val users = mutableListOf<UserModel>()
    private var swipeCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val layoutManager = CardStackLayoutManager(baseContext, object : CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {}
            override fun onCardSwiped(direction: Direction?) {
                if (direction == Direction.Right) {
                    val likeUid = users[swipeCount].uid
                    FirebaseRef.likes.child(FirebaseRef.currentUserId).child(likeUid).setValue(true)
                    likeMe(likeUid, users[swipeCount].nickName)
                }

                swipeCount++
                if (swipeCount == users.size) {
                    getUsers()
                    swipeCount = 0
                    Snackbar.make(binding.root, "사용자 목록이 새로 갱신되었습니다", Snackbar.LENGTH_SHORT).show()
                }
            }
            override fun onCardRewound() {}
            override fun onCardCanceled() {}
            override fun onCardAppeared(view: View?, position: Int) {}
            override fun onCardDisappeared(view: View?, position: Int) {}
        })

        getUsers()
        FirebaseRef.initCUid()

        binding.cardStackView.layoutManager = layoutManager
        binding.cardStackView.adapter = CardStackAdapter(users)

        binding.imageViewLogout.setOnClickListener{
            Firebase.auth.signOut()
            val intent = Intent(this, IntroActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.imageViewMyPage.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

        binding.imageViewLikes.setOnClickListener {
            val intent = Intent(this, LikesActivity::class.java)
            startActivity(intent)
        }

        binding.imageViewMessages.setOnClickListener {
            val intent = Intent(this, MessagesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun likeMe(likeUid: String, nickname: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.getValue<Boolean>() == true) {
                    Snackbar.make(binding.root, "매칭되었습니다!", Snackbar.LENGTH_SHORT).show()
                    NotificationUtil.createNotification(baseContext, "매칭되었어요!!", "${nickname}님이 나를 좋아합니다!")
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        FirebaseRef.likes.child(likeUid).child(FirebaseRef.currentUserId).addValueEventListener(postListener)
    }

    private fun getUsers() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                users.clear()
                for (dataModel in snapshot.children) {
                    val value = dataModel.getValue(UserModel::class.java)
                    value?.let {
                        if (value.sex != MyData.userModel.sex) {
                            users.add(value)
                        }
                    }
                }
                binding.cardStackView.adapter?.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        FirebaseRef.users.addValueEventListener(postListener)
    }
}