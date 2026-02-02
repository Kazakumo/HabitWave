package io.github.kazakumo.habitwave

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Button

import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import io.github.kazakumo.habitwave.data.repository.HabitRepository
import io.github.kazakumo.habitwave.domain.model.Habit
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate

class HabitWaveGlanceWidgetReceiver : GlanceAppWidgetReceiver() {
    // ReceiverにどのGlaceAppWidgetを使うか教える
    override val glanceAppWidget: GlanceAppWidget = HabitWaveGlanceWidget()
}

class HabitWaveGlanceWidget : GlanceAppWidget() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface GlaceContentEntryPoint {
        fun HabitRepository(): HabitRepository
    }

    override

    suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        val hiltEntryPoint =
            EntryPointAccessors.fromApplication<GlaceContentEntryPoint>(context.applicationContext)
        val repository = hiltEntryPoint.HabitRepository()
        val habits = repository.getHabits().firstOrNull()
            ?: emptyList() // NOTE: Flowを監視して、最初に何か送られてきた段階で接続を切る
        val displayHabit = habits.firstOrNull() // 最初の1件


        provideContent {
            GlaceContent(displayHabit)
        }
    }

    @Composable
    private fun GlaceContent(latestHabit: Habit?) {
        GlanceTheme {

            val context = LocalContext.current

            Column(
                modifier = GlanceModifier
                    .fillMaxSize(),
//                    .background(ImageProvider(R.drawable.widget_background)), // 波のデザインを背景に！
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (latestHabit != null) {
                    Text(
                        text = latestHabit.title,
                        style = TextStyle(
                            color = ColorProvider(Color.White),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    // ウィジェット専用のボタン（ActionCallbackを使用）
                    Spacer(GlanceModifier.height(8.dp))
                    Button(
                        text = "今日",
                        onClick = actionRunCallback<ToggleHabitAction>(
                            actionParametersOf(ToggleHabitAction.HabitIdKey to latestHabit.id)
                        ),

                        )
                } else {
                    Text("習慣がありません")
                }
            }
        }
    }
}


class ToggleHabitAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val habitId = parameters[HabitIdKey] ?: return

        // リポジトリを取得
        val repository = EntryPointAccessors
            .fromApplication<HabitWaveGlanceWidget.GlaceContentEntryPoint>(context.applicationContext)
            .HabitRepository()

        repository.toggleCheckIn(
            habitId = habitId,
            date = LocalDate.now()
        )
        // widget状態更新
        HabitWaveGlanceWidget().update(context, glanceId)
    }

    companion object {
        val HabitIdKey = ActionParameters.Key<Long>("habit_id")
    }
}

