package com.github.sebhoss.sql.common

import org.parboiled.scala.rules.Rule0
import org.parboiled.scala.Parser

object Lexer extends Parser {

  def WhiteSpace = rule { zeroOrMore(anyOf(" \n\r\t\f")) }

  def Digits = rule { oneOrMore(Digit) }

  def Digit = rule { "0" - "9" }

  def Letters = rule { oneOrMore(Letter) }

  def Letter = rule { "a" - "z" | "A" - "Z" }

  def Symbols = rule { str("_") }

}