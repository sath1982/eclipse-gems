package de.theves.eclipse.gems.metasearch.internal;

import org.eclipse.jface.resource.ImageDescriptor;

public abstract class SearchItem implements Comparable<SearchItem> {
	private SearchItemProvider provider;

	public SearchItem(SearchItemProvider provider) {
		this.provider = provider;
	}

	@Override
	public int compareTo(SearchItem o) {
		return 0;
	}

	public SearchItemProvider getProvider() {
		return provider;
	}

	public abstract String getLabel();

	public abstract ImageDescriptor getImage();

	public abstract void show();
}
