package de.theves.eclipse.gems.spotlight.internal.providers;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceContentProvider;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.internal.dialogs.WorkbenchPreferenceNode;

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
			try {
				Method imageDescriptorMethod = this.getItem().getClass().getMethod("getImageDescriptor", new Class[0]);
				return (ImageDescriptor) imageDescriptorMethod.invoke(this.getItem(), (Object[]) null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public void show() {
			PreferenceDialog preferenceDialog = PreferencesUtil.createPreferenceDialogOn(
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), getItem().getId(), null, null);
			preferenceDialog.open();
		}

		@SuppressWarnings("unchecked")
		@Override
		public String getElementName() {
			try {
				Method getKeywordLabelsMethod = this.getItem().getClass().getSuperclass().getMethod("getKeywordLabels",
						new Class[0]);
				Collection<String> result = (Collection<String>) getKeywordLabelsMethod.invoke(this.getItem(),
						(Object[]) null);
				return result.stream().collect(Collectors.joining(" "));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return super.getElementName();
		}

	}

	@Override
	public List<SpotlightItem<IPreferenceNode>> getItems(SpotlightItemsFilter filter, IProgressMonitor monitor) {
		PreferenceManager manager = PlatformUI.getWorkbench().getPreferenceManager();
		List<SpotlightItem<IPreferenceNode>> allNodes = new ArrayList<>();
		PreferenceContentProvider pcp = new PreferenceContentProvider();
		Object[] roots = pcp.getElements(manager);
		processChildren(allNodes, pcp, roots, filter);
		return allNodes;
	}

	public void processChildren(List<SpotlightItem<IPreferenceNode>> allNodes, PreferenceContentProvider pcp,
			Object[] roots, SpotlightItemsFilter filter) {
		for (Object root : roots) {
			String pattern = filter.getPattern();
			if (null != pattern) {
				try {
					Method getKeywordLabelsMethod = root.getClass().getSuperclass().getMethod("getKeywordLabels",
							new Class[0]);
					Collection<String> result = (Collection<String>) getKeywordLabelsMethod.invoke(root,
							(Object[]) null);
					boolean match = result.stream().filter(keyword -> keyword.contains(pattern))
							.collect(Collectors.toList()).isEmpty();
					if (match) {
						allNodes.add(new PreferencesItem(this, (IPreferenceNode) root));
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// recurse
			Object[] children = pcp.getChildren(root);
			processChildren(allNodes, pcp, children, filter);
		}
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
