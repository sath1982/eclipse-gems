package de.theves.eclipse.gems.spotlight.internal.providers;

import org.eclipse.jdt.core.search.TypeNameMatch;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;

import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItem;
import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItemProvider;

public class JavaTypeItem extends SpotlightItem {
	private TypeNameMatch type;

	public JavaTypeItem(SpotlightItemProvider provider, TypeNameMatch type) {
		super(provider);
		this.type = type;
	}

	@Override
	public String getLabel() {
		return this.type.getTypeQualifiedName();
	}

	@Override
	public String getElementName() {
		return this.type.getSimpleTypeName();
	}
	
	@Override
	public String getDetailsLabel() {
		return this.type.getFullyQualifiedName();
	}

	@Override
	public ImageDescriptor doGetImage() {
		IWorkbenchAdapter adapter = this.type.getType().getAdapter(IWorkbenchAdapter.class);
		if (adapter != null) {
			return adapter.getImageDescriptor(this.type.getType());
		}
		return null;
	}

	@Override
	public void show() {
		try {
			JavaUI.openInEditor(this.type.getType());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
