package com.bstek.bdf.plugins.databasetool.command;

import org.eclipse.gef.commands.Command;

import com.bstek.bdf.plugins.databasetool.model.Schema;
import com.bstek.bdf.plugins.databasetool.transform.TransformSchemaDbType;

public class TransformSchemaDbTypeCommand extends Command {

	private Schema schema;
	private String destDbType;

	public TransformSchemaDbTypeCommand(Schema schema, String destDbType) {
		this.schema = schema;
		this.destDbType = destDbType;
		setLabel("Change Database");
	}

	@Override
	public boolean canExecute() {
		return schema != null && destDbType != null;
	}

	@Override
	public boolean canUndo() {
		return false;
	}

	@Override
	public void execute() {
		redo();
	}

	@Override
	public void redo() {
		TransformSchemaDbType.getInstance(schema, destDbType).transform();
	}

}
