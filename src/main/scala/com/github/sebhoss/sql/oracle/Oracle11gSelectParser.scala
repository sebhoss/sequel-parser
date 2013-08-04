package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.AbstractParser
import com.github.sebhoss.sql.oracle.Oracle11gCommonParser._
import com.github.sebhoss.sql.oracle.Oracle11gConstraintParser._
import com.github.sebhoss.sql.oracle.Oracle11gExpressionParser._

import org.parboiled.scala._

object Oracle11gSelectParser extends AbstractParser {

  /** Creates a rule which matches multiple `SELECT` statements.
    */
  def SelectStatements = rule {
    oneOrMore(select) ~ EOI
  }

  /** Creates a rule for a select statement.
    *
    * It matches the following grammar:
    *
    * {{{
    * [ subquery_factoring_clause ] subquery [ for_update_clause ] ;
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_10002.htm#SQLRF55224 Syntax Definition]]
    */
  def select = rule {
    optional(subquery_factoring_clause) ~ subquery ~ optional(for_update_clause) ~ ";"
  }

  /** Creates a rule for a subquery factoring clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * WITH
    * query_name ([c_alias [, c_alias]...]) AS (subquery) [search__clause] [cycle_clause]
    * [, query_name ([c_alias [, c_alias]...]) AS (subquery) [search_clause] [cycle_clause]]...
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_10002.htm#i2161315 Syntax Definition]]
    */
  def subquery_factoring_clause = rule {
    NOTHING
  }

  /** Creates a rule for a subquery.
    *
    * It matches the following grammar:
    *
    * {{{
    * { query_block
    * | subquery { UNION [ALL] | INTERSECT | MINUS } subquery
    * [ { UNION [ALL] | INTERSECT | MINUS } subquery ]...
    * | ( subquery )
    * } [ order_by_clause ]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_10002.htm#i2126435 Syntax Definition]]
    */
  def subquery : Rule0 = rule {
    (query_block |
      (subquery ~ (("UNION" ~ optional("ALL")) | "INTERSECT" | "MINUS")) |
      ("(" ~ subquery ~ ")")) ~
      optional(order_by_clause)
  }

  /** Creates a rule for a query block.
    *
    * It matches the following grammar:
    *
    * {{{
    * SELECT
    * [ hint ]
    * [ { { DISTINCT | UNIQUE } | ALL } ]
    * select_list
    * FROM { table_reference | join_clause | ( join_clause ) }
    * [ , { table_reference | join_clause | (join_clause) } ] ...
    * [ where_clause ]
    * [ hierarchical_query_clause ]
    * [ group_by_clause ]
    * [ model_clause ]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_10002.htm#CHDDCHGF Syntax Definition]]
    */
  def query_block = rule {
    "SELECT" ~
      optional(hint) ~
      optional(("DISTINCT" | "UNIQUE") | "ALL") ~
      select_list ~
      "FROM" ~ (table_reference | join_clause | ("(" ~ join_clause ~ ")")) ~
      zeroOrMore("," ~ (table_reference | join_clause | ("(" ~ join_clause ~ ")"))) ~
      optional(where_clause) ~
      optional(hierarchical_query_clause) ~
      optional(group_by_clause) ~
      optional(model_clause)
  }

  /** Creates a rule for a select list.
    *
    * It matches the following grammar:
    *
    * {{{
    * { [t_alias.] *
    * | { query_name.*
    * | [ schema. ]
    * { table | view | materialized view } .*
    * | expr [ [ AS ] c_alias ]
    * }
    * [, { query_name.*
    * | [ schema. ]
    * { table | view | materialized view } .*
    * | expr [ [ AS ] c_alias ]
    * }
    * ]...
    * }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_10002.htm#i2126854 Syntax Definition]]
    */
  def select_list = rule {
    ((optional(t_alias ~ ".") ~ "*") |
      ((query_name ~ "." ~ "*") |
        (optional(schema ~ ".") ~
          (table | view) ~ "." ~ "*") |
          (expr ~ optional(optional("AS") ~ c_alias))) ~
          zeroOrMore("," ~ (query_name ~ "." ~ "*") |
            (optional(schema ~ ".") ~
              (table | view) ~ "." ~ "*") |
              (expr ~ optional(optional("AS") ~ c_alias))))
  }

