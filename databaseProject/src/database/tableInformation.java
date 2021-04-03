package database;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;

import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;

public class tableInformation {
	public Table t = new Table();	// table class 
	public List<ColumnDefinition> cd = new  ArrayList<>();	//array of ColumnDefinition class 
	public Map<String, ColumnDefinition > colCD = new LinkedHashMap<>();
	public List<Column> col = new ArrayList<>();
	
	public tableInformation(Table t, List<ColumnDefinition> CD) {			//Constructor
		this.t = t;
		
		int index = 0;
		for(ColumnDefinition temp: CD) {
			Column c = new Column(t, temp.getColumnName());
			colCD.put(c.getColumnName(), temp);
			cd.add(temp);
			col.add(c);
			
			
		}
	}
	public tableInformation(Table t, Collection<ColumnDefinition> CD) {
		// TODO Auto-generated constructor stub
        this.t = t;
		
		int index = 0;
		for(ColumnDefinition temp: CD) {
			Column c = new Column(t, temp.getColumnName());
			colCD.put(c.getColumnName(), temp);
			cd.add(temp);
			col.add(c);
	}
	}
	public boolean hasColumn(String colName) {
		for (Column c: this.col) { 
			if(c.getColumnName().equals(colName)) {
				return true;
			}
		}
		return false;
		
	}
	
	}

