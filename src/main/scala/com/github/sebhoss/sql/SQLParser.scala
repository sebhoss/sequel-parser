package com.github.sebhoss.sql

import com.github.sebhoss.sql.oracle._

import org.parboiled.scala.Parser

object SQLParser {

  case object Oracle extends Parser {

    case object VERSION_11g extends Parser {

      case object ALTER_INDEX { def parser = Oracle11gAlterIndexParser.AlterIndexStatements }
      case object COMMENT { def parser = Oracle11gCommentParser.CommentStatements }
      case object CREATE_INDEX { def parser = Oracle11gCreateIndexParser.CreateIndexStatements }
      case object CREATE_TABLE { def parser = Oracle11gCreateTableParser.CreateTableStatements }
      case object SELECT { def parser = Oracle11gSelectParser.SelectStatements }

      def parser = rule {
        oneOrMore(ALTER_INDEX.parser | COMMENT.parser | CREATE_INDEX.parser | CREATE_TABLE.parser | SELECT.parser)
      }

    }

    /* Always links to the latest version of the database. */
    case object ALTER_INDEX { def parser = VERSION_11g.ALTER_INDEX.parser }
    case object COMMENT { def parser = VERSION_11g.COMMENT.parser }
    case object CREATE_INDEX { def parser = VERSION_11g.CREATE_INDEX.parser }
    case object CREATE_TABLE { def parser = VERSION_11g.CREATE_TABLE.parser }
    case object SELECT { def parser = VERSION_11g.SELECT.parser }

    def parser = rule {
      oneOrMore(ALTER_INDEX.parser | COMMENT.parser | CREATE_INDEX.parser | CREATE_TABLE.parser | SELECT.parser)
    }

  }

}