  /** Creates a rule for a table reference.
    *
    * It matches the following grammar:
    *
    * {{{
    * { ONLY (query_table_expression)
    * | query_table_expression [ pivot_clause | unpivot_clause ]
    * } [ flashback_query_clause ]
    * [ t_alias ]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_10002.htm#i2126863 Syntax Definition]]
    */
  def table_reference : Rule0 = rule {
    (("ONLY" ~ "(" ~ query_table_expression ~ ")") |
      (query_table_expression ~ optional(pivot_clause | unpivot_clause))) ~
      optional(flashback_query_clause) ~
      optional(t_alias)
  }

  /** Creates a rule for a query table expression.
    *
    * It matches the following grammar:
    *
    * {{{
    * { query_name
    * | [ schema. ]
    * { table [ partition_extension_clause
    * | @ dblink
    * ]
    * | { view | materialized view } [ @ dblink ]
    * } ["sample_clause"]
    * | (subquery [ subquery_restriction_clause ])
    * | table_collection_expression
    * }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_10002.htm#i2126073 Syntax Definition]]
    */
  def query_table_expression = rule {
    (query_name |
      optional(schema ~ ".") ~
      (table ~ (optional(partition_extension_clause |
        "@" ~ dblink)) |
        (view ~ optional("@" ~ dblink))) ~ (optional(sample_clause) |
          ("(" ~ subquery ~ optional(subquery_restriction_clause) ~ ")") |
          table_collection_expression))
  }

  /** Creates a rule for a partition extension clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * { PARTITION (partition)
    * | PARTITION FOR (partition_key_value [, partition_key_value]...)
    * | SUBPARTITION (subpartition)
    * | SUBPARTITION FOR (subpartition_key_value [, subpartition_key_value]...)
    * }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_10002.htm#SQLRF55239 Syntax Definition]]
    */
  def partition_extension_clause = rule {
    ("PARTITION" ~ "(" ~ partition ~ ")") |
      ("PARTITION" ~ "FOR" ~ "(" ~ partition_key_value ~ zeroOrMore("," ~ partition_key_value) ~ ")") |
      ("SUBPARTITION" ~ "(" ~ subpartition ~ ")") |
      ("SUBPARTITION" ~ "FOR" ~ "(" ~ subpartition_key_value ~ zeroOrMore("," ~ subpartition_key_value) ~ ")")
  }

  def partition_key_value = rule {
    NOTHING
  }

  def subpartition_key_value = rule {
    NOTHING
  }

  /** Creates a rule for a sample clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * SAMPLE [ BLOCK ]
    * (sample_percent)
    * [ SEED (seed_value) ]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_10002.htm#SQLRF55238 Syntax Definition]]
    */
  def sample_clause = rule {
    "SAMPLE" ~ optional("BLOCK") ~
      "(" ~ sample_percent ~ ")" ~
      optional("SEED" ~ "(" ~ seed_value ~ ")")
  }

  /** Creates a rule for a subquery restriction clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * WITH { READ ONLY
    * | CHECK OPTION
    * } [ CONSTRAINT constraint ]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_10002.htm#SQLRF55240 Syntax Definition]]
    */
  def subquery_restriction_clause = rule {
    "WITH" ~ (("READ" ~ "ONLY") |
      ("CHECK" ~ "OPTION")) ~
      optional("CONSTRAINT" ~ constraint)
  }

  /** Creates a rule for a table collection expression.
    *
    * It matches the following grammar:
    *
    * {{{
    * TABLE (collection_expression) [ (+) ]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_10002.htm#SQLRF55241 Syntax Definition]]
    */
  def table_collection_expression = rule {
    "TABLE" ~ "(" ~ collection_expression ~ ")" ~ optional("(" ~ "+" ~ ")")
  }

  /** Creates a rule for a collection expression.
    *
    * It matches the following grammar:
    *
    * {{{
    * subquery | column
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_10002.htm#i2104990 Syntax Definition]]
    */
  def collection_expression = rule {
    subquery | column
  }

  /** Creates a rule for a pivot clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * table_reference PIVOT [ XML ]
    * ( aggregate_function ( expr ) [[AS] alias ]
    * [, aggregate_function ( expr ) [[AS] alias ] ]...
    * pivot_for_clause
    * pivot_in_clause
    * )
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_10002.htm#CHDCEJJE Syntax Definition]]
    */
  def pivot_clause = rule {
    table_reference ~ "PIVOT" ~ optional("XML") ~
      "(" ~ aggregate_function ~ "(" ~ expr ~ ")" ~ optional(optional("AS") ~ alias) ~
      zeroOrMore("," ~ aggregate_function ~ "(" ~ expr ~ ")") ~ optional(optional("AS") ~ alias) ~
      pivot_for_clause ~
      pivot_in_clause
  }

