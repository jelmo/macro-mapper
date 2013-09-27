package macromapper.tests

import macromapper.{Person, PersonDTO, MacroMapper}

/**
 * User: carlo
 */
object Mapping {
  val mapper = MacroMapper[Person, PersonDTO]
  //      .to(_.name, _.name).to(_.address, _.address)

}


//case class Person(name: String, age: Int, address: String, whatever: Boolean)
//
//case class PersonDTO(name: String, age: Int, address: String, whatever: Boolean)
