package de.theves.eclipse.gems.metasearch.internal;

import java.util.List;

public interface SearchItemProvider {
	List<SearchItem> getItems();
	String getLabel();
}
