package database;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SubSelect;

public class extractSubSelect extends baseClass {

	baseClass res = null;
	tableList tl = null;
	tableDetail td = null;
	SubSelect ss = null;
	
	
	public extractSubSelect(SubSelect ss, tableList tl) throws Exception {
		this.tl = tl;
		this.ss = ss;
		res = new extractSelectBody(tl, ss.getSelectBody(), null);
		
		

	}
	
	public extractSubSelect(tableList tl, SubSelect ss, Map<Column, PrimitiveValue> colResult) throws Exception {
		// TODO Auto-generated constructor stub
		this.tl = tl;
		this.ss = ss;
		res = new extractSelectBody(tl,ss.getSelectBody(),colResult);
	}

	@Override
	public boolean isNext() throws Exception {
		// TODO Auto-generated method stub
		if(res == null || !res.isNext())
			return false;
		else
			return true;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		res.clear();
	}

	@Override
	public tableDetail get() throws Exception {
		tableDetail oldtd = res.get();
		if(td == null) {
			td = new tableDetail();
			addSubSelectInfo(oldtd,td);
		}
		td.rowResult = oldtd.rowResult;
		return td;
	}
		
		
	
private void addSubSelectInfo(tableDetail oldtd, tableDetail td) {
		String tableAliasName = ss.getAlias();
		//  tableInformation ti = new tableInformation(new Table(tableAliasName),  oldcd.colCD.values());
		//  tl.tableInfo.put(tableAliasName, ti);
		td.table.add(new Table(tableAliasName));
		td.tableAlias.put(tableAliasName.toUpperCase(),tableAliasName.toUpperCase());
		for(Map.Entry<Column, Integer> e : oldtd.colAliasTable.entrySet()) {
			Column c = new Column(new Table(tableAliasName), e.getKey().getColumnName());
			td.colAliasTable.put(c, e.getValue());
		}
		td.colCD = oldtd.colCD;
	}

}
	

