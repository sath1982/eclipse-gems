package de.theves.eclipse.gems.spotlight.internal.view;

import java.util.List;

public interface SpotlightItemProvider {
	List<? extends SpotlightItem> getItems();
	String getLabel();
}
