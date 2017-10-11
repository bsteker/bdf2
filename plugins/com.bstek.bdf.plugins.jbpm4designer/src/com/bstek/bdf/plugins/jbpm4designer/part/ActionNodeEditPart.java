package com.bstek.bdf.plugins.jbpm4designer.part;

import org.eclipse.draw2d.IFigure;

import com.bstek.bdf.plugins.jbpm4designer.figure.ActionNodeFigure;
import com.bstek.bdf.plugins.jbpm4designer.model.AbstractNodeElement;

/**
 * @author Jacky
 */
public class ActionNodeEditPart extends NodeElementEditPart {

	public ActionNodeEditPart(AbstractNodeElement node) {
		super(node);
	}
	@Override
	protected IFigure createFigure() {
		return new ActionNodeFigure(this.getModel(),this.getNodeImage());
	}
}
