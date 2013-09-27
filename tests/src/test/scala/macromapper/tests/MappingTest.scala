package macromapper.tests

import org.specs2.mutable.SpecificationWithJUnit
import macromapper.{PersonDTO, Person}

class MappingTest extends SpecificationWithJUnit {
  "Macro Mapper" should {
    "create toString representation" in {
      Mapping.mapper.toString === "MacroMapper[source:Person with fields {name,age,address,whatever}, target:PersonDTO with fields {name,age,address,whatever}]"
    }

    "convert" in {
      Mapping.mapper.convert(Person("Carlo", 43, "Crissier", whatever = true)) === PersonDTO("Carlo",43,"Crissier",whatever = true)
    }
  }
}
