package com.github.sebhoss.sql.common

import org.junit.runner.RunWith
import org.parboiled.errors.ParseError
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.parboiled.scala.rules.Rule0
import org.parboiled.scala.parserunners.ReportingParseRunner

@RunWith(classOf[JUnitRunner])
abstract class SingleParserRuleTest extends FunSuite {

  def databaseName: String
  def rule: Rule0
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

  def generateErrorMessage(errors : List[ParseError], statement : String) = {
    errors map (error ⇒
      statement.slice(0, error.getStartIndex()) + "[" +
        statement.slice(error.getStartIndex(), error.getEndIndex()) + "]" +
        statement.slice(error.getEndIndex(), statement.length())) mkString("\n")
  }
  
}