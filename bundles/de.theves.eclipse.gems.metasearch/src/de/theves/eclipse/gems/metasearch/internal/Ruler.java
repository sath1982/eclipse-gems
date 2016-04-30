package de.theves.eclipse.gems.metasearch.internal;

import org.eclipse.jface.resource.ImageDescriptor;

public class Ruler extends SearchItem {

	private String headline;

	public Ruler(String headline) {
		super(new NullProvider());
		this.headline = headline;
	}

	@Override
	public ImageDescriptor getImage() {
		return null;
	}

	@Override
	public String getLabel() {
		return "--------------------------------------" + this.headline + "--------------------------------------";
	}

	@Override
	public void show() {
		// nothing to show here
	}

}
