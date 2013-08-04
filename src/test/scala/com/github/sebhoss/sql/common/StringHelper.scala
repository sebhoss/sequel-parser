package com.github.sebhoss.sql.common

object StringHelper {

  def flatten(elements: Traversable[Traversable[String]]) =
    for (inner <- elements) yield (inner.mkString(" "))

  def powerset(elements: String*) =
    flatten(elements.toSet.subsets.toSet)

  def combine(f: (String, String) => String)(xs: Traversable[Traversable[String]]) =
   xs reduceLeft { (x, y) => for (a <- x.view; b <- y) yield f(a, b) }

  def cartesian(xs: Traversable[String]*) = 
    combine(_.trim + " " + _.trim)(xs)

}