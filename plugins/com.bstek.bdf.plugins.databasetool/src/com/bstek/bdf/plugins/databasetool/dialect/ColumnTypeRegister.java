package com.bstek.bdf.plugins.databasetool.dialect;

import java.util.List;

public interface ColumnTypeRegister {

	public List<ColumnType> getAllColumnTypes();
	
	public String getNumberGroupPreference();

	public String getDecimalGroupPreference();

	public String getCharactorGroupPreference();

	public String getStringGroupPreference();

	public String getDateGroupPreference();

	public String getTimeGroupPreference();

	public String getBinaryLobGroupPreference();

	public String getCharactorLobGroupPreference();

}
