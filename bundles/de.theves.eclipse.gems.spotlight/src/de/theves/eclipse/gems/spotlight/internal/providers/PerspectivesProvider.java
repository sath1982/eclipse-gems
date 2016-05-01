package de.theves.eclipse.gems.spotlight.internal.providers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.PlatformUI;

import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItem;
import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItemProvider;

public class PerspectivesProvider implements SpotlightItemProvider {

	@Override
	public List<SpotlightItem> getItems() {
		IPerspectiveDescriptor[] perspectives = PlatformUI.getWorkbench().getPerspectiveRegistry().getPerspectives();
		List<SpotlightItem> items = new ArrayList<>();
		for (IPerspectiveDescriptor perspective : perspectives) {
			items.add(new PerspectiveItem(this, perspective));
		}
		return items;
	}

	@Override
	public String getLabel() {
		return "Perspectives";
	}

}
