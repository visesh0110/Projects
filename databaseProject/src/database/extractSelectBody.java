package database;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;

public class extractSelectBody extends baseClass {
	
	baseClass res = null;
	
	
	public extractSelectBody(tableList tl, SelectBody sb, Map<Column, PrimitiveValue> colResult) throws Exception {
		// TODO Auto-generated constructor stub
		if(sb instanceof PlainSelect) {
			res = new extractPlainSelect(tl,sb,colResult);
		}
	}

	@Override
	public boolean isNext() throws Exception {
		// TODO Auto-generated method stub
		if(!res.isNext() || res == null) {
			return false;
		}
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
		return res.get();
		
	}
	
}
