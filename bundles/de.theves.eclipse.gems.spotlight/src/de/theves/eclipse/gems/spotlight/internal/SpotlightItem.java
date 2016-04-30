package de.theves.eclipse.gems.spotlight.internal;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

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

	public ImageDescriptor getImage() {
		ImageDescriptor image = doGetImage();
		if (null != image) {
			return image;
		}
		// default image if no other is provided
		return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ELEMENT);
	};

	public abstract ImageDescriptor doGetImage();

	public abstract void show();
}
