package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.AbstractParser
import com.github.sebhoss.sql.common.Lexer._

import org.parboiled.scala._

/** Parser for functions.
  *
  * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/functions001.htm#CJAHCIID Syntax Definition]]
  */
object Oracle11gFunctionParser extends AbstractParser {

  /** Creates a rule for a Oracle supplied function.
    *
    * It matches the following grammar:
    *
    * {{{
    * { single_row_function
    * | aggregate_function
    * | analytic_function
    * | object_reference_function
    * | model_function
    * | user_defined_function
    * | OLAP_function
    * | data_cartridge_function
    * }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/functions001.htm#SQLRF51176 Syntax Definition]]
    */
  def function = rule {
    NOTHING
  }

  def user_defined_function = rule {
    NOTHING
  }

}