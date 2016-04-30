package de.theves.eclipse.gems.metasearch.internal;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPerspectiveDescriptor;

public class PerspectiveItem extends SearchItem {
	private IPerspectiveDescriptor perspective;

	public PerspectiveItem(PerspectivesProvider provider, IPerspectiveDescriptor perspective) {
		super(provider);
		this.perspective = perspective;
	}

	@Override
	public String getLabel() {
		return this.perspective.getLabel();
	}

	@Override
	public ImageDescriptor getImage() {
		return this.perspective.getImageDescriptor();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

}
