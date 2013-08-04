package com.github.sebhoss.sql.common

import org.parboiled.scala.rules.Rule0

trait CreateTableStatementsParser {

  /** Creates a rule which matches multiple `CREATE TABLE` statements.
    */
  def CreateTableStatements: Rule0

}