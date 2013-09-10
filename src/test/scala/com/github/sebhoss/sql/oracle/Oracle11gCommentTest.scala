package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gCommentTest extends Oracle11gTest {

  def rule = Oracle11gCommentParser.comment

  def statements = Oracle11gCommentTest.statements

}

object Oracle11gCommentTest {

  val statements = {
    val comment = Set("COMMENT ON")
    val ref = Set("COLUMN table.column",
        "EDITION edition",
        "INDEXTYPE indextype",
        "MATERIALIZED VIEW mat_view",
        "MINING MODEL model",
        "OPERATOR operator",
        "TABLE table")
    val isString = Set("IS \"asdfasdf\";")

    cartesian(comment, ref, isString)
  }

}