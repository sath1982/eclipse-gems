package de.theves.eclipse.gems.metasearch.internal;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;

public class ResourceItem extends SearchItem {
	private IResource resource;

	public ResourceItem(ResourcesProvider provider, IResource res) {
		super(provider);
		this.resource = res;
	}

	@Override
	public String getLabel() {
		return this.resource.getName();
	}

	@Override
	public ImageDescriptor getImage() {
		IWorkbenchAdapter adapter = getAdapter(resource);
		if (null == adapter) {
			return null;
		}
		return adapter.getImageDescriptor(resource);
	}

	IWorkbenchAdapter getAdapter(IResource res) {
		return res.getAdapter(IWorkbenchAdapter.class);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

}
