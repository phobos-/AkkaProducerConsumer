name := "ScalaFrameworks"

version := "1.0-SNAPSHOT"

scalaVersion := "2.12.6"
crossScalaVersions := Seq("2.11.12", "2.12.6")

lazy val akka = project.in(file("akka")).settings(
  commonSettings,
  libraryDependencies ++= commonDependencies ++ Seq(
    dependencies.akka,
    dependencies.akkaTest % "test"
  )
)

lazy val play = project.in(file("play")).enablePlugins(PlayScala).disablePlugins(PlayLayoutPlugin).settings(
  commonSettings,
  libraryDependencies ++= commonDependencies ++ Seq(
    guice,
    dependencies.playTest % "test"
  ),
  dependencyOverrides ++= Seq(
    dependencies.akka,
    dependencies.akkaStream,
    dependencies.guava,
    dependencies.combinators
  )
)

lazy val spark = project.in(file("spark")).settings(
  commonSettings,
  scalaVersion := "2.11.12",
  libraryDependencies ++= commonDependencies ++ Seq(
    dependencies.sparkSql
  ),
  dependencyOverrides ++= Seq(
    dependencies.guava,
    dependencies.commonsNet,
    dependencies.netty,
    dependencies.findBugs
  )
)

lazy val puzzles = project.in(file("puzzles")).settings(
  commonSettings,
  libraryDependencies ++= commonDependencies
)

lazy val commonDependencies = Seq(
  dependencies.scalatest  % "test",
  dependencies.scalacheck % "test"
)

lazy val dependencies =
  new {
    val scalaFmtV       = "1.2.0"

    val akkaV           = "2.5.14"
    val scalatestV      = "3.0.4"
    val scalacheckV     = "1.13.5"
    val playTestV       = "3.1.2"
    val sparkV          = "2.3.1"

    val guavaV          = "22.0"
    val combinatorsV    = "1.1.0"
    val commonsNetV     = "3.1"
    val nettyV          = "3.9.9.Final"
    val findBugsV       = "3.0.2"

    val akka           = "com.typesafe.akka"          %% "akka-actor"               % akkaV
    val akkaStream     = "com.typesafe.akka"          %% "akka-stream"              % akkaV
    val akkaTest       = "com.typesafe.akka"          %% "akka-testkit"             % akkaV
    val scalatest      = "org.scalatest"              %% "scalatest"                % scalatestV
    val scalacheck     = "org.scalacheck"             %% "scalacheck"               % scalacheckV
    val playTest       = "org.scalatestplus.play"     %% "scalatestplus-play"       % playTestV
    val combinators    = "org.scala-lang.modules"     %% "scala-parser-combinators" % combinatorsV
    val sparkSql       = "org.apache.spark"           % "spark-sql_2.11"            % sparkV
    val guava          = "com.google.guava"           % "guava"                     % guavaV
    val commonsNet     = "commons-net"                % "commons-net"               % commonsNetV
    val netty          = "io.netty"                   % "netty"                     % nettyV
    val findBugs       = "com.google.code.findbugs"   % "jsr305"                    % findBugsV
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

lazy val notNeededWarts = Seq(
  Wart.Throw,
  Wart.ImplicitParameter,
  Wart.ToString,
  Wart.NonUnitStatements,
  Wart.StringPlusAny,
  Wart.Any,
  Wart.AsInstanceOf,
  Wart.Product,
  Wart.Serializable,
  Wart.Var,
  Wart.DefaultArguments,
  Wart.While,
  Wart.Nothing,
  Wart.Overloading
)

lazy val commonSettings = Seq(
  scalacOptions ++= compilerOptions,
  wartremoverWarnings in (Compile, compile) ++= Warts.allBut(notNeededWarts:_*),
  scalastyleConfig := file("project/scalastyle-config.xml"),
  scalafmtOnCompile := true,
  scalafmtTestOnCompile := true,
  scalafmtVersion := dependencies.scalaFmtV,
  coverageEnabled := true
)
