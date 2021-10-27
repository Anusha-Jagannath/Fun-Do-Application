package model

data class UserDetails(val userName:String,val email:String,var loginStatus:Boolean = false) {
}