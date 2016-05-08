package de.theves.eclipse.gems.spotlight.internal.view;

import org.eclipse.ui.dialogs.SearchPattern;

public class SpotlightItemsFilter {
	protected SearchPattern patternMatcher;

	public SpotlightItemsFilter(String pattern) {
		patternMatcher = new SearchPattern();
		setPattern(pattern);
	}

	public void setPattern(String pattern) {
		String stringPattern = ""; //$NON-NLS-1$
		if (pattern != null && !pattern.equals("*") && !pattern.isEmpty()) { //$NON-NLS-1$
			stringPattern = pattern;
		}
		patternMatcher.setPattern(stringPattern);
	}

	protected boolean matches(String text) {
		return patternMatcher.matches(text);
	}

	public String getPattern() {
		return patternMatcher.getPattern();
	}

	public boolean isCamelCasePattern() {
		return patternMatcher.getMatchRule() == SearchPattern.RULE_CAMELCASE_MATCH;
	}

	public boolean isSubFilter(SpotlightItemsFilter other) {
		String otherPattern = other.getPattern();
		String pattern = getPattern();
		return pattern.startsWith(otherPattern);
	}

	@Override
	public String toString() {
		return "SpotlightItemsFilter [pattern=" + patternMatcher.getPattern() + "]";
	}

}
