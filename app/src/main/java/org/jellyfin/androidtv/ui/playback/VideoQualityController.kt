package org.jellyfin.androidtv.ui.playback

import org.jellyfin.androidtv.preference.UserPreferences;
import timber.log.Timber

class VideoQualityController(
	previousQualitySelection: String,
	private val userPreferences: UserPreferences,
) {
	var currentQuality = previousQualitySelection
		set(value) {
			Timber.d("userPreferences Changed: $value")
			userPreferences[UserPreferences.maxBitrate] = value
			field = value
		}
}
