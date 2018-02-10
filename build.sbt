enablePlugins(ScalaJSPlugin)

name := "Calder"
scalaVersion := "2.12.2"

libraryDependencies += "org.scala-js"  %%% "scalajs-dom" % "0.9.1"
//libraryDependencies += "org.scalactic" %%% "scalactic"   % "3.0.4"
libraryDependencies += "org.scalatest" %%% "scalatest"   % "3.0.0" % "test"

//scalaJSStage in Global := FastOptStage
//scalaJSUseMainModuleInitializer := true
