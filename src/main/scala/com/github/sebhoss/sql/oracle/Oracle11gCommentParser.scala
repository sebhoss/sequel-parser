package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.AbstractParser
import com.github.sebhoss.sql.oracle.Oracle11gCommonParser._

import org.parboiled.scala._

/**
 * Parser for a comment expression.
 *
 * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_4009.htm#SQLRF01109 Syntax Definition]]
 */
object Oracle11gCommentParser extends AbstractParser {

  /**
   * Creates a rule which matches multiple `COMMENT` statements.
   */
  def CommentStatements = rule {
    oneOrMore(comment) ~ EOI
  }

  /**
   * Creates a rule for a comment.
   *
   * It matches the following grammar:
   *
   * {{{
   * COMMENT ON
   * { COLUMN [ schema. ]
   * { table. | view. | materialized_view. } column
   * | EDITION edition_name
   * | INDEXTYPE [ schema. ] indextype
   * | MATERIALIZED VIEW materialized_view
   * | MINING MODEL [ schema. ] model
   * | OPERATOR [ schema. ] operator
   * | TABLE [ schema. ] { table | view }
   * }
   * IS string ;
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_4009.htm#SQLRF53761 Syntax Definition]]
   */
  def comment = rule {
    "COMMENT" ~ "ON" ~
      (("COLUMN" ~ (table | schema ~ "." ~ table) ~ "." ~ column) |
        ("EDITION" ~ edition_name) |
        ("INDEXTYPE" ~ optional(schema ~ ".") ~ indextype) |
        ("MATERIALIZED" ~ "VIEW" ~ materialized_view) |
        ("MINING" ~ "MODEL" ~ optional(schema ~ ".") ~ model) |
        ("OPERATOR" ~ optional(schema ~ ".") ~ operator) |
        ("TABLE" ~ optional(schema ~ ".") ~ table)) ~
        "IS" ~ string ~ ";"
  }

}