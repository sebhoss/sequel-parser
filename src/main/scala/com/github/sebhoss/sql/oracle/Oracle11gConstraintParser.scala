package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.AbstractParser
import com.github.sebhoss.sql.common.Lexer._
import com.github.sebhoss.sql.oracle.Oracle11gCommonParser._
import com.github.sebhoss.sql.oracle.Oracle11gConditionParser._
import com.github.sebhoss.sql.oracle.Oracle11gCreateIndexParser._

import org.parboiled.scala._

object Oracle11gConstraintParser extends AbstractParser {

  /** Creates a rule for a constraint.
    *
    * It matches the following grammar:
    *
    * {{{
    * { inline_constraint
    * | out_of_line_constraint
    * | inline_ref_constraint
    * | out_of_line_ref_constraint
    * }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses002.htm#SQLRF52182 Syntax Definition]]
    */
  def constraint = rule {
    (inline_constraint |
      out_of_line_constraint |
      inline_ref_constraint |
      out_of_line_ref_constraint)
  }

  /** Creates a rule for an inline constraint.
    *
    * It matches the following grammar:
    *
    * {{{
    * [ CONSTRAINT constraint_name ]
    * { [ NOT ] NULL
    * | UNIQUE
    * | PRIMARY KEY
    * | references_clause
    * | CHECK (condition)
    * }
    * [ constraint_state ]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses002.htm#SQLRF52183 Syntax Definition]]
    */
  def inline_constraint = rule {
    optional("CONSTRAINT" ~ constraint_name) ~
      ((optional("NOT") ~ "NULL") |
        "UNIQUE" |
        ("PRIMARY" ~ "KEY") |
        references_clause |
        ("CHECK" ~ "(" ~ condition ~ ")")) ~
        constraint_state
  }

  /** Creates a rule for a reference clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * REFERENCES [ schema. ] { object_table | view }
    * [ (column [, column ]...) ]
    * [ON DELETE { CASCADE | SET NULL } ]
    * [ constraint_state ]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses002.htm#CJAIHHGC Syntax Definition]]
    */
  def references_clause = rule {
    "REFERENCES" ~ optional(schema ~ ".") ~ (table | view) ~
      optional("(" ~ column ~ zeroOrMore("," ~ column) ~ ")") ~
      optional("ON" ~ "DELETE" ~ ("CASCADE" | "SET" ~ "NULL")) ~
      constraint_state
  }

  /** Creates a rule for a constraint state.
    *
    * It matches the following grammar:
    *
    * {{{
    * [ [ [ NOT ] DEFERRABLE ]
    * [ INITIALLY { IMMEDIATE | DEFERRED } ]
    * | [ INITIALLY { IMMEDIATE | DEFERRED } ]
    * [ [ NOT ] DEFERRABLE ]
    * ]
    * [ RELY | NORELY ]
    * [ using_index_clause ]
    * [ ENABLE | DISABLE ]
    * [ VALIDATE | NOVALIDATE ]
    * [ exceptions_clause ]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses002.htm#CJAFFBAA Syntax Definition]]
    */
  def constraint_state = rule {
    optional((optional(optional("NOT") ~ "DEFERABLE") ~
      optional("INITIALLY" ~ ("IMMEDIATE" | "DEFERED"))) |
      (optional("INITIALLY" ~ ("IMMEDIATE" | "DEFERED")) ~
        optional(optional("NOT") ~ "DEFERABLE"))) ~
      optional("RELY" | "NORELY") ~
      optional(using_index_clause) ~
      optional("ENABLE" | "DISABLE") ~
      optional("VALIDATE" | "NOVALIDATE") ~
      optional(exceptions_clause)
  }

  /** Creates a rule for a index clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * USING INDEX
    * { [ schema. ] index
    * | (create_index_statement)
    * | index_properties
    * }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses002.htm#SQLRF52189 Syntax Definition]]
    */
  def using_index_clause = rule {
    "USING" ~ "INDEX" ~
      ((optional(schema ~ ".") ~ index) |
        ("(" ~ create_index_statement ~ ")") |
        optional(index_properties))
  }

  /** Creates a rule for an exceptions clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * EXCEPTIONS INTO [ schema. ] table
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses002.htm#CJAEHIDE Syntax Definition]]
    */
  def exceptions_clause = rule {
    "EXCEPTIONS" ~ "INTO" ~ optional(schema ~ ".") ~ table
  }

  /** Creates a rule for an inline ref constraint.
    *
    * It matches the following grammar:
    *
    * {{{
    * { SCOPE  IS [ schema. ] scope_table
    * | WITH ROWID
    * | [ CONSTRAINT constraint_name ]
    * references_clause
    * [ constraint_state ]
    * }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses002.htm#CJAHIEIJ Syntax Definition]]
    */
  def inline_ref_constraint = rule {
    (("SCOPE" ~ "IS" ~ optional(schema ~ ".") ~ table) |
      ("WITH" ~ "ROWID") |
      optional("CONSTRAINT" ~ constraint_name) ~
      references_clause ~
      optional(constraint_state))
  }

  /** Creates a rule for an out of line constraint.
    *
    * It matches the following grammar:
    *
    * {{{
    * [ CONSTRAINT constraint_name ]
    * { UNIQUE (column [, column ]...)
    * | PRIMARY KEY (column [, column ]...)
    * | FOREIGN KEY (column [, column ]...) references_clause
    * | CHECK (condition)
    * } [ constraint_state ]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses002.htm#CJADJGEC Syntax Definition]]
    */
  def out_of_line_constraint = rule {
    optional("CONSTRAINT" ~ constraint_name) ~
      (("UNIQUE" ~ "(" ~ column ~ zeroOrMore("," ~ column) ~ ")") |
        ("PRIMARY" ~ "KEY" ~ "(" ~ column ~ zeroOrMore("," ~ column) ~ ")") |
        ("FOREIGN" ~ "KEY" ~ "(" ~ column ~ zeroOrMore("," ~ column) ~ ")" ~ references_clause) |
        ("CHECK" ~ "(" ~ condition ~ ")")) ~
        optional(constraint_state)
  }

  /** Creates a rule for an out of line ref constraint.
    *
    * It matches the following grammar:
    *
    * {{{
    * { SCOPE FOR ({ ref_col | ref_attr })
    * IS [ schema. ] scope_table
    * | REF ({ ref_col | ref_attr }) WITH ROWID
    * | [ CONSTRAINT constraint_name ] FOREIGN KEY
    * ( { ref_col | ref_attr } ) references_clause
    * [ constraint_state ]
    * }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses002.htm#CJABCJJF Syntax Definition]]
    */
  def out_of_line_ref_constraint = rule {
    ("SCOPE" ~ "FOR" ~ "(" ~ (ref_col | ref_attr) ~ ")" ~
      "IS" ~ optional(schema ~ ".") ~ scope_table) |
      ("REF" ~ "(" ~ (ref_col | ref_attr) ~ ")" ~ "WITH" ~ "ROWID") |
      (optional("CONSTRAINT" ~ constraint_name) ~ "FOREIGN" ~ "KEY") ~
      "(" ~ (ref_col | ref_attr) ~ ")" ~ references_clause ~
      optional(constraint_state)
  }

}