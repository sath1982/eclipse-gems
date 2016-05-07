package de.theves.eclipse.gems.spotlight.internal.view;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class SpotlightItemViewerFilter extends ViewerFilter {
	private SpotlightItemsFilter filter;

	public SpotlightItemViewerFilter(SpotlightItemsFilter filter) {
		this.filter = filter;
	}

	public void setFilter(SpotlightItemsFilter filter) {
		this.filter = filter;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (element instanceof SpotlightItem) {
			return filter.matches(((SpotlightItem<?>) element).getElementName());
		}
		if (element instanceof SpotlightItemProvider) {
			// no filtering for providers
			return true;
		}
		return false;
	}

}
