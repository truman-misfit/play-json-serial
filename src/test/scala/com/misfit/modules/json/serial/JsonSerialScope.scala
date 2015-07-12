package com.truman.modules.json.serial

import play.api.libs.json._

import org.msgpack.annotation.{Message, MessagePackMessage}

object JsonSerialScope {

  implicit lazy val ProfileFormat = Json.format[Profile]
  implicit lazy val UserFormat = Json.format[User]

  @Message
  case class User(
    var id : Long = 0,
    var name : String = "",
    var roles : Array[String] = Array("admin","user"),
    var profile : Profile = new Profile ){
    def this() = this(0, "", Array("admin", "user"), Profile(1))
  }

  @Message
  case class Profile(var gender : Int = 1) {
    def this() = this(1)
  }
}
