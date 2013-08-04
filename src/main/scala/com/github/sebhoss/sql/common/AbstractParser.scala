package com.github.sebhoss.sql.common

import com.github.sebhoss.sql.common.Lexer._

import org.parboiled.scala._

abstract class AbstractParser extends Parser {

  override implicit def toRule(string: String) = {
    WhiteSpace ~ ignoreCase(string) ~ WhiteSpace
  }

}