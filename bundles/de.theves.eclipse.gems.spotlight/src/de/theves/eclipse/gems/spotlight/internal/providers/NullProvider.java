package de.theves.eclipse.gems.spotlight.internal.providers;

import java.util.Collections;
import java.util.List;

import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItem;
import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItemProvider;

public class NullProvider implements SpotlightItemProvider {
	private static final String NOTHING = "";

	@Override
	public List<SpotlightItem> getItems() {
		return Collections.emptyList();
	}

	@Override
	public String getLabel() {
		return NOTHING;
	}

}
