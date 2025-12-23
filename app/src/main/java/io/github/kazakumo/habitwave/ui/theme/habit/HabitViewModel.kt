package io.github.kazakumo.habitwave.ui.theme.habit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kazakumo.habitwave.data.repository.HabitRepository
import io.github.kazakumo.habitwave.domain.model.Habit
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {
    /**
     * 1. UI状態の公開
     * Repositoryの getHabits() (Flow) を、UIが購読できる StateFlow に変換します。
     * - stateIn: FlowをStateFlowに変換する拡張関数。
     * - SharingStarted.WhileSubscribed(5000): 画面が非表示になってから5秒間はデータを保持し、それ以降はリソースを解放します。
     */
    val uiState: StateFlow<List<Habit>> = repository.getHabits().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    /**
     * 2. チェックインの切り替え（今日・昨日）
     * 画面側のポチポチという操作をリポジトリに伝えます。
     * YC（イエスタデイ・チェックイン）機能のため、LocalDateを引数で受け取れるようにしています。
     */
    fun toggleCheckIn(habitId: Long, Date: LocalDate) {
        viewModelScope.launch {
            repository.toggleCheckIn(habitId, Date)
        }
    }

    /**
     * 3. 習慣の新規追加
     * 入力フォームからの入力を受け取ってリポジトリに渡します。
     */
    fun addHabit(title: String, colorHex: String = "#6200EE") {
        viewModelScope.launch {
            repository.addHabit(title, colorHex)
        }
    }

    /**
     * 4. 習慣の削除
     * 必要に応じて実装します。
     */
    fun deleteHabit(habitId: Long) {
        viewModelScope.launch {
            repository.deleteHabit(habitId)
        }
    }
}