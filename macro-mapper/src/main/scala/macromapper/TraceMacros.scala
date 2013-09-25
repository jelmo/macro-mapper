package macromapper

import scala.language.experimental.macros

import scala.reflect.macros.Context

/**
 * User: carlo
 * Date: 25.09.13
 * Time: 09:25
 */
object TraceMacros {
  def trace(param: Any): Unit = macro traceImpl

  def traceImpl(c: Context)(param: c.Expr[Any]): c.Expr[Unit] = {
    import c.universe._
    val paramRep = show(param.tree)
    val paramRepTree = Literal(Constant(paramRep))
    val paramRepExpr = c.Expr[String](paramRepTree)
    reify { println(paramRepExpr.splice + " = " + param.splice) }
  }
}
