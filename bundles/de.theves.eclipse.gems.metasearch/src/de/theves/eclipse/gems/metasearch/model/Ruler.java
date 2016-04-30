package de.theves.eclipse.gems.metasearch.model;

import org.eclipse.jface.resource.ImageDescriptor;

public class Ruler extends SearchItem {

	private String headline;

	public Ruler(String headline) {
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

}
