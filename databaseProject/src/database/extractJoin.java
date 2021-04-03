package database;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.apache.commons.collections4.CollectionUtils;

import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.schema.Column;

public class extractJoin extends baseClass {
	baseClass r1 = null;
	baseClass r2 = null;
	tableDetail oldTD = null;
	tableDetail newTD = null;
	tableDetail temp = null;
	
	
	public extractJoin(baseClass r1, baseClass r2) {
		this.r1 = r1;
		this.r2 = r2;
	}


	@Override
	public boolean isNext() throws Exception {
		// TODO Auto-generated method stub
		if(oldTD == null && r1.isNext()) {
			oldTD = r1.get();
		}
		if(r1 == null || r2 == null) {
			return false;
		}
		if(!r2.isNext()) {
			r2.clear();
			if(!r1.isNext()) 
				return false;
			oldTD = r1.get();	
		}
		temp = r2.get();
		if(newTD == null) {
			newTD = new tableDetail();
			newTD.tableAlias.putAll(oldTD.tableAlias);
			newTD.tableAlias.putAll(temp.tableAlias);
			newTD.colCD.putAll(oldTD.colCD);
			newTD.colCD.putAll(temp.colCD);
			newTD.table.addAll(oldTD.table);
			newTD.table.addAll(temp.table);
			newTD.colAliasTable.putAll(oldTD.colAliasTable);
			int pos = newTD.colAliasTable.size();
			for (Entry<Column, Integer> e : temp.colAliasTable.entrySet()) {
				newTD.colAliasTable.put(e.getKey(), e.getValue() + pos);
			}
		}
		newTD.rowResult = new ArrayList<>();
		
		for(rowClass rc1 : oldTD.rowResult) {
			for(rowClass rc2 : temp.rowResult) {
				rowClass rc3 = extraMethods.joinArray(rc1.row, rc2.row);
				newTD.rowResult.add(new rowClass(rc3.row));
			}
		}
		return CollectionUtils.isNotEmpty(newTD.rowResult);
	}
		
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		r1.clear();
		r2.clear();
	}


	@Override
	public tableDetail get() throws Exception {
		// TODO Auto-generated method stub
		return newTD;
	}
}

