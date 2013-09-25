package macromapper.tests

import macromapper.MacroMapper

/**
 * User: carlo
 */
object Mapping {
  def main(args: Array[String]) {
    val mapper = MacroMapper[Person, PersonDTO].
      to(_.name, _.name).to(_.address, _.address)

    println(mapper.toString)
  }
}


case class Person(name: String, age: Int, address: String, whatever: Boolean)

case class PersonDTO(name: String, age: Int, address: String, whatever: Boolean)
