package macromapper

import scala.language.experimental.macros
import scala.reflect.macros.Context

class MacroMapper[A, B] {
  override def toString = macro MacroMapper.toStringImpl[A, B]

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

  def toStringImpl[A: c.WeakTypeTag, B: c.WeakTypeTag](c: Context): c.Expr[String] = {
    import c.universe._

    reify {
      s"MacroMapper between ${classNameExpr[A](c).splice} and ${classNameExpr[B](c).splice}"
    }
  }

  private def classNameExpr[T: c.WeakTypeTag](c: Context) = {
    import c.universe._
    val repr = weakTypeTag[T].tpe.typeSymbol.name.toString
    c.literal(show(repr))
  }
}