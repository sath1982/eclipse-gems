package de.theves.eclipse.gems.spotlight.internal.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.viewers.ILazyContentProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

public class CachingContentProvider implements IStructuredContentProvider, ILazyContentProvider {
	private List<SpotlightItem<?>> elements;
	private TableViewer viewer;
	private final SpotlightItemProvider[] providers;
	private SpotlightItemsFilter lastUsedFilter;
	private boolean reset;

	public CachingContentProvider(TableViewer viewer, SpotlightItemProvider[] providers) {
		this.viewer = viewer;
		this.elements = new ArrayList<>();
		this.providers = providers;
		this.reset = false;
	}

	@Override
	public void dispose() {
		// nothing
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// not used
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return elements.toArray();
	}

	public void reloadCache(SpotlightItemsFilter filter, IProgressMonitor monitor) {
		// TODO check if the given filter is a sub-filter of the last one and
		// just filter the last search result
		this.lastUsedFilter = filter;
		listItems(filter, monitor);
	}

	private void listItems(SpotlightItemsFilter filter, IProgressMonitor monitor) {
		monitor.beginTask(null, providers.length * 100);
		reset();
		try {
			for (SpotlightItemProvider provider : providers) {
				List<SpotlightItem<?>> items = provider.getItems(filter, new SubProgressMonitor(monitor, 100));
				for (SpotlightItem<?> spotlightItem : items) {
					addElement(spotlightItem, filter);
				}
				if (monitor.isCanceled() || !reset) {
					return;
				}
			}
			sort();
		} catch (OperationCanceledException e) {
			return;
		} finally {
			monitor.done();
		}
	}

	private void addElement(SpotlightItem<?> item, SpotlightItemsFilter filter) {
		if (filter.matches(item.getElementName())) {
			elements.add(item);
		}
	}

	private void sort() {
		Collections.sort(elements);
	}

	public void reset() {
		elements.clear();
		this.reset = true;
	}

	public int getNumberOfElements() {
		return elements.size();
	}

	@Override
	public void updateElement(int index) {
		viewer.replace((elements.size() > index) ? elements.get(index) : null, index);
	}

	public SpotlightItem<?> getElement(int i) {
		return elements.get(i);
	}

	public void stopReloadProgress() {
		this.reset = false;
	}

}
