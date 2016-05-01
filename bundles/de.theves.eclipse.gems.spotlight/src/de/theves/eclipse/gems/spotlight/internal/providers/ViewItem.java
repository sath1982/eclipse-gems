package de.theves.eclipse.gems.spotlight.internal.providers;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.IViewDescriptor;

import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItem;

public class ViewItem extends SpotlightItem {
	private IViewDescriptor view;

	public ViewItem(ViewProvider provider, IViewDescriptor view) {
		super(provider);
		this.view = view;
	}

	@Override
	public String getLabel() {
		return this.view.getLabel();
	}

	@Override
	public ImageDescriptor doGetImage() {
		return this.view.getImageDescriptor();
	}

	@Override
	public void show() {
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(this.view.getId());
		} catch (PartInitException e) {
			// give up
			return;
		}
	}

}
