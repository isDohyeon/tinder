package hnu.multimedia.tinder.mypage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import hnu.multimedia.tinder.databinding.ActivityJoinBinding
import hnu.multimedia.tinder.util.MyData

class MyPageActivity : AppCompatActivity() {

    private val binding by lazy { ActivityJoinBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupUI()
        bindUserData()
    }

    private fun bindUserData() {
        binding.editTextAge.setText(MyData.userModel.age.toString())
        binding.editTextSex.setText(MyData.userModel.sex)
        binding.editTextArea.setText(MyData.userModel.city)
        binding.editTextNickname.setText(MyData.userModel.nickName)
        binding.editTextID.setText(MyData.email)
        Glide.with(this)
            .load(MyData.photoUri)
            .into(binding.imageViewPhoto)
    }

    private fun setupUI() {
        binding.buttonJoin2.isVisible = false
        binding.textInputLayoutPassword.isVisible = false
        binding.textInputLayoutPasswordCheck.isVisible = false

        binding.editTextID.isEnabled = false
        binding.editTextAge.isEnabled = false
        binding.editTextArea.isEnabled = false
        binding.editTextNickname.isEnabled = false
        binding.editTextSex.isEnabled = false
    }
}