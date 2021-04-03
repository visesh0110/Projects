package database;

import java.beans.Expression;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import database.solveFunction;
import net.sf.jsqlparser.eval.Eval;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;

public class extractAggFunction extends baseClass {
	
	tableList tl;
	tableDetail td = null;
	int count;
	
	public extractAggFunction(tableList tl, baseClass res, List<SelectItem> si) throws Exception {
		// TODO Auto-generated constructor stub
		this.count = 0;
		this.tl = tl;
		tableDetail aggtd = null;
		List<rowClass> rowResult = new ArrayList<>();
		PrimitiveValue[] resrow = new PrimitiveValue[si.size()];
		
		long counter = 0;
		while(res.isNext()) {
			aggtd = res.get();
			for(rowClass rc : aggtd.rowResult) {
				counter++;
				for(int i = 0; i < si.size(); i++) {
					Eval eval = new evaluator(aggtd,tl,Arrays.asList(rc),null);
					net.sf.jsqlparser.expression.Expression e = ((SelectExpressionItem) si.get(i)).getExpression();
							Function f = (Function) e;
					PrimitiveValue PV = eval.eval(f.getParameters().getExpressions().get(0));
										if(PV instanceof DoubleValue && resrow[i] != null) {
						if(f.getName().toLowerCase().contains("count")) {
							PV = new DoubleValue(counter);
						}
						else if(f.getName().toLowerCase().contains("max")) {
							double max = PV.toDouble();
							if(max < resrow[i].toDouble()) {
								PV = resrow[i];
							}
						}
						else if(f.getName().toLowerCase().contains("min")) {
							double min = PV.toDouble();
							if(min > resrow[i].toDouble()) {
								PV = resrow[i];
							}
						}
						else if(f.getName().toLowerCase().contains("sum")) {
							double sum;
							sum = resrow[i].toDouble() + PV.toDouble();
							PV = new DoubleValue(sum);
						}
						else if(f.getName().toLowerCase().contains("avg")) {
							double avg;
							avg = resrow[i].toDouble() * (counter - 1);
							PV = new DoubleValue((PV.toDouble() + avg)/counter);
						}
					}
					resrow[i]= PV;
				}
			}
//			rowResult.addAll(aggtd.rowResult);
			
		}
		td = new tableDetail();
		copySchema(td,si);
		td.rowResult.add(new rowClass(resrow));
	}
		
	
	private void copySchema(tableDetail td, List<SelectItem> si) {
		int position = 0;
		for(SelectItem s: si) {
			net.sf.jsqlparser.expression.Expression e = ((SelectExpressionItem) s).getExpression();
			String cname = e.toString();
			if(e instanceof Column) {
				cname = ((Column) e).getColumnName();
			}
			String calias;
			if(((SelectExpressionItem) s).getAlias() != null)
				calias = ((SelectExpressionItem) s).getAlias();
			else
				calias = cname;
			td.colAliasTable.put(new Column(null,calias),position);
		}
		position++;
	}
	
	@Override
	public boolean isNext() throws Exception {
		// TODO Auto-generated method stub
		if(count == 0) {
			count =1;
			return true;
		}
		else
			return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		count = 0;
		
	}

	@Override
	public tableDetail get() throws Exception {
		// TODO Auto-generated method stub
		return td;
	}
	

}
