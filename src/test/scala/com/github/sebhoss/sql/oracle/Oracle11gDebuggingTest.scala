package com.github.sebhoss.sql.oracle

import scala.language.postfixOps
import com.github.sebhoss.sql.SQLParser.Oracle
import org.parboiled.scala.parserunners.TracingParseRunner
import org.parboiled.support.ParseTreeUtils

class Oracle11gDebuggingTest extends Oracle11gTest {

  def statements = List(
    """
      ADD_COLUMN XMLTABLE identifier COLUMNS column CHARACTER(123) PATH "path"
      """
  ) map (statement ⇒ statement trim)

  def rule = Oracle11gCreateIndexParser.add_column_clause

    for ((statement) ← statements) {
      test(s"The statement [$statement] should be matched be the parser!") {
        // Given
        val runner = TracingParseRunner(rule)
  
        //      .filter(
        //        Lines(10 until 20) && Rules.below(parser.Factor) && !Rules.below(parser.Digits)
        //      )
  
        // When
        val result = runner.run(statement)
  
        // Then
        assert(result.matched, "Could not parse:\n\n\t" + generateErrorMessage(result.parseErrors, statement) +
          "\nTree: " + ParseTreeUtils.printNodeTree(result));
      }
    }

}