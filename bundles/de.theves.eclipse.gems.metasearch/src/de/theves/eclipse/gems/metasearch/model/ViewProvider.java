package de.theves.eclipse.gems.metasearch.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.IViewDescriptor;

public class ViewProvider implements SearchItemProvider {

	@Override
	public List<SearchItem> getItems() {
		IViewDescriptor[] views = PlatformUI.getWorkbench().getViewRegistry().getViews();
		List<SearchItem> items = new ArrayList<>();
		for (IViewDescriptor view : views) {
			items.add(new SearchItem() {

				@Override
				public ImageDescriptor getImage() {
					return view.getImageDescriptor();
				}

				@Override
				public String getLabel() {
					return view.getLabel();
				}

				@Override
				public int compareTo(SearchItem o) {
					return getLabel().compareTo(o.getLabel());
				}
			});
		}
		return items;
	}
	
	@Override
	public String getLabel() {
		return "Views";
	}

}
