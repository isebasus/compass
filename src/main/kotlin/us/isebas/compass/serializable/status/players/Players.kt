package us.isebas.compass.serializable.status.players

import us.isebas.compass.serializable.status.players.sample.Sample

data class Players(
    val max: Int? = null,
    val online: Int? = null,
    val sample: Array<Sample>? = null,
)