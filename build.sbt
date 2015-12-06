organization := "br.edu.ufabc.ufabcapi"

version := "0.1"

scalaVersion := "2.11.7"

libraryDependencies ++= {
  val akkaV = "2.4.1"
  val sprayV = "1.3.3"
  Seq(
    "io.spray"            %%  "spray-can"     % sprayV,
    "io.spray"            %%  "spray-routing" % sprayV,
    "io.spray"            %%  "spray-json"    % "1.3.2",
    "io.spray"            %%  "spray-httpx"   % sprayV,
    "io.spray"            %%  "spray-http"   % sprayV,
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
    "org.json4s"          %% "json4s-native" % "3.3.0"
  )
}


