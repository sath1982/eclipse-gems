package de.theves.eclipse.gems.spotlight.internal.view;

import org.eclipse.jface.resource.ImageDescriptor;

import de.theves.eclipse.gems.spotlight.internal.providers.NullProvider;

public class Ruler extends SpotlightItem {

	private String headline;

	public Ruler(String headline) {
		super(new NullProvider());
		this.headline = headline;
	}

	@Override
	public ImageDescriptor getImage() {
		// no images for rulers
		return null;
	}

	@Override
	public ImageDescriptor doGetImage() {
		return null;
	}

	@Override
	public String getLabel() {
		return "--------------------------------------" + this.headline + "--------------------------------------";
	}
	
	@Override
	public String getElementName() {
		return getLabel();
	}

	@Override
	public void show() {
		// nothing to show here
	}

}
