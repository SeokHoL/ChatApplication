package kr.ac.kopo.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kr.ac.kopo.chatapplication.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var  binding : ActivityLoginBinding


    lateinit var  mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityLoginBinding.inflate(layoutInflater) //객체초기화
        setContentView(binding.root)


        //인증 초기화
        mAuth =  Firebase.auth
        
        //로그인 버튼 이벤트
        binding.loginBtn.setOnClickListener {

            val email = binding.emailEdit.text.toString()
            val password = binding.passwordEdit.text.toString()

            login(email, password)
            
        }


        //회원가입 버튼 이벤트
        binding.signUpBtn.setOnClickListener {
            val intent:Intent =Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

    }
    //로그인
    private fun login(email: String, password: String){
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //성공시 실행
                    val intent : Intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this,"로그인 성공",Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    //실패시 실행
                    Toast.makeText(this,"로그인 실패",Toast.LENGTH_SHORT).show()
                    Log.d("Login","Error:${task.exception}")

                }
            }


    }

}
