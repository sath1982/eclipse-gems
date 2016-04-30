package de.theves.eclipse.gems.metasearch.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;

public class ResourcesProvider implements SearchItemProvider {

	@Override
	public List<SearchItem> getItems() {
		List<SearchItem> items = new ArrayList<>();
		try {
			ResourcesPlugin.getWorkspace().getRoot().accept(new IResourceVisitor() {
				@Override
				public boolean visit(IResource resource) throws CoreException {
					if (resource.getType() == IResource.FILE) {
						items.add(new SearchItem() {
							@Override
							public ImageDescriptor getImage() {
								IWorkbenchAdapter adapter = getAdapter(resource);
								if (null == adapter) {
									return null;
								}
								return adapter.getImageDescriptor(resource);
							}

							public String getLabel() {
								return resource.getName();
							};
						});
					}
					return resource.getType() != IResource.FILE;
				}
			});
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return items;
	}

	IWorkbenchAdapter getAdapter(IResource res) {
		return res.getAdapter(IWorkbenchAdapter.class);
	}

	@Override
	public String getLabel() {
		return "Resources";
	}

}
