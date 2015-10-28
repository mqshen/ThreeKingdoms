package org.goldratio.common

import com.typesafe.config.ConfigFactory

/**
 * Created by goldratio on 10/25/15.
 */
object ServerConstants {

  val CROSSDOMAIN = "<cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"*\" /></cross-domain-policy>\u0000".getBytes()

  val compress = true

  val isDevelopmentMode: Boolean = true

  private val root = ConfigFactory.load().getConfig("ThreeKingdoms")

  object DatabaseConfig {

    val (url, user, password) = {
      val databaseConfig = root.getConfig("database.mysql")

      (databaseConfig.getString("url"), databaseConfig.getString("user"), databaseConfig.getString("password"))
    }
  }

}
