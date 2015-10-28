package org.goldratio

import scala.collection.mutable.ListBuffer

/**
 * Created by goldratio on 10/27/15.
 */
object XMLTest extends App {

  val xml = <resultMap id="PlayerResourceAdditionResultMap" type="PlayerResourceAddition">
              <id column="V_ID" property="vId"/>
              <result column="PLAYER_ID" property="playerId"/>
              <result column="RESOURCE_TYPE" property="resourceType"/>
              <result column="ADDITION_MODE" property="additionMode"/>
              <result column="END_TIME" property="endTime"/>
              <result column="TIME_TYPE" property="timeType"/>
              <result column="TASK_ID" property="taskId"/>
            </resultMap>

  val tableName = xml.attribute("type").get.text
  println(tableName)

  var id = ("", "")

  val columns = ListBuffer[(String, String)]()

  xml.child.foreach { node =>
    if (node.label == "id") {
      id = (node.attribute("column").get.text, node.attribute("property").get.text)
    } else if (node.label == "result") {
      columns += ((node.attribute("column").get.text, node.attribute("property").get.text))
    }
  }
  println(id)
  columns.foreach(println(_))

  println("case class " + tableName + "(")
  println("id: Long")
  columns.foreach { column =>
    println(", " + column._2 + ": String")
  }
  println(")\n")

  print("class UserTable(tag: Tag) extends Table[")
  print(tableName)
  println(s"""](tag, "$tableName") with BasicTemplate {""")

  columns.foreach { column =>
    println(s"""def  ${column._2} = column[String]("${column._1}")""")
  }

  print("def * = (id")
  columns.foreach { column =>
    print(s", ${column._2}")
  }

  println(s") <> ($tableName.tupled, $tableName.unapply _)")
  println("}")
}
