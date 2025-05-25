package hnu.multimedia.tinder.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hnu.multimedia.tinder.MainActivity
import hnu.multimedia.tinder.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {

    private val binding by lazy { ActivityIntroBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonJoin.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.buttonLogin.setOnClickListener {

            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (email.isEmpty()) {
                Snackbar.make(binding.root, "이메일을 입력해주세요.", Snackbar.LENGTH_LONG).show()
            } else if (password.isEmpty()) {
                Snackbar.make(binding.root, "패스워드를 입력해주세요.", Snackbar.LENGTH_LONG).show()
            } else {
                Firebase.auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Snackbar.make(binding.root, "로그인에 실패했습니다", Snackbar.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }
}