package de.theves.eclipse.gems.spotlight.internal.view;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

public interface SpotlightItemProvider {
	List<? extends SpotlightItem> getItems(SpotlightItemsFilter filter, IProgressMonitor monitor);

	String getLabel();
}
