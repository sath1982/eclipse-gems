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
	public int compareTo(SpotlightItem<I> i2) {
		if (getProvider().getCategory() != i2.getProvider().getCategory()) {
			return getProvider().getCategory() - i2.getProvider().getCategory();
		}
		return getLabel().compareTo(i2.getLabel());
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SpotlightItem<?> other = (SpotlightItem<?>) obj;
		if (item == null) {
			if (other.item != null)
				return false;
		} else if (!item.equals(other.item))
			return false;
		return true;
	}

}
