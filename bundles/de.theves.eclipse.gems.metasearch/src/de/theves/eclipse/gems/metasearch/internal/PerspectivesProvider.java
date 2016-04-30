package de.theves.eclipse.gems.metasearch.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.PlatformUI;

public class PerspectivesProvider implements SearchItemProvider {

	@Override
	public List<SearchItem> getItems() {
		IPerspectiveDescriptor[] perspectives = PlatformUI.getWorkbench().getPerspectiveRegistry().getPerspectives();
		List<SearchItem> items = new ArrayList<>();
		for (IPerspectiveDescriptor perspective : perspectives) {
			items.add(new SearchItem() {
				@Override
				public ImageDescriptor getImage() {
					return perspective.getImageDescriptor();
				}

				@Override
				public String getLabel() {
					return perspective.getLabel();
				}
			});
		}
		return items;
	}

	@Override
	public String getLabel() {
		return "Perspectives";
	}

}
