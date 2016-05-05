package de.theves.eclipse.gems.spotlight.internal.view;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public abstract class SpotlightItem<I> implements Comparable<SpotlightItem<I>> {
	protected SpotlightItemProvider provider;
	protected I item;

	public SpotlightItem(SpotlightItemProvider provider, I item) {
		this.provider = provider;
		this.item = item;
	}

	public I getItem() {
		return item;
	}

	@Override
	public int compareTo(SpotlightItem<I> o) {
		return getLabel().compareTo(o.getLabel());
	}

	public SpotlightItemProvider getProvider() {
		return provider;
	}

	public String getElementName() {
		return getLabel();
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

	public String getDetailsLabel() {
		return getLabel();
	}

	@Override
	public boolean equals(Object obj) {
		return getItem().equals(obj);
	}

	@Override
	public int hashCode() {
		return getItem().hashCode();
	}
}
