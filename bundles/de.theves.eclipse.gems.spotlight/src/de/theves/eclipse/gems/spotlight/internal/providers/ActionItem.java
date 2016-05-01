package de.theves.eclipse.gems.spotlight.internal.providers;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.resource.ImageDescriptor;

import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItem;

public class ActionItem extends SpotlightItem {
	private ActionContributionItem action;

	public ActionItem(ActionsProvider provider, ActionContributionItem action) {
		super(provider);
		this.action = action;
	}

	@Override
	public String getLabel() {
		return this.action.getAction().getText();
	}

	@Override
	public ImageDescriptor doGetImage() {
		return action.getAction().getImageDescriptor();
	}

	@Override
	public void show() {
		this.action.getAction().run();
	}

}
