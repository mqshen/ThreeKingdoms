import java.io.File

import sbt._
import sbt.Keys._
import com.typesafe.sbt.SbtMultiJvm
import com.typesafe.sbt.SbtMultiJvm.MultiJvmKeys.MultiJvm
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import scala.sys.process._

object Build extends Build {

  lazy val root = Project("ThreeKingdoms", file("."))
    .aggregate(framework, core, gateway, server, game)
    .settings(basicSettings: _*)
    .settings(noPublishing: _*)
    .settings(XitrumPackage.skip: _*)

  lazy val framework = Project("framework", file("framework"))
    .settings(basicSettings: _*)
    .settings(formatSettings: _*)
    .settings(releaseSettings: _*)
    .settings(SbtMultiJvm.multiJvmSettings ++ multiJvmSettings: _*)
    .settings(libraryDependencies ++= Dependencies.all )
    .settings(unmanagedSourceDirectories in Test += baseDirectory.value / "multi-jvm/scala")
    .settings(XitrumPackage.skip: _*)
    .configs(MultiJvm)
    .dependsOn(core)

  lazy val core = Project("core", file("core"))
    .settings(basicSettings: _*)
    .settings(formatSettings: _*)
    .settings(releaseSettings: _*)
    .settings(SbtMultiJvm.multiJvmSettings ++ multiJvmSettings: _*)
    .settings(libraryDependencies ++= Dependencies.all )
    .settings(unmanagedSourceDirectories in Test += baseDirectory.value / "multi-jvm/scala")
    .settings(XitrumPackage.skip: _*)
    .configs(MultiJvm)

  lazy val gateway = Project("gateway", file("gateway"))
    .settings(basicSettings: _*)
    .settings(formatSettings: _*)
    .settings(releaseSettings: _*)
    .settings(SbtMultiJvm.multiJvmSettings ++ multiJvmSettings: _*)
    .settings(libraryDependencies ++= Dependencies.all )
    .settings(unmanagedSourceDirectories in Test += baseDirectory.value / "multi-jvm/scala")
    .settings(XitrumPackage.skip: _*)
    .configs(MultiJvm)
    .dependsOn(core)

  lazy val server = Project("server", file("server"))
    .settings(basicSettings: _*)
    .settings(formatSettings: _*)
    .settings(releaseSettings: _*)
    .settings(SbtMultiJvm.multiJvmSettings ++ multiJvmSettings: _*)
    .settings(libraryDependencies ++= Dependencies.all )
    .settings(unmanagedSourceDirectories in Test += baseDirectory.value / "multi-jvm/scala")
    .settings(XitrumPackage.skip: _*)
    .configs(MultiJvm)
    .dependsOn(core)

  lazy val game = Project("game", file("game"))
    .settings(basicSettings: _*)
    .settings(formatSettings: _*)
    .settings(releaseSettings: _*)
    .settings(SbtMultiJvm.multiJvmSettings ++ multiJvmSettings: _*)
    .settings(libraryDependencies ++= (Dependencies.all ++ Dependencies.game))
    .settings(unmanagedSourceDirectories in Test += baseDirectory.value / "multi-jvm/scala")
    .settings(XitrumPackage.skip: _*)
    .configs(MultiJvm)
    .dependsOn(core, server)

  lazy val noPublishing = Seq(
    publish := (),
    publishLocal := (),
    // required until these tickets are closed https://github.com/sbt/sbt-pgp/issues/42,
    // https://github.com/sbt/sbt-pgp/issues/36
    publishTo := None
  )

  def multiJvmSettings = Seq(
    // make sure that MultiJvm test are compiled by the default test compilation
    compile in MultiJvm <<= (compile in MultiJvm) triggeredBy (compile in Test),
    // disable parallel tests
    parallelExecution in Test := false,
    // make sure that MultiJvm tests are executed by the default test target,
    // and combine the results from ordinary test and multi-jvm tests
    executeTests in Test <<= (executeTests in Test, executeTests in MultiJvm) map {
      case (testResults, multiNodeResults) =>
        val overall =
          if (testResults.overall.id < multiNodeResults.overall.id)
            multiNodeResults.overall
          else
            testResults.overall
        Tests.Output(overall,
          testResults.events ++ multiNodeResults.events,
          testResults.summaries ++ multiNodeResults.summaries)
    })

