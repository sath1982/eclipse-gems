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
	public int getCategory() {
		return CATEGORY_PERSPECTIVES;
	}

	@Override
	public List<SpotlightItem<?>> getItems(SpotlightItemsFilter filter, IProgressMonitor monitor) {
		try {
			IPerspectiveDescriptor[] perspectives = PlatformUI.getWorkbench().getPerspectiveRegistry()
					.getPerspectives();
			monitor.beginTask(null, perspectives.length);
			List<SpotlightItem<?>> items = new ArrayList<>();
			for (IPerspectiveDescriptor perspective : perspectives) {
				items.add(new PerspectiveItem(this, perspective));
				monitor.worked(1);
			}
			return items;
		} finally {
			monitor.done();
		}
	}

	@Override
	public String getLabel() {
		return "Perspectives";
	}

}
