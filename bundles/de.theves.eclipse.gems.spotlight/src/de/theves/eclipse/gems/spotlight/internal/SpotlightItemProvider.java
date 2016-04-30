package de.theves.eclipse.gems.spotlight.internal;

import java.util.List;

public interface SpotlightItemProvider {
	List<SpotlightItem> getItems();
	String getLabel();
}
