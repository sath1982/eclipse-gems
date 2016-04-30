package de.theves.eclipse.gems.spotlight.internal;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.PlatformUI;

public class PerspectiveItem extends SpotlightItem {
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
		try {
			PlatformUI.getWorkbench().showPerspective(this.perspective.getId(),
					PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		} catch (Exception e) {
			// give up
			return;
		}
	}

}
