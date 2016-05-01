package de.theves.eclipse.gems.spotlight.internal.providers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.TypeNameMatch;
import org.eclipse.jdt.core.search.TypeNameMatchRequestor;

import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItem;
import de.theves.eclipse.gems.spotlight.internal.view.SpotlightItemProvider;

public class JavaTypesProvider implements SpotlightItemProvider {

	@Override
	public List<? extends SpotlightItem> getItems() {
		List<JavaTypeItem> items = new ArrayList<>();
		SearchEngine engine = new SearchEngine((WorkingCopyOwner) null);
		IJavaSearchScope searchScope = SearchEngine.createWorkspaceScope();
		try {
			engine.searchAllTypeNames(null, SearchPattern.R_EXACT_MATCH, null, SearchPattern.R_PREFIX_MATCH,
					IJavaSearchConstants.TYPE, searchScope, new TypeNameMatchRequestor() {
						@Override
						public void acceptTypeNameMatch(TypeNameMatch match) {
							items.add(new JavaTypeItem(JavaTypesProvider.this, match));
						}
					}, IJavaSearchConstants.WAIT_UNTIL_READY_TO_SEARCH, new NullProgressMonitor());
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return items;
	}

	@Override
	public String getLabel() {
		return "Java Types";
	}

}
