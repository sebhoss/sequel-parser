package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.AbstractParser
import com.github.sebhoss.sql.oracle.Oracle11gCommonParser._
import com.github.sebhoss.sql.oracle.Oracle11gSizeClauseParser._

/**
 * Parser for a allocate_extent_clause expression.
 *
 * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses001.htm#CJABGIJJ Syntax Definition]]
 */
object Oracle11gAllocateExtentClauseParser extends AbstractParser {

  /**
   *  Creates a rule for an allocate extent clause.
   *
   * It matches the following grammar:
   *
   * {{{
   * ALLOCATE EXTENT
   * [ ( { SIZE size_clause
   * | DATAFILE 'filename'
   * | INSTANCE integer
   * } ...
   * )
   * ]
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses001.htm#SQLRF52176 Syntax Definition]]
   */
  def allocate_extent_clause = rule {
    "ALLOCATE" ~ "EXTENT" ~
      optional("(" ~ oneOrMore(("SIZE" ~ size_clause) |
        ("DATAFILE" ~ "'" ~ filename ~ "'") |
        ("INSTANCE" ~ integer)) ~
        ")")
  }

}