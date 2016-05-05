package de.theves.eclipse.gems.spotlight.internal.view;

import org.eclipse.ui.dialogs.SearchPattern;

public class SpotlightItemsFilter {
	protected SearchPattern patternMatcher;

	public SpotlightItemsFilter(String pattern) {
		this(new SearchPattern(), pattern);
	}

	public SpotlightItemsFilter(SearchPattern searchPattern, String pattern) {
		patternMatcher = searchPattern;
		String stringPattern = ""; //$NON-NLS-1$
		if (pattern != null && !pattern.equals("*")) { //$NON-NLS-1$
			stringPattern = pattern;
		}
		patternMatcher.setPattern(stringPattern);
	}

	public boolean matchesRawNamePattern(SpotlightItem item) {
		String prefix = patternMatcher.getPattern();
		String text = item.getElementName();

		if (text == null)
			return false;

		int textLength = text.length();
		int prefixLength = prefix.length();
		if (textLength < prefixLength) {
			return false;
		}
		for (int i = prefixLength - 1; i >= 0; i--) {
			if (Character.toLowerCase(prefix.charAt(i)) != Character.toLowerCase(text.charAt(i)))
				return false;
		}
		return true;
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
}
