package kr.ac.kopo.chatapplication

data class Message(
    val message: String?,
    var sendId: String?
){
    constructor():this("","")
}
