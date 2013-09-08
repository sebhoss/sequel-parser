package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gEncryptionSpecTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.encryption_spec
  
  def statements = Oracle11gEncryptionSpecTest.statements

}

object Oracle11gEncryptionSpecTest {

  val statements = {
    val algorithm = List("USING 'algorithm'")
    val password = List("IDENTIFIED BY password")
    val constraint = List("'constraint'")
    val salt = List("SALT", "NOSALT")

    cartesian(algorithm, password, constraint, salt)
  }

}