package de.theves.eclipse.gems.spotlight.internal;

import org.eclipse.jface.resource.ImageDescriptor;

public abstract class SpotlightItem implements Comparable<SpotlightItem> {
	protected SpotlightItemProvider provider;

	public SpotlightItem(SpotlightItemProvider provider) {
		this.provider = provider;
	}

	@Override
	public int compareTo(SpotlightItem o) {
		return 0;
	}

	public SpotlightItemProvider getProvider() {
		return provider;
	}

	public abstract String getLabel();

	public abstract ImageDescriptor getImage();

	public abstract void show();
}
