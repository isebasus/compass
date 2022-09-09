package us.isebas.compass.serializable.status

import us.isebas.compass.serializable.status.description.Description
import us.isebas.compass.serializable.status.players.Players
import us.isebas.compass.serializable.status.version.Version

data class StatusResponse (
    val version: Version?,
    val players: Players?,
    val description: Description?,
    val favicon: String? = "",
    val previewsChat: Boolean? = null,
    val enforcesSecureChat: Boolean? = null
)