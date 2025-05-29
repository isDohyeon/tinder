package hnu.multimedia.tinder.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hnu.multimedia.tinder.MainActivity
import hnu.multimedia.tinder.R
import hnu.multimedia.tinder.util.MyData

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        MyData.init()

        if (Firebase.auth.currentUser?.uid == null) {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, IntroActivity::class.java))
                finish()
            }, 2000)
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, 2000)

        }
    }
}