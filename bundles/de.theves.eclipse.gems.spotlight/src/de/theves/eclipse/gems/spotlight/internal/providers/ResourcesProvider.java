package de.theves.eclipse.gems.spotlight.internal.providers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.model.IWorkbenchAdapter;

import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItem;
import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItemProvider;
import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItemsFilter;

public class ResourcesProvider implements SpotlightItemProvider {

	@Override
	public int getCategory() {
		return CATEGORY_RESOURCES;
	}

	@Override
	public List<SpotlightItem<?>> getItems(SpotlightItemsFilter filter, IProgressMonitor monitor) {
		List<SpotlightItem<?>> items = new ArrayList<>();
		try {
			ResourcesPlugin.getWorkspace().getRoot().accept(new IResourceVisitor() {
				@Override
				public boolean visit(IResource resource) throws CoreException {
					if (resource.getType() == IResource.FILE) {
						items.add(new ResourceItem(ResourcesProvider.this, (IFile) resource));
					}
					return resource.getType() != IResource.FILE;
				}
			});
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			monitor.done();
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
