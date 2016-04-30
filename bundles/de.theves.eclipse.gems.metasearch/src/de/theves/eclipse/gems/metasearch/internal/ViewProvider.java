package de.theves.eclipse.gems.metasearch.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.IViewDescriptor;

public class ViewProvider implements SearchItemProvider {

	@Override
	public List<SearchItem> getItems() {
		IViewDescriptor[] views = PlatformUI.getWorkbench().getViewRegistry().getViews();
		List<SearchItem> items = new ArrayList<>();
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
