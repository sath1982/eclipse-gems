package de.theves.eclipse.gems.spotlight.internal.providers;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.IViewDescriptor;

import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItem;

public class ViewItem extends SpotlightItem<IViewDescriptor> {

	public ViewItem(ViewProvider provider, IViewDescriptor view) {
		super(provider, view);
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
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(getItem().getId());
		} catch (PartInitException e) {
			// give up
			return;
		}
	}

}
