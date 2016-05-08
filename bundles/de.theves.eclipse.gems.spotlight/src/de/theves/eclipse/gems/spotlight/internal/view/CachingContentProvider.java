package de.theves.eclipse.gems.spotlight.internal.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.ILazyContentProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

public class CachingContentProvider implements IStructuredContentProvider, ILazyContentProvider {
	private List<SpotlightItem<?>> elements;
	private TableViewer viewer;

	public CachingContentProvider(TableViewer viewer) {
		this.viewer = viewer;
		this.elements = new ArrayList<>();
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

	public void addElement(SpotlightItem<?> item, SpotlightItemsFilter filter) {
		if (filter.matches(item.getElementName())) {
			elements.add(item);
		}
	}

	public void sort() {
		Collections.sort(elements);
	}

	public void reset() {
		elements.clear();
	}

	public int getNumberOfElements() {
		return elements.size();
	}

	@Override
	public void updateElement(int index) {
		viewer.replace((elements.size() > index) ? elements.get(index) : null, index);
	}

}