  /** Creates a rule for a pivot for clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * FOR { column
    * | ( column [, column]... )
    * }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_10002.htm#SQLRF55234 Syntax Definition]]
    */
  def pivot_for_clause = rule {
    "FOR" ~ (column |
      ("(" ~ column ~ zeroOrMore("," ~ column)))
  }

  /** Creates a rule for a pivot in clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * IN ( { { { expr
    * | ( expr [, expr]... )
    * } [ [ AS] alias]
    * }...
    * | subquery
    * | ANY [, ANY]...
    * }
    * )
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_10002.htm#SQLRF55235 Syntax Definition]]
    */
  def pivot_in_clause = rule {
    "IN" ~ "(" ~ (oneOrMore((expr |
      "(" ~ expr ~ zeroOrMore("," ~ expr) ~ ")") ~
      optional(optional("AS") ~ alias)) |
      subquery |
      ("ANY" ~ zeroOrMore("," ~ "ANY"))) ~
      ")"
  }

  /** Creates a rule for an unpivot clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * table_reference UNPIVOT [ {INCLUDE | EXCLUDE} NULLS ]
    * ( { column | ( column [, column]... ) }
    * pivot_for_clause
    * unpivot_in_clause
    * )
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_10002.htm#SQLRF55236 Syntax Definition]]
    */
  def unpivot_clause = rule {
    table_reference ~ "UNPIVOT" ~ optional(("INCLUDE" | "EXCLUDE") ~ "NULLS") ~
      "(" ~ (column | ("(" ~ column ~ zeroOrMore("," ~ column) ~ ")")) ~
      pivot_for_clause ~
      unpivot_in_clause ~
      ")"
  }

  /** Creates a rule for an unpivot in clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * IN
    * ( { column | ( column [, column]... ) }
    * [  AS { literal | ( literal [, literal]... ) } ]
    * [, { column | ( column [, column]... ) }
    * [  AS {literal | ( literal [, literal]... ) } ]
    * ]...
    * )
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_10002.htm#SQLRF55237 Syntax Definition]]
    */
  def unpivot_in_clause = rule {
    "IN" ~
      "(" ~ (column | ("(" ~ column ~ zeroOrMore("," ~ column) ~ ")")) ~
      optional("AS" ~ (literal | ("(" ~ literal ~ zeroOrMore("," ~ literal) ~ ")"))) ~
      zeroOrMore("," ~ (column | ("(" ~ column ~ zeroOrMore("," ~ column) ~ ")")) ~
        optional("AS" ~ (literal | ("(" ~ literal ~ zeroOrMore("," ~ literal) ~ ")")))) ~
      ")"
  }

  /** Creates a rule for a flashback query clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * { VERSIONS BETWEEN
    * { SCN | TIMESTAMP }
    * { expr | MINVALUE } AND { expr | MAXVALUE }
    * | AS OF { SCN | TIMESTAMP } expr
    * }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_10002.htm#i2126134 Syntax Definition]]
    */
  def flashback_query_clause = rule {
    ("VERSIONS" ~ "BETWEEN" ~
      (("SCN" | "TIMESTAMP") ~
        (expr | "MINVALUE") ~ "AND" ~ (expr | "MAXVALUE")) |
        ("AS" ~ "OF" ~ ("SCN" | "TIMESTAMP") ~ expr))
  }

  def join_clause = rule {
    NOTHING
  }

  def where_clause = rule {
    NOTHING
  }

  def hierarchical_query_clause = rule {
    NOTHING
  }

  def group_by_clause = rule {
    NOTHING
  }

  def model_clause = rule {
    NOTHING
  }

  /** Creates a rule for an order by clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * ORDER [ SIBLINGS ] BY
    * { expr | position | c_alias }
    * [ ASC | DESC ]
    * [ NULLS FIRST | NULLS LAST ]
    * [, { expr | position | c_alias }
    * [ ASC | DESC ]
    * [ NULLS FIRST | NULLS LAST ]
    * ]...
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_10002.htm#i2168299 Syntax Definition]]
    */
  def order_by_clause = rule {
    NOTHING
  }

  /** Creates a rule for a for update clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * FOR UPDATE
    * [ OF [ [ schema. ] { table | view } . ] column
    * [, [ [ schema. ] { table | view } . ] column
    * ]...
    * ]
    * [ { NOWAIT | WAIT integer
    * |  SKIP LOCKED
    * }
    * ]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_10002.htm#i2126016 Syntax Definition]]
    */
  def for_update_clause = rule {
    NOTHING
  }

}