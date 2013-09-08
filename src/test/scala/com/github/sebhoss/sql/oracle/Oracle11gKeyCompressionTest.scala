package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gKeyCompressionTest extends Oracle11gTest {
  
  def rule = Oracle11gCreateIndexParser.key_compression

  def statements = Oracle11gKeyCompressionTest.statements

}

object Oracle11gKeyCompressionTest {

  val statements = {
    cartesian(List("COMPRESS"), List("1", "10", "100")) ++ List("NOCOMPRESS")
  }

}