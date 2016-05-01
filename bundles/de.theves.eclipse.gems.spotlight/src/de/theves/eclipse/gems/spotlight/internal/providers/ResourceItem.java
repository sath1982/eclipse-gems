package de.theves.eclipse.gems.spotlight.internal.providers;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.part.FileEditorInput;

import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItem;

public class ResourceItem extends SpotlightItem {
	private IFile resource;

	public ResourceItem(ResourcesProvider provider, IFile res) {
		super(provider);
		this.resource = res;
	}

	@Override
	public String getLabel() {
		return this.resource.getName();
	}

	@Override
	public String getDetailsLabel() {
		return this.resource.getFullPath().toString();
	}

	@Override
	public ImageDescriptor doGetImage() {
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
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry()
				.getDefaultEditor(this.resource.getName());
		if (desc != null) {
			try {
				IEditorInput input = new FileEditorInput(this.resource);
				page.openEditor(input, desc.getId());
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
