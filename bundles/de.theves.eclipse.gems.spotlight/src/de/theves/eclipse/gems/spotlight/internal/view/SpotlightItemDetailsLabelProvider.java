package de.theves.eclipse.gems.spotlight.internal.view;

public class SpotlightItemDetailsLabelProvider extends SpotlightItemLabelProvider {
	@Override
	public String getText(Object element) {
		if (element instanceof SpotlightItemProvider) {
			return ((SpotlightItemProvider) element).getLabel();
		}
		if (element instanceof SpotlightItem) {
			SpotlightItem item = (SpotlightItem) element;
			return item.getDetailsLabel();
		}
		return super.getText(element);
	}

}
