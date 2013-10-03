package macromapper

import scala.language.experimental.macros
import scala.reflect.macros.Context

case class Person(name: String, age: Int, address: String, whatever: Boolean)

case class PersonDTO(age: Int, name: String, whatever: Boolean)

class MacroMapper[A, B] /*extends (A => B)*/ {
  override def toString = macro MacroMapper.toStringImpl[A, B]

  def apply(input: A): B = macro MacroMapper.convertImpl[A,B]

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

  def convertImpl[A: c.WeakTypeTag, B: c.WeakTypeTag](c: Context)(input: c.Expr[A]): c.Expr[B] = {
    import c.universe._
    val valName = newTermName(c.fresh("source$"))
    val assignment = List(ValDef(Modifiers(), valName, TypeTree(), input.tree))

    def allFields(tpe: Type) = tpe.declarations.filterNot(d => d.isMethod).map(_.name.decoded.trim)

    val sourceFields = allFields(weakTypeTag[A].tpe).toSet
    val targetFields = allFields(weakTypeTag[B].tpe).toList.filter(sourceFields)

    val fieldMappings = targetFields.map(name => Select(Ident(valName), newTermName(name)))

    val factoryCall = Select(Ident(weakTypeTag[B].tpe.typeSymbol.companionSymbol), newTermName("apply"))
    val mapping = Apply(factoryCall, fieldMappings)

    val res = c.Expr[B](Block(assignment, mapping))

//    println(showRaw(res))

    res
  }

  def toStringImpl[A: c.WeakTypeTag, B: c.WeakTypeTag](c: Context): c.Expr[String] = {
    import c.universe._

    def classType[T: c.WeakTypeTag] = weakTypeTag[T].tpe
    def className(tpe: Type) = tpe.typeSymbol.name.toString
    def allFields(tpe: Type) = tpe.declarations.filterNot(d => d.isMethod).map(_.name.decoded.trim).mkString(",")

    val output = s"MacroMapper[source:${className(classType[A])} with fields {${allFields(classType[A])}}, " +
      s"target:${className(classType[B])} with fields {${allFields(classType[B])}}]"
    c.literal(show(output))
  }

}

class Helper[C <: Context](val c: C) {


}