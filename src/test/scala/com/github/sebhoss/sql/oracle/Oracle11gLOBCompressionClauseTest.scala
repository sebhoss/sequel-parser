package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gLOBCompressionClauseTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.LOB_compression_clause

  def statements = Oracle11gLOBCompressionClauseTest.statements

}

object Oracle11gLOBCompressionClauseTest {

  val statements = {
    val compress = Set("COMPRESS")
    val compressOpts = Set("HIGH", "MEDIUM", "LOW")
    val noCompress = Set("NOCOMPRESS")

    cartesian(compress, compressOpts) ++ noCompress
  }

}