package de.theves.eclipse.gems.spotlight.internal.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class SpotlightItemContentProvider implements IStructuredContentProvider {
	private static final Object[] EMPTY_ARRAY = new Object[0];
	private SpotlightItemsFilter filter;
	private IProgressMonitor monitor;

	public SpotlightItemContentProvider(SpotlightItemsFilter filter) {
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
		// not interested
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (this.filter != null && this.filter.getPattern() != null && !this.filter.getPattern().isEmpty()) {
			if (inputElement instanceof SpotlightDialogInput) {
				List<SpotlightItem> result = new ArrayList<>();
				SpotlightItemProvider[] providers = ((SpotlightDialogInput) inputElement).getProviders();
				IProgressMonitor safeMonitor = monitor != null ? monitor : new NullProgressMonitor();
				try {
					safeMonitor.beginTask(null, providers.length);
					for (SpotlightItemProvider provider : providers) {
						List<SpotlightItem> providerItems = provider.getItems(this.filter,
								new SubProgressMonitor(safeMonitor, 1));
						result.addAll(providerItems);
					}
					return result.toArray(new SpotlightItem[result.size()]);
				} finally {
					safeMonitor.done();
				}
			}
		}
		return EMPTY_ARRAY;
	}

	public void setMonitor(IProgressMonitor monitor) {
		this.monitor = monitor;
	}

}
