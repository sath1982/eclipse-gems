package de.theves.eclipse.gems.spotlight.internal.providers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.IViewDescriptor;

import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItem;
import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItemProvider;
import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItemsFilter;

public class ViewProvider implements SpotlightItemProvider {

	@Override
	public int getCategory() {
		return CATEGORY_VIEWS;
	}

	@Override
	public List<SpotlightItem<?>> getItems(SpotlightItemsFilter filter, IProgressMonitor monitor) {
		IViewDescriptor[] views = PlatformUI.getWorkbench().getViewRegistry().getViews();
		List<SpotlightItem<?>> items = new ArrayList<>();
		for (IViewDescriptor view : views) {
			items.add(new ViewItem(this, view));
		}
		return items;
	}

	@Override
	public String getLabel() {
		return "Views";
	}

}
