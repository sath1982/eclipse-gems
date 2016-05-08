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
	private boolean reset;

	public CachingContentProvider(TableViewer viewer, SpotlightItemProvider[] providers) {
		this.viewer = viewer;
		this.elements = Collections.synchronizedList(new ArrayList<>());
		this.providers = providers;
		this.reset = true;
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
		reset = false;
		// TODO only re-run the search if the new filter is not a sub filter of
		// the
		// previously used one
		if (filter.getPattern() != null && !filter.getPattern().isEmpty()) {
			listItems(filter, monitor);
			sort();
		}
	}

	private void listItems(SpotlightItemsFilter filter, IProgressMonitor monitor) {
		monitor.beginTask(null, providers.length * 100);
		try {
			for (SpotlightItemProvider provider : providers) {
				List<SpotlightItem<?>> items = provider.getItems(filter, new SubProgressMonitor(monitor, 100));
				addFilteredElements(filter, items);
				if (monitor.isCanceled() || reset) {
					monitor.done();
					return;
				}
			}
		} catch (OperationCanceledException e) {
			return;
		} finally {
			monitor.done();
		}
	}

	public void addFilteredElements(SpotlightItemsFilter filter, List<SpotlightItem<?>> items) {
		for (SpotlightItem<?> spotlightItem : items) {
			addElement(spotlightItem, filter);
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
		this.reset = true;
		this.elements.clear();
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
		this.reset = true;
	}

}
