package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.AbstractParser
import com.github.sebhoss.sql.common.Lexer._

import org.parboiled.scala._

object Oracle11gCommonParser extends AbstractParser {

  def schema = rule {
    objectName
  }

  def table = rule {
    objectName
  }

  def view = rule {
    objectName
  }

  def column = rule {
    objectName
  }

  def constraint_name = rule {
    objectName
  }

  def index = rule {
    objectName
  }

  def cluster = rule {
    objectName
  }

  def integer = rule {
    Digits
  }

  def tablespace = rule {
    objectName
  }

  def table_alias = rule {
    objectName
  }

  def index_expression = rule {
    Letters
  }

  def literal = rule {
    Letters
  }

  def partition = rule {
    objectName
  }

  def subpartition = rule {
    objectName
  }

  def column_list = rule {
    Letters
  }

  def LOB_item = rule {
    objectName
  }

  def varray_item = rule {
    objectName
  }

  def LOB_segname = rule {
    objectName
  }

  def hash_partition_quantity = rule {
    Digits
  }

  def indextype = rule {
    objectName
  }

  def ODCI_parameters = rule {
    Letters
  }

  def XPaths_list = rule {
    Letters
  }

  def namespace = rule {
    objectName
  }

  def XML_namespaces_clause = rule {
    Letters
  }

  def XQuery_string = rule {
    Letters
  }

  def string = rule {
    "\"" ~ Letters ~ "\"" 
  }

  def number = rule {
    Digits
  }

  def sequence = rule {
    objectName
  }

  def repeat_interval = rule {
    Digits
  }

  def identifier = rule {
    objectName
  }

  def precision = rule {
    WhiteSpace ~ Digits ~ WhiteSpace
  }

  def scale = rule {
    WhiteSpace ~ Digits ~ WhiteSpace
  }

  def size = rule {
    WhiteSpace ~ Digits ~ WhiteSpace
  }

  def fractional_seconds_precision = rule {
    Digits
  }

  def year_precision = rule {
    Digits
  }

  def day_precision = rule {
    Digits
  }

  def ref_col = rule {
    objectName
  }

  def ref_attr = rule {
    objectName
  }

  def scope_table = rule {
    objectName
  }

  def column_name = rule {
    column
  }

  def access_driver_type = rule {
    objectName
  }

  def directory = rule {
    Letters
  }

  def opaque_format_spec = rule {
    Letters
  }

  def type_name = rule {
    objectName
  }

  def nested_item = rule {
    objectName
  }

  def storage_table = rule {
    objectName
  }

  def attribute = rule {
    objectName
  }

  def XMLSchema_URL = rule {
    Letters
  }

  def element = rule {
    objectName
  }

  def flashback_archive = rule {
    objectName
  }

  def object_type = rule {
    objectName
  }

  def hint = rule {
    Letters
  }

  def t_alias = rule {
    Letters
  }

  def query_name = rule {
    Letters
  }

  def c_alias = rule {
    Letters
  }

  def dblink = rule {
    Letters
  }

  def sample_percent = rule {
    Digits
  }

  def seed_value = rule {
    Digits
  }

  def aggregate_function = rule {
    Letters
  }

  def alias = rule {
    objectName
  }

  def hh = rule {
    Letters
  }

  def mm = rule {
    Letters
  }

  def time_zone_name = rule {
    Letters
  }

  def leading_field_precision = rule {
    Digit
  }

  def fractional_second_precision = rule {
    Digit
  }

  def filename = rule {
    Letters
  }

  def new_name = rule {
    objectName
  }

  def partition_name = rule {
    objectName
  }

  def partition_name_old = rule {
    objectName
  }

  def tablespace_name = rule {
    objectName
  }

  def edition_name = rule {
    objectName
  }

  def materialized_view = rule {
    objectName
  }

  def model = rule {
    objectName
  }

  def operator = rule {
    objectName
  }

  /**
   * Creates a rule which matches the Oracle schema object naming rules.
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/sql_elements008.htm Database Object Names]]
   */
  def objectName = rule {
    oneOrMore(Letters | Digits | Symbols)
  }

}