package com.truman.modules.json.serial

import play.api._
import play.api.test._
import play.api.libs.json._

import javax.inject._

import scala.concurrent.Await
import scala.concurrent.duration._

import JsonSerialScope._

class JsonSerialSpec extends PlaySpecification {

  "A JSON Serialization specification" should {
    "Normal write and read" in new WithApplication {

      val jserial = app.injector.instanceOf[JsonSerial]

      val user = new User
      user.id = 439
      user.name = "sizuru"
      user.roles = Array("user")
      user.profile.gender = 2

      val data = Await.result(jserial.write(user), 10.millis)
      data must not be empty

      for(b <- data){
        print("%02x ".format(b))
      }
      println("")
      println("The length of msgpacked bytes is : " + data.length)

      val json = Json.toJson(user)
      println("The length of JSON String bytes is : " + json.toString.getBytes.length)

      val recover = Await.result(jserial.read[User](data), 10.millis)

      recover.id must_== user.id
      recover.name must_== user.name
      recover.roles.toList must_== user.roles.toList
      recover.profile.gender must_== user.profile.gender
    }
  }
}
