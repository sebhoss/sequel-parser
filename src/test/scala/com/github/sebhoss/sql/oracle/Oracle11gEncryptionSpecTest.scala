package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gEncryptionSpecTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.encryption_spec
  
  def statements = Oracle11gEncryptionSpecTest.statements

}

object Oracle11gEncryptionSpecTest {

  val statements = {
    val algorithm = Set("USING 'algorithm'")
    val password = Set("IDENTIFIED BY password")
    val constraint = Set("'constraint'")
    val salt = Set("SALT", "NOSALT")

    cartesian(algorithm, password, constraint, salt)
  }

}