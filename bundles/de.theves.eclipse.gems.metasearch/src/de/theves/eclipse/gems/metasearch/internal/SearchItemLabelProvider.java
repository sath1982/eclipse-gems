package de.theves.eclipse.gems.metasearch.internal;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class SearchItemLabelProvider extends LabelProvider {
	private ResourceManager resourceManager;

	@Override
	public Image getImage(Object element) {
		if (element instanceof SearchItem) {
			SearchItem item = (SearchItem) element;
			if (null != item.getImage()) {
				return (Image) getResourceManager().get(item.getImage());
			}
		}
		return super.getImage(element);
	}

	@Override
	public String getText(Object element) {
		if (element instanceof SearchItem) {
			SearchItem item = (SearchItem) element;
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
