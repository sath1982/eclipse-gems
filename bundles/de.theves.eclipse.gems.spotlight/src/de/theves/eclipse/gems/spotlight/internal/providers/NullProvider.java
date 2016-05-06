package de.theves.eclipse.gems.spotlight.internal.providers;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItem;
import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItemProvider;
import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItemsFilter;

public class NullProvider implements SpotlightItemProvider<Object> {
	@Override
	public List<SpotlightItem<Object>> getItems(SpotlightItemsFilter filter, IProgressMonitor monitor) {
		// what a suprise ;-)
		return null;
	}

	@Override
	public String getLabel() {
		// what a suprise ;-)
		return null;
	}

}
