package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.AbstractParser
import com.github.sebhoss.sql.oracle.Oracle11gCommonParser._
import com.github.sebhoss.sql.oracle.Oracle11gConditionParser._
import com.github.sebhoss.sql.oracle.Oracle11gFunctionParser._
import com.github.sebhoss.sql.oracle.Oracle11gSelectParser.subquery
import com.github.sebhoss.sql.oracle.Oracle11gSizeClauseParser._

import org.parboiled.scala._

/**
 * Parser for storage_clause expression.
 *
 * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses009.htm#i997450 Syntax Definition]]
 */
object Oracle11gStorageClauseParser extends AbstractParser {

  /**
   * Creates a rule for a storage clause.
   *
   * It matches the following grammar:
   *
   * {{{
   * STORAGE
   * ({ INITIAL size_clause
   * | NEXT size_clause
   * | MINEXTENTS integer
   * | MAXEXTENTS { integer | UNLIMITED }
   * | maxsize_clause
   * | PCTINCREASE integer
   * | FREELISTS integer
   * | FREELIST GROUPS integer
   * | OPTIMAL [ size_clause | NULL ]
   * | BUFFER_POOL { KEEP | RECYCLE | DEFAULT }
   * | FLASH_CACHE { KEEP | NONE | DEFAULT }
   * | ENCRYPT
   * } ...
   * )
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses009.htm#SQLRF52300 Syntax Definition]]
   */
  def storage_clause = rule {
    "STORAGE" ~
      "(" ~ oneOrMore(("INITIAL" ~ size_clause) |
        ("NEXT" ~ size_clause) |
        ("MINEXTENTS" ~ integer) |
        ("MAXEXTENTS" ~ (integer | "UNLIMITED")) |
        maxsize_clause |
        ("PCTINCREASE" ~ integer) |
        ("FREELISTS" ~ integer) |
        ("FREELIST" ~ "GROUPS" ~ integer) |
        ("OPTIMAL" ~ optional(size_clause | "NULL")) |
        ("BUFFER_POOL" ~ ("KEEP" | "RECYCLE" | "DEFAULT")) |
        ("FLASH_CACHE" ~ ("KEEP" | "NONE" | "DEFAULT")) |
        ("ENCRYPT")) ~
      ")"
  }

  /**
   * Creates a rule for a maxsize clause.
   *
   * It matches the following grammar:
   *
   * {{{
   * MAXSIZE { UNLIMITED | size_clause }
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses009.htm#SQLRF52301 Syntax Definition]]
   */
  def maxsize_clause = rule {
    "MAXSIZE" ~ ("UNLIMITED" | size_clause)
  }

}