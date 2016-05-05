package de.theves.eclipse.gems.spotlight.internal.view;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class SpotlightItemTreeContentProvider implements ITreeContentProvider {
	private static final Object[] EMPTY_ARRAY = new Object[0];
	private SpotlightItemsFilter filter;

	public SpotlightItemTreeContentProvider(SpotlightItemsFilter filter) {
		this.filter = filter;
	}
	
	public void setFilter(SpotlightItemsFilter filter) {
		this.filter = filter;
	}

	@Override
	public void dispose() {
		// nothing to dispose here
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// not used
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof SpotlightDialogInput) {
			SpotlightDialogInput input = (SpotlightDialogInput) inputElement;
			return input.getProviders();
		}
		return EMPTY_ARRAY;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof SpotlightItemProvider) {
			SpotlightItemProvider provider = (SpotlightItemProvider) parentElement;
			return provider.getItems(filter, new NullProgressMonitor()).toArray();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof SpotlightItem) {
			return ((SpotlightItem) element).getProvider();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return element instanceof SpotlightItemProvider;
	}

}
