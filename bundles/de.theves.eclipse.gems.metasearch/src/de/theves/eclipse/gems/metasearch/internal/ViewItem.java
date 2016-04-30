package de.theves.eclipse.gems.metasearch.internal;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.IViewDescriptor;

public class ViewItem extends SearchItem {
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
	public ImageDescriptor getImage() {
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
