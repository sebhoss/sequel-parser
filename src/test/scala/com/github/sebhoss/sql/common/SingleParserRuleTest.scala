package com.github.sebhoss.sql.common

import org.junit.runner.RunWith
import org.parboiled.errors.ParseError
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.parboiled.scala.rules.Rule0
import org.parboiled.scala.parserunners.ReportingParseRunner

/**
 * Checks a single SQL statement against a single parsing rule.
 */
@RunWith(classOf[JUnitRunner])
abstract class SingleParserRuleTest extends FunSuite {

  /** The name of the database the given statement should be validated against. */
  def databaseName: String
  
  /** The parsing rule to use. */
  def rule: Rule0
  
  /** Statements to check against the above parsing rule. */
  def statements: TraversableOnce[String]

  for (statement ← statements) {
    test(s"The statement [$statement] should be parsed by [$rule] for database [$databaseName]") {
      // Given
      val runner = ReportingParseRunner(rule)

      // When
      val result = runner.run(statement)

      // Then
      assert(result.matched, "Could not parse:\n\n\t" + generateErrorMessage(result.parseErrors, statement) + "\n");
    }
  }

  /**
   * Marks the parsing errors inside the given statement with enclosing brackets.
   */
  def generateErrorMessage(errors : List[ParseError], statement : String) = {
    errors map (error ⇒
      statement.slice(0, error.getStartIndex()) + "[" +
        statement.slice(error.getStartIndex(), error.getEndIndex()) + "]" +
        statement.slice(error.getEndIndex(), statement.length())) mkString("\n")
  }
  
}