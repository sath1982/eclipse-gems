package de.theves.eclipse.gems.spotlight.internal.providers;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.resource.ImageDescriptor;

import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItem;

public class ActionItem extends SpotlightItem<ActionContributionItem> {

	public ActionItem(ActionsProvider provider, ActionContributionItem action) {
		super(provider, action);
	}

	@Override
	public String getLabel() {
		return getItem().getAction().getText();
	}

	@Override
	public ImageDescriptor doGetImage() {
		return getItem().getAction().getImageDescriptor();
	}

	@Override
	public void show() {
		getItem().getAction().run();
	}

}