  lazy val basicSettings = Seq(
    organization := "com.ynet",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "2.11.2",
    crossScalaVersions := Seq("2.10.4", "2.11.2"),
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-encoding", "UTF-8"),
    javacOptions ++= Seq("-encoding", "UTF-8"),
    resolvers ++= Seq(
      "Local Maven Repository" at "file:///"+ Path.userHome.absolutePath + "/.m2/repository",
      "maven1" at "http://repo1.maven.org/maven2",
      "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases",
      "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
      "Typesafe repo" at "http://repo.typesafe.com/typesafe/releases/",
      "spray" at "http://repo.spray.io",
      "spray nightly" at "http://nightlies.spray.io/",
      "Local Sonatype OSS Releases" at "http://101.251.195.186:48081/nexus/content/repositories/releases",
      "Local Sonatype OSS Snapshots" at "http://101.251.195.186:48081/nexus/content/repositories/snapshots",
      "krasserm at bintray" at "http://dl.bintray.com/krasserm/maven")
  )

  lazy val releaseSettings = Seq(
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (version.value.trim.endsWith("SNAPSHOT"))
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases"  at nexus + "service/local/staging/deploy/maven2")
    },
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { (repo: MavenRepository) => false },
    pomExtra := (
      <url>https://github.com/mqshen/</url>
        <licenses>
          <license>
            <name>The Apache Software License, Version 2.0</name>
            <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
            <distribution>repo</distribution>
          </license>
        </licenses>
        <scm>
          <url>git@github.com:mqshen/</url>
          <connection>scm:git:git@github.com:</connection>
        </scm>
        <developers>
          <developer>
            <id>mqshen</id>
            <name>miaoqi shen</name>
            <email>goldratio87@gmail.com</email>
          </developer>
        </developers>
      )
  )

  lazy val formatSettings = SbtScalariform.scalariformSettings ++ Seq(
    ScalariformKeys.preferences in Compile := formattingPreferences,
    ScalariformKeys.preferences in Test := formattingPreferences)

  import scalariform.formatter.preferences._
  def formattingPreferences =
    FormattingPreferences()
      .setPreference(RewriteArrowSymbols, false)
      .setPreference(AlignParameters, true)
      .setPreference(AlignSingleLineCaseStatements, true)
      .setPreference(DoubleIndentClassDeclaration, true)
      .setPreference(IndentSpaces, 2)

}

object Dependencies {

  val AKKA_VERSION = "2.3.12"
  val AKKA_STREAM_VERSION = "1.0"
  val slickVersion = "2.1.0"

  val akka = Seq(
    "com.typesafe.akka" %% "akka-actor" % AKKA_VERSION,
    "com.typesafe.akka" %% "akka-contrib" % AKKA_VERSION,
    "com.typesafe.akka" %% "akka-persistence-experimental" % AKKA_VERSION exclude ("org.iq80.leveldb", "leveldb"),
    "com.typesafe.akka" %% "akka-slf4j" % AKKA_VERSION,
    "com.typesafe.akka" %% "akka-testkit" % AKKA_VERSION % Test,
    "com.typesafe.akka" %% "akka-multi-node-testkit" % AKKA_VERSION % Test,
    "org.iq80.leveldb" % "leveldb" % "0.7" % Runtime,
    "com.github.krasserm" %% "akka-persistence-cassandra" % "0.3.6" % Runtime)


  val all = Seq(
    "org.json4s" %% "json4s-jackson" % "3.2.10",
    "commons-io" % "commons-io" % "2.4",
    "commons-lang" % "commons-lang" % "2.6",
    "commons-dbcp" % "commons-dbcp" % "1.4",
    "org.apache.commons" % "commons-compress" % "1.5",
    "org.apache.commons" % "commons-email" % "1.3.1",
    "commons-fileupload" % "commons-fileupload" % "1.3.1",
    "org.apache.httpcomponents" % "httpclient" % "4.3",
    "org.apache.sshd" % "apache-sshd" % "0.11.0",
    "mysql" % "mysql-connector-java" % "5.1.29",
    "net.debasishg" %% "redisclient" % "2.13",
    "org.hibernate.javax.persistence" % "hibernate-jpa-2.0-api" % "1.0.1.Final",
    "org.slf4j" % "slf4j-log4j12" % "1.7.7",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
    "javax.servlet" % "javax.servlet-api" % "3.1.0",
    "junit" % "junit" % "4.11" % "test",
    "log4j" % "log4j" % "1.2.17",
    "com.thoughtworks.xstream" % "xstream" % "1.4.8",
    "com.google.zxing" % "javase" % "3.2.0",
    "com.typesafe.slick" %% "slick" % slickVersion,
    "org.reflections" % "reflections" % "0.9.10",
    "xerces" % "xercesImpl" % "2.11.0"
  ).map(_.exclude("org.slf4j", "slf4j-jdk14")) ++ akka

  val game = Seq(
  )
}
