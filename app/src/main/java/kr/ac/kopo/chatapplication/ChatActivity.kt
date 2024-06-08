package kr.ac.kopo.chatapplication

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kr.ac.kopo.chatapplication.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    private lateinit var receiverName:String
    private lateinit var receiverUid:String
    
    //바인딩 객체
    private lateinit var binding: ActivityChatBinding
    
    lateinit var mAuth:FirebaseAuth //인증 객체
    lateinit var mDbRef:DatabaseReference //DB 객체
    
    private lateinit var receiverRoom:String //받는 대화방
    private lateinit var senderRoom:String  //보낸 대화방


    private lateinit var messageList:ArrayList<Message>
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // 초기화
        messageList = ArrayList()
        val messageAdapter :MessageAdapter = MessageAdapter(this,messageList)

        //RecyclerView
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatRecyclerView.adapter =messageAdapter

        //넘어온 데이터 변수에 담기
        receiverName = intent.getStringExtra("name").toString()
        receiverUid = intent.getStringExtra("uId").toString()

        mAuth = FirebaseAuth.getInstance()
        mDbRef =FirebaseDatabase.getInstance().reference

        //접속자 Uid
        val senderUid = mAuth.currentUser?.uid

        //보낸이방
        senderRoom = receiverUid + senderUid

        //받는이방
        receiverRoom = senderUid + receiverUid

        //액션바에 상대방 이름 보여주기
        supportActionBar?.title = receiverName
        
        //메세지 전종 버튼 이벤트
        binding.sendBtn.setOnClickListener { 

            val message = binding.messageEdit.text.toString()
            val messageObject =Message(message,senderUid)

            //데이터 저장
            mDbRef.child("chats").child(senderRoom).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    //저장 성공하면
                    mDbRef.child("chats").child(receiverRoom).child("messages").push()
                        .setValue(messageObject)
                }
            //입력값 부분 초기화
            binding.messageEdit.setText("")
        }

        //메세지 가져오기
        mDbRef.child("chats").child(senderRoom).child("messages")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    //초기화
                    messageList.clear()
                    for (postSnapshat in snapshot.children){
                        val message = postSnapshat.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    //적용
                    messageAdapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {

                        

                }

            })

    }
}