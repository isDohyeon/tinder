package hnu.multimedia.tinder.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.ktx.storage
import hnu.multimedia.tinder.MainActivity
import hnu.multimedia.tinder.databinding.ActivityJoinBinding
import hnu.multimedia.tinder.util.FirebaseRef

class JoinActivity : AppCompatActivity() {

    private val binding by lazy { ActivityJoinBinding.inflate(layoutInflater) }
    private var uri: Uri = Uri.EMPTY
    private var isPhotoAvailable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonJoin2.setOnClickListener {
            val email = binding.editTextID.text.toString()
            val password = binding.editTextPassword.text.toString()
            val passwordCheck = binding.editTextPasswordCheck.text.toString()
            val sex = binding.editTextSex.text.toString()
            val age = binding.editTextAge.text.toString()
            val nickname = binding.editTextNickname.text.toString()

            if (validateInfo(email, password, passwordCheck, sex, age, nickname)) {
                createUser(email, password)
            }
        }

        val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { _uri ->
            _uri?.let {
                binding.imageViewPhoto.setImageURI(_uri)
                uri = _uri
                isPhotoAvailable = true
            }
        }

        binding.imageViewPhoto.setOnClickListener{
            launcher.launch("image/*")
        }
    }

    private fun validateInfo(
        email: String,
        password: String,
        passwordCheck: String,
        sex: String,
        age: String,
        nickname: String
    ) : Boolean {
        if (email.isEmpty()) {
            Snackbar.make(binding.root, "이메일을 입력해주세요.", Snackbar.LENGTH_LONG).show()
            return false
        }
        if (password.isEmpty()) {
            Snackbar.make(binding.root, "패스워드를 입력해주세요.", Snackbar.LENGTH_LONG).show()
            return false
        }
        if (password != passwordCheck) {
            Snackbar.make(binding.root, "패스워드를 동일하게 입력해주세요.", Snackbar.LENGTH_LONG).show()
            return false
        }
        if (sex != "W" && sex != "M") {
            Snackbar.make(binding.root, "성별을 M 혹은 W로 입력해주세요.", Snackbar.LENGTH_LONG).show()
            return false
        }
        if (!isPhotoAvailable) {
            Snackbar.make(binding.root, "사진을 등록해주세요.", Snackbar.LENGTH_LONG).show()
            return false
        }
        if (age.isEmpty()) {
            Snackbar.make(binding.root, "나이를 입력해주세요.", Snackbar.LENGTH_LONG).show()
            return false
        }
        if (nickname.isEmpty()) {
            Snackbar.make(binding.root, "닉네임을 입력해주세요.", Snackbar.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun createUser(email: String, password: String) {
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val uid = Firebase.auth.currentUser?.uid ?: ""
                    saveUser(uid)
                    FirebaseMessaging.getInstance().token.addOnCompleteListener {
                        if (!it.isSuccessful) {
                            Log.d("JoinActivity", "Fetching FCM token failed: ${it.exception}")
                            return@addOnCompleteListener
                        }
                        val fcmToken = it.result
                        FirebaseRef.fcmTokens.child(uid).setValue(fcmToken)
                    }
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

    private fun saveUser(uid: String) {
        val nickName = binding.editTextNickname.text.toString()
        val sex = binding.editTextSex.text.toString()
        val city = binding.editTextArea.text.toString()
        val age = binding.editTextAge.text.toString().toInt()
        val userModel = UserModel(uid, nickName, age, sex, city)
        FirebaseRef.users.child(uid).setValue(userModel)
        if (uri != Uri.EMPTY) {
            Firebase.storage.reference.child("$uid.jpg").putFile(uri)
        }
    }
}