package de.theves.eclipse.gems.spotlight.internal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.SubContributionItem;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchWindow;

public class ActionsProvider implements SpotlightItemProvider {
	private IWorkbenchWindow window;

	public ActionsProvider(IWorkbenchWindow window) {
		this.window = window;
	}

	@Override
	public List<SpotlightItem> getItems() {
		List<SpotlightItem> items = new ArrayList<>();
		IWorkbenchWindow window = this.window;
		if (window instanceof WorkbenchWindow) {
			MenuManager menu = ((WorkbenchWindow) window).getMenuManager();
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
