package de.theves.eclipse.gems.spotlight.internal.view;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

public interface SpotlightItemProvider<T> {
	List<SpotlightItem<T>> getItems(SpotlightItemsFilter filter, IProgressMonitor monitor);

	String getLabel();
}
