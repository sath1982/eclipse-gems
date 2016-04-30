package de.theves.eclipse.gems.metasearch.model;

import java.util.List;

public interface SearchItemProvider {
	List<SearchItem> getItems();
	String getLabel();
}
