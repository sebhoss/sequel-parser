package com.github.sebhoss.sql.oracle

import scala.language.postfixOps

import com.github.sebhoss.sql.SQLParser.Oracle

class Oracle11gDebuggingTest extends Oracle11gTest {

  def statements = Array(
    """
      CREATE INDEX
          ix_emp_01
      ON
          emp (deptno)
      TABLESPACE
          index_tbs;
      """ // TODO: Als IT benutzen. Referenz auf Online-Seite (Ein IT pro Seite)
  ) map (statement ⇒ statement trim)

  def rule = Oracle.VERSION_11g.CREATE_INDEX.parser // TODO: Nur Oracle.VERSION_11g.parser benutzen

  //  for ((statement) ← statements) {
  //    test("The statement [" + statement + "] should be matched be the parser!") {
  //      // Given
  //      val runner = TracingParseRunner(rule)
  //
  //      //      .filter(
  //      //        Lines(10 until 20) && Rules.below(parser.Factor) && !Rules.below(parser.Digits)
  //      //      )
  //
  //      // When
  //      val result = runner.run(statement)
  //
  //      // Then
  //      assert(result.matched, "Could not parse:\n\n\t" + generateErrorMessage(result.parseErrors, statement) +
  //        "\nTree: " + ParseTreeUtils.printNodeTree(result));
  //    }
  //  }

}