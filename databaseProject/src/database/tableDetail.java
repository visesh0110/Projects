package database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;

import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;

public class tableDetail  implements Cloneable {
	
	public List<rowClass> rowResult = new ArrayList<>();
	public Map<String, String> tableAlias = new LinkedHashMap<>();
	public Map<Column, Integer> colAliasTable = new LinkedHashMap<>();
	public List<Table> table = new ArrayList<>();
	public Map<String, ColumnDefinition> colCD = new LinkedHashMap<>();
	
	 @Override
	    public Object clone() throws CloneNotSupportedException {
	        return super.clone();
	    }
}
