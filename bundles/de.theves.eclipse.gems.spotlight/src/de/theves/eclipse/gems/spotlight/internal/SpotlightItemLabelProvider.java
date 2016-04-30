package de.theves.eclipse.gems.spotlight.internal;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class SpotlightItemLabelProvider extends LabelProvider {
	private ResourceManager resourceManager;

	@Override
	public Image getImage(Object element) {
		if (element instanceof SpotlightItem) {
			SpotlightItem item = (SpotlightItem) element;
			if (null != item.getImage()) {
				return (Image) getResourceManager().get(item.getImage());
			}
		}
		return super.getImage(element);
	}

	@Override
	public String getText(Object element) {
		if (element instanceof SpotlightItem) {
			SpotlightItem item = (SpotlightItem) element;
			return item.getLabel() + " - " + item.getProvider().getLabel();
		}
		return super.getText(element);
	}

	public void dispose() {
		if (null != resourceManager) {
			resourceManager.dispose();
		}
		super.dispose();
	};

	ResourceManager getResourceManager() {
		if (resourceManager == null) {
			resourceManager = new LocalResourceManager(JFaceResources.getResources());
		}
		return resourceManager;
	}
}
