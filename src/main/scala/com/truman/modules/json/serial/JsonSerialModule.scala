package com.truman.modules.json.serial

import javax.inject._
import com.google.inject.AbstractModule

import scala.concurrent.Future

import play.api.Play
import play.api.inject.ApplicationLifecycle
import play.api.{ Logger, Environment, Configuration }

import org.msgpack.annotation.Message
import org.msgpack.ScalaMessagePack

trait JsonSerial {
  /**
   * Serialize scala object into bytes Array
   *
   * @param obj The scala object
   * @return The serialized bytes array
   */
  def write(obj: Any): Future[Array[Byte]]
  /**
   * Extract the scala object from bytes Array
   *
   * @param data The serialized bytes array
   * @return The extracted object
   */
  def read[T](data: Array[Byte])(implicit mainfest: Manifest[T]): Future[T]
}

@Singleton
class MsgPackSerial extends JsonSerial {
  override def write(obj: Any): Future[Array[Byte]] = {
    Future.successful(ScalaMessagePack.write(obj))
  }

  override def read[T](data: Array[Byte])
    (implicit manifest : Manifest[T]): Future[T] = {
    Future.successful(ScalaMessagePack.read[T](data))
  }
}

@Singleton
class ProtoBufSerial extends JsonSerial {
  def write(obj: Any): Future[Array[Byte]] = {
    throw new Exception("Have not implemented yet.")
  }

  def read[T](data: Array[Byte])
    (implicit manifest: Manifest[T]): Future[T] = {
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
