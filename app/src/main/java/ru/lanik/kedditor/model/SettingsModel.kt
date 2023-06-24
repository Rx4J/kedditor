package ru.lanik.kedditor.model

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import ru.lanik.kedditor.ui.theme.KedditorCorners
import ru.lanik.kedditor.ui.theme.KedditorSize
import ru.lanik.network.constants.DefaultPostSort
import ru.lanik.network.constants.DefaultPostSource
import ru.lanik.network.constants.DefaultSubredditSource
import java.io.InputStream
import java.io.OutputStream

@Serializable
data class SettingsModel(
    val isDark: Boolean = true,
    val isAuth: Boolean = false,
    val textSize: KedditorSize = KedditorSize.Medium,
    val paddingSize: KedditorSize = KedditorSize.Medium,
    val cornerStyle: KedditorCorners = KedditorCorners.Rounded,
    val defaultPostSource: DefaultPostSource = DefaultPostSource.POPULAR,
    val defaultPostSort: DefaultPostSort = DefaultPostSort.HOT,
    val defaultSubredditSource: DefaultSubredditSource = DefaultSubredditSource.DEFAULT,
)

object SettingsModelSerializer : Serializer<SettingsModel> {
    override val defaultValue: SettingsModel = SettingsModel()
    override suspend fun readFrom(input: InputStream): SettingsModel {
        try {
            return Json.decodeFromString(
                SettingsModel.serializer(),
                input.readBytes().decodeToString(),
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read UserPrefs", serialization)
        }
    }
    override suspend fun writeTo(t: SettingsModel, output: OutputStream) {
        output.write(
            Json.encodeToString(SettingsModel.serializer(), t)
                .encodeToByteArray(),
        )
    }
}
