package ru.lanik.kedditor.model

data class SnackbarModel(
    val messageBody: String? = null,
    val actionLabel: String? = null,
    val isShow: Boolean = false,
    val isUpdate: Boolean = false,
)