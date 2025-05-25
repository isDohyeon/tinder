package hnu.multimedia.tinder.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hnu.multimedia.tinder.MainActivity
import hnu.multimedia.tinder.databinding.ActivityJoinBinding

class JoinActivity : AppCompatActivity() {

    private val binding by lazy { ActivityJoinBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonJoin2.setOnClickListener {
            val email = binding.editTextID.text.toString()
            val password = binding.editTextPassword.text.toString()
            val passwordCheck = binding.editTextPasswordCheck.text.toString()
            val sex = binding.editTextSex.text.toString()

            if (password != passwordCheck) {
                Snackbar.make(binding.root, "패스워드를 동일하게 입력해주세요.", Snackbar.LENGTH_LONG).show()
            } else if (sex != "W" && sex != "M") {
                Snackbar.make(binding.root, "성별을 M 혹은 W로 입력해주세요.", Snackbar.LENGTH_LONG).show()
            } else {
                Firebase.auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener{
                        if (it.isSuccessful) {
                            Snackbar.make(binding.root, "회원가입 성공!.", Snackbar.LENGTH_LONG)
                                .show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Snackbar.make(binding.root, "회원가입 실패", Snackbar.LENGTH_LONG)
                                .show()
                        }
                    }
            }
        }
    }
}