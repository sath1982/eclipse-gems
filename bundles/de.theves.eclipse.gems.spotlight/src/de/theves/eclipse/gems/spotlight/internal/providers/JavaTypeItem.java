package de.theves.eclipse.gems.spotlight.internal.providers;

import org.eclipse.jdt.core.search.TypeNameMatch;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;

import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItem;
import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItemProvider;

public class JavaTypeItem extends SpotlightItem<TypeNameMatch> {

	public JavaTypeItem(SpotlightItemProvider provider, TypeNameMatch type) {
		super(provider, type);
	}

	@Override
	public String getLabel() {
		return getItem().getTypeQualifiedName();
	}

	@Override
	public String getElementName() {
		return getItem().getSimpleTypeName();
	}

	@Override
	public String getDetailsLabel() {
		return getItem().getFullyQualifiedName();
	}

	@Override
	public ImageDescriptor doGetImage() {
		IWorkbenchAdapter adapter = getItem().getType().getAdapter(IWorkbenchAdapter.class);
		if (adapter != null) {
			return adapter.getImageDescriptor(getItem().getType());
		}
		return null;
	}

	@Override
	public void show() {
		try {
			JavaUI.openInEditor(getItem().getType());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
