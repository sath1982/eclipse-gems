package de.theves.eclipse.gems.metasearch.internal;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.resource.ImageDescriptor;

public class ActionItem extends SearchItem {
	private ActionContributionItem action;

	public ActionItem(ActionsProvider provider, ActionContributionItem action) {
		super(provider);
		this.action = action;
	}

	@Override
	public String getLabel() {
		return action.getAction().getText();
	}

	@Override
	public ImageDescriptor getImage() {
		return action.getAction().getImageDescriptor();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
	}

}
