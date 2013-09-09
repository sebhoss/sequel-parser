package com.github.sebhoss.sql.common

object StringHelper {

  def flatten(elements: Traversable[Traversable[String]]) =
    for (inner <- elements) yield (inner.mkString(" "))

  def powerset(elements: String*) =
    flatten(elements.toSet.subsets.toSet)

  def combination(xx: List[List[String]], i: Int): String = xx match {
    case Nil => ""
    case x :: xs => x(i % x.length).trim + " " + combination(xs, i/x.length).trim
  }

  def cartesian(ll: List[String]*): Iterator[String] = {
    Iterator.from(0).takeWhile(n => n < ll.map(_.length).product).map(combination(ll.toList, _))
  }

}