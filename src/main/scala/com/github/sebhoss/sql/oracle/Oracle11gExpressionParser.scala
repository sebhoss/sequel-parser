package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.AbstractParser
import com.github.sebhoss.sql.common.Lexer._
import com.github.sebhoss.sql.oracle.Oracle11gCommonParser._
import com.github.sebhoss.sql.oracle.Oracle11gConditionParser._
import com.github.sebhoss.sql.oracle.Oracle11gFunctionParser._
import com.github.sebhoss.sql.oracle.Oracle11gSelectParser.subquery

import org.parboiled.scala._

object Oracle11gExpressionParser extends AbstractParser {

  /**
   * Creates a rule for an expression.
   *
   * It matches the following grammar:
   *
   * {{{
   * { simple_expression
   * | compound_expression
   * | case_expression
   * | cursor_expression
   * | datetime_expression
   * | function_expression
   * | interval_expression
   * | object_access_expression
   * | scalar_subquery_expression
   * | model_expression
   * | type_constructor_expression
   * | variable_expression
   * }
   *
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/expressions001.htm#i1002626 Syntax Definition]]
   */
  def expr : Rule0 = rule {
    (simple_expression |
      compound_expression |
      case_expression |
      cursor_expression |
      datetime_expression |
      function_expression |
      interval_expression |
      object_access_expression |
      scalar_subquery_expression |
      model_expression |
      type_constructor_expression |
      variable_expression)
  }

  /**
   * Creates a rule for a simple expression.
   *
   * It matches the following grammar:
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
  def simple_expression = rule {
    ((optional((table | schema ~ "." ~ table) ~ ".") ~ (column | "ROWID")) |
      "ROWNUM" |
      string |
      number |
      (sequence ~ "." ~ ("CURRVAL" | "NEXTVAL")) |
      "NULL")
  }

  /**
   * Creates a rule for a compound expression.
   *
   * It matches the following grammar:
   *
   * {{{
   * { (expr)
   * | { + | - | PRIOR } expr
   * | expr { * | / | + | - | || } expr
   * }
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/expressions003.htm#SQLRF52070 Syntax Definition]]
   */
  def compound_expression = rule {
    (("(" ~ expr ~ ")") |
      (("+" | "-" | "PRIOR") ~ expr) |
      (expr ~ ("*" | "/" | "-" | "||") ~ expr))
  }

  /**
   * Creates a rule for a case expression.
   *
   * It matches the following grammar:
   *
   * {{{
   * CASE { simple_case_expression
   * | searched_case_expression
   * }
   * [ else_clause ]
   * END
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/expressions004.htm#SQLRF20037 Syntax Definition]]
   */
  def case_expression = rule {
    "CASE" ~ (simple_case_expression |
      searched_case_expression) ~
      optional(else_clause) ~
      "END"
  }

  /**
   * Creates a rule for a simple case expression.
   *
   * It matches the following grammar:
   *
   * {{{
   * expr
   * { WHEN comparison_expr THEN return_expr }...
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/expressions004.htm#SQLRF52072 Syntax Definition]]
   */
  def simple_case_expression = rule {
    expr ~
      oneOrMore("WHEN" ~ expr ~ "THEN" ~ expr)
  }

  /**
   * Creates a rule for a searched case expression.
   *
   * It matches the following grammar:
   *
   * {{{
   * { WHEN condition THEN return_expr }...
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/expressions004.htm#SQLRF52073 Syntax Definition]]
   */
  def searched_case_expression = rule {
    oneOrMore("WHEN" ~ condition ~ "THEN" ~ expr)
  }

  /**
   * Creates a rule for an else clause.
   *
   * It matches the following grammar:
   *
   * {{{
   * ELSE else_expr
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/expressions004.htm#SQLRF52074 Syntax Definition]]
   */
  def else_clause = rule {
    "ELSE" ~ expr
  }

  /**
   * Creates a rule for a cursor expression.
   *
   * It matches the following grammar:
   *
   * {{{
   * CURSOR (subquery)
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/expressions006.htm#SQLRF52077 Syntax Definition]]
   */
  def cursor_expression = rule {
    "CURSOR" ~ "(" ~ subquery ~ ")"
  }

  /**
   * Creates a rule for a datetime expression.
   *
   * It matches the following grammar:
   *
   * {{{
   * expr AT
   * { LOCAL
   * | TIME ZONE { ' [ + | - ] hh:mm'
   * | DBTIMEZONE
   * | 'time_zone_name'
   * | expr
   * }
   * }
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/expressions007.htm#SQLRF00401 Syntax Definition]]
   */
  def datetime_expression = rule {
    expr ~ "AT" ~
      ("LOCAL" |
        ("TIME" ~ "ZONE" ~ (("'" ~ optional("+" | "-") ~ hh ~ ":" ~ mm ~ "'") |
          "DBTIMEZONE" |
          ("'" ~ time_zone_name ~ "'") |
          expr)))
  }

  /**
   * Creates a rule for a function expression.
   *
   * It matches the following grammar:
   *
   * {{{
   * Oracle_supplied_function | user_defined_function
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/expressions008.htm#SQLRF52082 Syntax Definition]]
   */
  def function_expression = rule {
    function
  }

  /**
   * Creates a rule for an interval expression.
   *
   * It matches the following grammar:
   *
   * {{{
   * ( expr1 - expr2 )
   * { DAY [ (leading_field_precision) ] TO
   * SECOND [ (fractional_second_precision) ]
   * | YEAR [ (leading_field_precision) ] TO
   * MONTH
   * }
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/expressions009.htm#SQLRF52084 Syntax Definition]]
   */
  def interval_expression = rule {
    "(" ~ expr ~ expr ~ ")" ~
      (("DAY" ~ optional("(" ~ leading_field_precision ~ ")") ~ "TO" ~
        "SECOND" ~ optional("(" ~ fractional_second_precision ~ ")")) |
        ("YEAR" ~ optional("(" ~ leading_field_precision ~ ")") ~ "TO" ~
          "MONTH"))
  }

  /**
   * Creates a rule for an object access expression.
   *
   * It matches the following grammar:
   *
   * {{{
   * { table_alias.column.
   * | object_table_alias.
   * | (expr).
   * }
   * { attribute [.attribute ]...
   * [.method ([ argument [, argument ]... ]) ]
   * | method ([ argument [, argument ]... ])
   * }
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/expressions011.htm#SQLRF52088 Syntax Definition]]
   */
  def object_access_expression = rule {
    NOTHING
  }

  def scalar_subquery_expression = rule {
    NOTHING
  }

  /**
   * Creates a rule for a model expression.
   *
   * It matches the following grammar:
   *
   * {{{
   * { measure_column [ { condition | expr }[ , { condition | expr } ...] ]
   * | aggregate_function
   * { [ { condition | expr }[ , { condition | expr } ...] ]
   * | [ single_column_for_loop [, single_column_for_loop] ... ]
   * | [ multi_column_for_loop ]
   * }
   * | analytic_function
   * }
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/expressions010.htm#SQLRF52086 Syntax Definition]]
   */
  def model_expression = rule {
    NOTHING
  }

  /**
   * Creates a rule for a type constructor expression.
   *
   * It matches the following grammar:
   *
   * {{{
   * [ NEW ] [ schema. ]type_name
   * ([ expr [, expr ]... ])
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/expressions014.htm#SQLRF52094 Syntax Definition]]
   */
  def type_constructor_expression = rule {
    NOTHING
  }

  def variable_expression = rule {
    NOTHING
  }

}