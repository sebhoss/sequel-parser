package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

/**
 * Checks whether the supplied Oracle 11g parser understands simple expressions.
 *
 * Simple expressions are build by the following grammar:
 *
 * {{{
 * { [ query_name.
 * | [schema.]
 * { table. | view. | materialized view. }
 * ] { column | ROWID }
 * | ROWNUM
 * | string
 * | number
 * | sequence. { CURRVAL | NEXTVAL }
 * | NULL
 * }
 * }}}
 *
 * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/expressions002.htm#SQLRF52068 Syntax Definition]]
 */
class Oracle11gSimpleExpressionTest extends Oracle11gTest {

  def rule = Oracle11gExpressionParser.simple_expression

  def statements = Oracle11gSimpleExpressionTest.statements

}

object Oracle11gSimpleExpressionTest {

  val statements = {
    cartesian(List("table.", "schema.table."), List("column", "ROWID")) ++ List("ROWNUM") ++ List("\"asdf\"") ++
      List("1", "2", "3") ++ List("sequence.CURRVAL", "sequence.NEXTVAL") ++ List("NULL")
  }

}