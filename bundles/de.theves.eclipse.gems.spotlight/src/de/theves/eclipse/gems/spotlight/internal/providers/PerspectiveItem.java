package de.theves.eclipse.gems.spotlight.internal.providers;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.PlatformUI;

import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItem;

public class PerspectiveItem extends SpotlightItem<IPerspectiveDescriptor> {

	public PerspectiveItem(PerspectivesProvider provider, IPerspectiveDescriptor perspective) {
		super(provider, perspective);
	}

	@Override
	public String getLabel() {
		return getItem().getLabel();
	}

	@Override
	public ImageDescriptor doGetImage() {
		return getItem().getImageDescriptor();
	}

	@Override
	public void show() {
		try {
			PlatformUI.getWorkbench().showPerspective(getItem().getId(),
					PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		} catch (Exception e) {
			// give up
			return;
		}
	}

}
