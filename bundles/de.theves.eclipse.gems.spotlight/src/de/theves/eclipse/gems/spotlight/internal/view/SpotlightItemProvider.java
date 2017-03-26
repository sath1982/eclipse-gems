package de.theves.eclipse.gems.spotlight.internal.view;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

public interface SpotlightItemProvider<I> {
	int CATEGORY_JAVA_TYPES = 0;
	int CATEGORY_RESOURCES = 1;
	int CATEGORY_COMMANDS = 2;
	int CATEGORY_ACTIONS = 3;
	int CATEGORY_VIEWS = 4;
	int CATEGORY_PERSPECTIVES = 5;
	int CATEGORY_PREFERENCES = 6;
	
	List<SpotlightItem<I>> getItems(SpotlightItemsFilter filter, IProgressMonitor monitor);

	String getLabel();
	
	int getCategory();
}
