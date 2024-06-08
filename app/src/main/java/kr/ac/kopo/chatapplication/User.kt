package kr.ac.kopo.chatapplication

//데이터클래스 User 안에 사용자정보가 들어갈것임.
data class User(
    var name: String,
    val email:String,
    var uId:String
){
    constructor(): this("","","")
}
