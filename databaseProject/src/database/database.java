package database;

import database.extraMethods;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import net.sf.jsqlparser.eval.Eval;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.BooleanValue;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;

import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.parser.ParseException;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.PrimitiveType;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.Union;

public class database {

	public static void main(String[] args) throws Exception {

		String sql_path = args[1];
		tableList tl = new tableList();
		tl.table_path = args[0];
		List<String> query = extraMethods.extractQuery(sql_path); // function to extract each query from sql file.
		for (String stat : query) {

			Reader reader = new StringReader(stat);
			CCJSqlParser parser = new CCJSqlParser(reader);
			try {
				Statement st = parser.Statement();
				if (st instanceof CreateTable) {
					tableSchema(st, tl);
				} else if (st instanceof Select) {
					Select s = (Select) st;
					baseClass res = new extractSelectBody(tl, s.getSelectBody(), null);
					while (res.isNext()) {
						List<rowClass> pvlist = res.get().rowResult;
						for (rowClass pvarr : pvlist) {
							for (PrimitiveValue pv : pvarr.row) {
								System.out.print(pv.toRawString() + "|");
							}
							System.out.println();
						}
					}
					System.out.println("=");

				}
			}

			catch (ParseException e) {
				e.printStackTrace();
			}
		}

	}

	static void tableSchema(Statement statement, tableList tl) {
		CreateTable ct = (CreateTable) statement;
		String name = ct.getTable().getName();
		Table t = new Table(name);
		List<ColumnDefinition> cd = ct.getColumnDefinitions();
		tableInformation ti = new tableInformation(t, cd);
		tl.tableInfo.put(name.toUpperCase(), ti);
	}
}
