package com.example.wayspot.ui.components.map

enum class MapRenderStatus {
    Loading,
    Ready,
    Failed
}

data class MapRenderState(
    val status: MapRenderStatus = MapRenderStatus.Loading,
    val attempt: Int = 0
) {
    fun retry(): MapRenderState =
        if (status == MapRenderStatus.Failed) {
            copy(status = MapRenderStatus.Loading, attempt = attempt + 1)
        } else {
            this
        }
}
