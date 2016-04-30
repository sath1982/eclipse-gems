package de.theves.eclipse.gems.spotlight.internal;

import java.util.Collections;
import java.util.List;

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
