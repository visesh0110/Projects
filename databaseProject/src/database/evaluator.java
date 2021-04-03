package database;

import database.solveFunction;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.CollectionUtils;

import net.sf.jsqlparser.eval.Eval;
import net.sf.jsqlparser.expression.BooleanValue;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.select.SubSelect;

public class evaluator extends Eval {
	
	public tableDetail td;
	public tableList tl;
	public Map<Column, PrimitiveValue> colResult;
	public List<rowClass> rowResult;
	public List<PrimitiveValue> inRowResult;
	
	public evaluator(tableDetail td, tableList tl, List<rowClass> rowResult ,Map<Column, PrimitiveValue> colResult) {
		this.td = td;
		this.colResult = colResult;
		this.tl = tl;
		this.rowResult = rowResult;
		this.inRowResult = new ArrayList<>();
	}
	
	

	
	@Override
	public PrimitiveValue eval(Function f) throws SQLException {
		try {
			PrimitiveValue pv = solveFunction.mathFunction(tl,td,f,rowResult);
			return pv;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
	}
			
	@Override
	public PrimitiveValue eval(Column c) throws SQLException {
		// TODO Auto-generated method stub
		Table t = c.getTable();
		if(t.getName() == null || t == null) {
			for (Column col : td.colAliasTable.keySet()) {
				if(col.getColumnName().equals(c.getColumnName()))
					t = col.getTable();
			}
			c.setTable(t);
		}
		

		int pos = td.colAliasTable.get(c);
		


		if(td.colAliasTable.get(c) == null) {
			return colResult.get(c);
		}
		return rowResult.get(0).row[pos];
	}
	
	@Override
	public PrimitiveValue eval(Between b) throws SQLException {
		if(b.getLeftExpression() instanceof Column) {
			Column c = (Column) b.getLeftExpression();
			Table t = c.getTable();
			if(t.getName() == null || t == null) {
				for (Column col : td.colAliasTable.keySet()) {
					if(col.getColumnName().equals(c.getColumnName()))
						t = col.getTable();
				}
				c.setTable(t);
			}
			String tableName = td.tableAlias.get(t.getName().toUpperCase());
			ColumnDefinition cd = tl.tableInfo.get(tableName).colCD.get(c.getColumnName());
			String str = cd.getColDataType().getDataType().toUpperCase();
			DataType dt = DataType.valueOf(str);
			if(dt.equals(DataType.DATE)) {
				String s1;
				String s2;
				s1 = b.getBetweenExpressionEnd().toString().replace("'", "");
				s2 = b.getBetweenExpressionStart().toString().replace("'", "");
				b.setBetweenExpressionEnd(new DateValue(s1));
				b.setBetweenExpressionStart(new DateValue(s2));
			}
			
		}
		return super.eval(b);
	}
	
	@Override
	public PrimitiveValue eval(InExpression in) throws SQLException {
		
		if(in.getItemsList() instanceof SubSelect) {
			if(CollectionUtils.isEmpty(inRowResult)) {
				SubSelect ss = (SubSelect) in.getItemsList();
				baseClass res = null;
				try {
					res = new extractSubSelect(ss,tl);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					while(res.isNext()) {
						for (rowClass pv : res.get().rowResult) {
							inRowResult.add(pv.row[0]);
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else {
			List<Expression> exp = ((ExpressionList) in.getItemsList()).getExpressions(); 
			for(Expression e : exp) {
				Eval evalIn = new evaluator(td,tl,rowResult,null);
				inRowResult.add(evalIn.eval(e));
			}
		}
		Eval leftEval = new evaluator(td,tl,rowResult,null);
		PrimitiveValue leftPv = leftEval.eval(in.getLeftExpression());
		if(inRowResult.contains(leftPv)) 
			return BooleanValue.TRUE;
		else
			return BooleanValue.FALSE;
	}
	
	
	@Override
	public PrimitiveValue eval(ExistsExpression exist) throws SQLException {
		Eval eval = new evaluator(td,tl,rowResult,colResult);
		PrimitiveValue pv = null;
		if(exist.getRightExpression() instanceof SubSelect)
		{
			try {
				pv = subSelectEval((SubSelect) exist.getRightExpression());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return pv;
		}
		else
			return eval.eval(exist.getRightExpression());
	}
		
	public PrimitiveValue subSelectEval(SubSelect ss) throws Exception {
		
		Map<Column, PrimitiveValue> colResult = new LinkedHashMap<>();
		for(Entry<Column, Integer> e : td.colAliasTable.entrySet()) {
			colResult.put(e.getKey(), rowResult.get(0).row[e.getValue()]);
		}
		baseClass res = new extractSubSelect(tl,ss,colResult);
		if(res.isNext()) 
			return BooleanValue.TRUE;
		else
			return BooleanValue.FALSE;
	}

		
	
}
