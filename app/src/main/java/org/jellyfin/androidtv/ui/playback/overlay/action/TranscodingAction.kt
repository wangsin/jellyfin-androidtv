package org.jellyfin.androidtv.ui.playback.overlay.action

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import androidx.core.view.get
import org.jellyfin.androidtv.R
import org.jellyfin.androidtv.preference.UserPreferences
import org.jellyfin.androidtv.preference.UserPreferences.Companion.needForceTranscodingAction
import org.jellyfin.androidtv.ui.playback.PlaybackController
import org.jellyfin.androidtv.ui.playback.overlay.CustomPlaybackTransportControlGlue
import org.jellyfin.androidtv.ui.playback.overlay.VideoPlayerAdapter
import org.koin.java.KoinJavaComponent.get
import timber.log.Timber

class TranscodingAction(
	context: Context,
	customPlaybackTransportControlGlue: CustomPlaybackTransportControlGlue
) : CustomAction(context, customPlaybackTransportControlGlue) {

	private var popup: PopupMenu? = null
	private var needTranscodeReason: Int = 0
	private val userPreferences = get<UserPreferences?>(UserPreferences::class.java)

	init {
		initializeWithIcon(R.drawable.ic_loop)
	}

	override fun handleClickAction(
		playbackController: PlaybackController,
		videoPlayerAdapter: VideoPlayerAdapter,
		context: Context,
		view: View,
	) {
		videoPlayerAdapter.leanbackOverlayFragment.setFading(false)
		dismissPopup()
		popup = PopupMenu(context, view, Gravity.END).apply {

			menu.add(0, 0, 100, "不需要转码")
			menu.add(0, 1, 101, "需要转码")

			menu.setGroupCheckable(0, true, true)

			menu[needTranscodeReason].isChecked = true

			setOnDismissListener {
				videoPlayerAdapter.leanbackOverlayFragment.setFading(true)
				popup = null
			}

			setOnMenuItemClickListener { menuItem ->
				Timber.d("${menuItem.itemId} $menuItem Selected!!!!")
				needTranscodeReason = menuItem.itemId
				menu[needTranscodeReason].isChecked = true
				userPreferences?.set(needForceTranscodingAction, menu[needTranscodeReason].itemId==1)
				playbackController.refreshStream()
				dismiss()
				true
			}
		}
		popup?.show()
	}

	fun dismissPopup() {
		Timber.d("detached,needForceTranscodingAction_set_to_false")
		userPreferences?.set(needForceTranscodingAction, false)
		popup?.dismiss()
	}
}
