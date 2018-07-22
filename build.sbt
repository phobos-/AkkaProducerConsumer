name := "producerConsumer"

version := "1.0"

scalaVersion := "2.12.6"
crossScalaVersions := Seq("2.11.12", "2.12.6")

lazy val akka = project.in(file("akka")).settings(
  commonSettings,
  libraryDependencies ++= commonDependencies ++ Seq(
    dependencies.akka,
    dependencies.akkaTest   % "test"
  )
)

lazy val play = project.in(file("play")).enablePlugins(PlayScala).disablePlugins(PlayLayoutPlugin).settings(
  commonSettings,
  libraryDependencies ++= commonDependencies ++ Seq(
    guice,
    dependencies.playTest % "test"
  )
)

lazy val commonDependencies = Seq(
  dependencies.scalatest  % "test",
  dependencies.scalacheck % "test"
)

lazy val dependencies =
  new {
    val akkaV           = "2.5.14"
    val scalatestV      = "3.0.4"
    val scalacheckV     = "1.13.5"
    val playTestV       = "3.1.2"

    val akka           = "com.typesafe.akka"          %% "akka-actor"              % akkaV
    val akkaTest       = "com.typesafe.akka"          %% "akka-testkit"            % akkaV
    val scalatest      = "org.scalatest"              %% "scalatest"               % scalatestV
    val scalacheck     = "org.scalacheck"             %% "scalacheck"              % scalacheckV
    val playTest       = "org.scalatestplus.play"     %% "scalatestplus-play"      % playTestV
  }

lazy val compilerOptions = Seq(
  "-unchecked",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-deprecation",
  "-encoding",
  "utf8"
)

lazy val commonSettings = Seq(
  scalacOptions ++= compilerOptions,
  wartremoverWarnings in (Compile, compile) ++= Warts.allBut(Wart.Throw),
  scalastyleConfig := file("project/scalastyle-config.xml"),
  scalafmtOnCompile := true,
  scalafmtTestOnCompile := true,
  scalafmtVersion := "1.2.0"
)
