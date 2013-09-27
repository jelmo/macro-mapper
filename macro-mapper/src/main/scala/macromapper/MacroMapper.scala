package macromapper

import scala.language.experimental.macros
import scala.reflect.macros.Context

case class Person(name: String, age: Int, address: String, whatever: Boolean)

case class PersonDTO(name: String, age: Int, address: String, whatever: Boolean)

class MacroMapper[A, B] /*extends (A => B)*/ {
  override def toString = macro MacroMapper.toStringImpl[A, B]

  def convert(a: Person): PersonDTO = macro MacroMapper.convertImpl

//  def apply(input: A): B = macro MacroMapper.mapperApplyImpl[A,B]

  def to(f1: A => Any, f2: B => Any): this.type = ???
}

object MacroMapper {
  def apply[A, B]: MacroMapper[A,B] = macro applyImpl[A, B]

  def applyImpl[A: c.WeakTypeTag, B: c.WeakTypeTag](c: Context): c.Expr[MacroMapper[A, B]] = {
    import c.universe._
    reify {
      new MacroMapper[A, B]
    }
  }

  def convertImpl(c: Context)(a: c.Expr[Person]): c.Expr[PersonDTO] = {
    import c.universe._

    val res = reify {
      val p = a.splice
      PersonDTO(p.name, p.age, p.address, p.whatever)
    }

    println(showRaw(res))

    res
  }

//  def mapperApplyImpl[A: c.WeakTypeTag, B: c.WeakTypeTag](c: Context)(a: c.Expr[A]): c.Expr[B] = {
//    import c.universe._
//
//    val p = new Person("Carlo Jelmini", 43, "1023 Crissier", whatever = true)
//
//    reify {
//      PersonDTO(p.name, p.age, p.address, p.whatever)
//    }
//
//  }

  def toStringImpl[A: c.WeakTypeTag, B: c.WeakTypeTag](c: Context): c.Expr[String] = {
    import c.universe._

    def classType[T: c.WeakTypeTag] = weakTypeTag[T].tpe
    def className(tpe: Type) = tpe.typeSymbol.name.toString
    def allFields(tpe: Type) = tpe.declarations.filterNot(d => d.isMethod).map(_.name.decoded.trim).mkString(",")

    val output = s"MacroMapper[source:${className(classType[A])} with fields {${allFields(classType[A])}}, " +
      s"target:${className(classType[B])} with fields {${allFields(classType[A])}}]"
    c.literal(show(output))
  }

}