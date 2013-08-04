package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.AbstractParser
import com.github.sebhoss.sql.common.Lexer._

import org.parboiled.scala._

object Oracle11gConditionParser extends AbstractParser {

  /** Creates a rule for a condition.
    *
    * It matches the following grammar:
    *
    * {{{
    * { comparison_condition
    * | floating_point_condition
    * | logical_condition
    * | model_condition
    * | multiset_condition
    * | pattern_matching_condition
    * | range_condition
    * | null_condition
    * | XML_condition
    * | compound_condition
    * | exists_condition
    * | in_condition
    * | is_of_type_condition
    * }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/conditions001.htm#i1034172 Syntax Definition]]
    */
  def condition = rule {
    Letters
  }

}