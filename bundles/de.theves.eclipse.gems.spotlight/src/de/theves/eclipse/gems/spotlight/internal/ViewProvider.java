package de.theves.eclipse.gems.spotlight.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.IViewDescriptor;

public class ViewProvider implements SpotlightItemProvider {

	@Override
	public List<SpotlightItem> getItems() {
		IViewDescriptor[] views = PlatformUI.getWorkbench().getViewRegistry().getViews();
		List<SpotlightItem> items = new ArrayList<>();
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
