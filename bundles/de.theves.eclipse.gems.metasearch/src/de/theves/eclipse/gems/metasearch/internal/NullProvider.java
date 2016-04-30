package de.theves.eclipse.gems.metasearch.internal;

import java.util.Collections;
import java.util.List;

public class NullProvider implements SearchItemProvider {
	private static final String NOTHING = "";

	@Override
	public List<SearchItem> getItems() {
		return Collections.emptyList();
	}

	@Override
	public String getLabel() {
		return NOTHING;
	}

}
