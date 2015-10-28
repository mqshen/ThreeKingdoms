package org.goldratio.game.user

/**
 * Created by goldratio on 10/26/15.
 */

import org.goldratio.game.BasicTemplate

import scala.slick.driver.MySQLDriver.simple._

case class User(id: Long, userName: String, password: String, activate: Int, activateCode: String, adult: Int, rewardForce: Int)

class UserTable(tag: Tag) extends Table[User](tag, "user") with BasicTemplate {
  def userName = column[String]("USER_NAME")
  def password = column[String]("PASSWORD")
  def activate = column[Int]("ACTIVATE")
  def activateCode = column[String]("ACTIVATE_CODE")
  def adult = column[Int]("ADULT")
  def rewardForce = column[Int]("REWARD_FORCE")

  def * = (id, userName, password, activate, activateCode, adult, rewardForce) <> (User.tupled, User.unapply _)

}

object UserService {

  val users = TableQuery[UserTable]

  def create(user: User)(implicit s: Session) {
    users.insert(user)
  }

  def login(name: String, password: String)(implicit s: Session): Option[User] = {
    users.filter(_.userName === name).filter(_.password === password).firstOption
  }
}
