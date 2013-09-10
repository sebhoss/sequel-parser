package com.github.sebhoss.sql.oracle

import scala.language.postfixOps
import org.junit.runner.RunWith
import org.parboiled.errors.ParseError
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import com.github.sebhoss.sql.SQLParser.Oracle
import org.parboiled.scala.parserunners.TracingParseRunner
import org.parboiled.support.ParseTreeUtils

class Oracle11gDebuggingTest extends FunSuite {

  def statements = Set(
    """
      ADD_COLUMN XMLTABLE identifier COLUMNS column CHARACTER(123) PATH "path"
      """
  ) map (statement ⇒ statement trim)

  def rule = Oracle11gCreateIndexParser.add_column_clause

  for ((statement) ← statements) {
//    test(s"The statement [$statement] should be matched be the parser!") {
//      // Given
//      val runner = TracingParseRunner(rule)
//
//      // When
//      val result = runner.run(statement)
//
//      // Then
//      assert(result.matched, "Could not parse:\n\n\t" + generateErrorMessage(result.parseErrors, statement) +
//          "\nTree: " + ParseTreeUtils.printNodeTree(result));
//    }
  }
  
  def generateErrorMessage(errors : List[ParseError], statement : String) = {
    errors map (error ⇒
      statement.slice(0, error.getStartIndex()) + "[" +
        statement.slice(error.getStartIndex(), error.getEndIndex()) + "]" +
        statement.slice(error.getEndIndex(), statement.length())) mkString("\n")
  }

}