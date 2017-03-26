package de.theves.eclipse.gems.spotlight.internal.providers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.resource.ImageDescriptor;

import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItem;
import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItemProvider;
import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItemsFilter;

public class PreferencesPageProvider implements SpotlightItemProvider<IPreferenceNode> {

	public static class PreferencesItem extends SpotlightItem<IPreferenceNode> {

		public PreferencesItem(PreferencesPageProvider provider, IPreferenceNode item) {
			super(provider, item);
		}

		@Override
		public String getLabel() {
			return this.getItem().getLabelText();
		}

		@Override
		public ImageDescriptor doGetImage() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void show() {
			// TODO Auto-generated method stub
		}

	}

	@Override
	public List<SpotlightItem<IPreferenceNode>> getItems(SpotlightItemsFilter filter, IProgressMonitor monitor) {
		PreferenceManager manager = new PreferenceManager();
		List<SpotlightItem<IPreferenceNode>> allNodes = new ArrayList<>();
		IPreferenceNode[] rootSubNodes = manager.getRootSubNodes();
		for (IPreferenceNode iPreferenceNode : rootSubNodes) {
			allNodes.add(new PreferencesItem(this, iPreferenceNode));
		}
		return allNodes;
	}

	@Override
	public String getLabel() {
		return "Preferences";
	}

	@Override
	public int getCategory() {
		return CATEGORY_PREFERENCES;
	}

}
