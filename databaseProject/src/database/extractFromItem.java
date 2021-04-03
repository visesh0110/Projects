package database;

import java.io.FileNotFoundException;

import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.SubSelect;

public class extractFromItem extends baseClass {
	
	baseClass res = null;
	
	public extractFromItem(FromItem fm, tableList tl) throws Exception {
		
		if(fm instanceof Table) {
			res = new extractTable(fm,tl);
		}
		
		if(fm instanceof SubSelect) {
			SubSelect ss = (SubSelect) fm;
			res = new extractSubSelect(ss,tl);
		}
		
		
	}
	@Override
	public boolean isNext() throws Exception {
		// TODO Auto-generated method stub
		if(!res.isNext() || res == null) 
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
		// TODO Auto-generated method stub
		return res.get();
	}
	

}
