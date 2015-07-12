package com.truman.modules.json.serial

import javax.inject._
import com.google.inject.AbstractModule

import play.api.Play
import play.api.inject.ApplicationLifecycle
import play.api.{ Logger, Environment, Configuration }

import org.msgpack.annotation.Message
import org.msgpack.ScalaMessagePack

trait JsonSerial {
  def write(obj: Any): Array[Byte]
  def read[T](data: Array[Byte])(implicit mainfest: Manifest[T]): T
}

class MsgPackSerial extends JsonSerial {
  def write(obj: Any): Array[Byte] = {
    ScalaMessagePack.write(obj)
  }
  def read[T](data: Array[Byte])
    (implicit manifest : Manifest[T]): T = {
    ScalaMessagePack.read[T](data)
  }
}

class ProtoBufSerial extends JsonSerial {
  def write(obj: Any): Array[Byte] = {
    throw new Exception("Have not implemented yet.")
  }

  def read[T](data: Array[Byte])
    (implicit manifest: Manifest[T]): T = {
    throw new Exception("Have not implemented yet.")
  }
}

class JsonSerialModule(
  environment: Environment,
  configuration: Configuration) extends AbstractModule {
  def configure() = {
    val isEnabledJsonSerial: Boolean =
          configuration.getBoolean("ms.module.json.serial.enabled")
            .getOrElse(true)
    if (isEnabledJsonSerial) {
      val typeOfJsonSerialOpt: Option[String] =
            configuration.getString(
              "ms.module.json.serial.mode",
              Some(Set("msgpack", "protobuf")))
      typeOfJsonSerialOpt match {
        case Some("msgpack") => {
          bind(classOf[JsonSerial])
            .to(classOf[MsgPackSerial])
            .asEagerSingleton
          Logger.info("Bind MsgPack as JSON default serial module.")
        }
        case Some("protobuf") => {
          bind(classOf[JsonSerial])
            .to(classOf[ProtoBufSerial])
            .asEagerSingleton
          Logger.info("Bind Google Protocl Buffer as JSON default serial module.")
        }
        case _ => {
          Logger.warn("None valid json mode has been assgiend.")
        }
      }
    }
  }
}
