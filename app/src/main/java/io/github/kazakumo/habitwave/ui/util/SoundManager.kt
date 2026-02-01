package io.github.kazakumo.habitwave.ui.util

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.kazakumo.habitwave.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val soundPool: SoundPool = SoundPool.Builder().setMaxStreams(1).setAudioAttributes(
        AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(
                AudioAttributes.CONTENT_TYPE_SONIFICATION
            ).build()
    ).build()

    private var soundId: Int = 0

    init {
        soundId = soundPool.load(context, R.raw.complete_sound, 1)
    }

    fun playCompleteSound() {
        if (soundId != 0) {
            soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
        }
    }

    fun release() {
        soundPool.release()
    }
}