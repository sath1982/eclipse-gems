package de.theves.eclipse.gems.spotlight.internal.providers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.SubContributionItem;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.ui.IWorkbenchWindow;

import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItem;
import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItemProvider;
import de.theves.eclipse.gems.spotlight.internal.view.SpotlightView.SpotlightItemsFilter;

public class ActionsProvider implements SpotlightItemProvider {
	private IWorkbenchWindow window;

	public ActionsProvider(IWorkbenchWindow window) {
		this.window = window;
	}

	@Override
	public List<SpotlightItem> getItems(SpotlightItemsFilter filter, IProgressMonitor monitor) {
		List<SpotlightItem> items = new ArrayList<>();
		IWorkbenchWindow window = this.window;
		if (window instanceof ApplicationWindow) {
			MenuManager menu = ((ApplicationWindow) window).getMenuBarManager();
			Set<ActionContributionItem> result = new HashSet<ActionContributionItem>();
			collectContributions(menu, result);
			for (ActionContributionItem action : result) {
				items.add(new ActionItem(this, action));
			}
		}
		return items;
	}

	private void collectContributions(MenuManager menu, Set<ActionContributionItem> result) {
		IContributionItem[] items = menu.getItems();
		for (int i = 0; i < items.length; i++) {
			IContributionItem item = items[i];
			if (item instanceof SubContributionItem) {
				item = ((SubContributionItem) item).getInnerItem();
			}
			if (item instanceof MenuManager) {
				collectContributions((MenuManager) item, result);
			} else if (item instanceof ActionContributionItem && item.isEnabled()) {
				result.add((ActionContributionItem) item);
			}
		}
	}

	@Override
	public String getLabel() {
		return "Actions";
	}

}
