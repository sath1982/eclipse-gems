package de.theves.eclipse.gems.metasearch.internal;

import org.eclipse.jface.resource.ImageDescriptor;

public abstract class SearchItem implements Comparable<SearchItem> {

	@Override
	public int compareTo(SearchItem o) {
		return 0;
	}

	public abstract String getLabel();

	public abstract ImageDescriptor getImage();
}
