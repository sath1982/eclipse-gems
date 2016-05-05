package de.theves.eclipse.gems.spotlight.internal.view;

public class SpotlightDialogInput {
	private SpotlightItemProvider[] providers;

	public SpotlightDialogInput(SpotlightItemProvider[] providers) {
		this.providers = providers;
	}

	public SpotlightItemProvider[] getProviders() {
		return providers;
	}
}
