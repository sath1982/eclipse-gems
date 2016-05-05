package de.theves.eclipse.gems.spotlight.internal.providers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.PlatformUI;

import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItem;
import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItemProvider;
import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItemsFilter;

public class PerspectivesProvider implements SpotlightItemProvider {

	@Override
	public List<SpotlightItem<IPerspectiveDescriptor>> getItems(SpotlightItemsFilter filter, IProgressMonitor monitor) {
		IPerspectiveDescriptor[] perspectives = PlatformUI.getWorkbench().getPerspectiveRegistry().getPerspectives();
		List<SpotlightItem<IPerspectiveDescriptor>> items = new ArrayList<>();
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
