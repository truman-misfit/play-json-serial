name := """play-json-serial"""

version := "0.1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "com.typesafe.play" % "play_2.11" % "2.4.2",
  "com.typesafe.play" % "play-json_2.11" % "2.4.2",
  "com.typesafe.play" % "play-test_2.11" % "2.4.2",
  "com.typesafe.play" % "play-specs2_2.11" % "2.4.2",
  "org.specs2" % "specs2_2.11" % "3.3.1",
  "org.msgpack" %% "msgpack-scala" % "0.6.11"
)
