package br.com.inseguros.data.model

enum class QuoteTypeEnum(val value: String) {
    UNDER_ANALYSIS("under_analysis"),
    CANCELED("canceled"),
    APPROVED("approved"),
    FINISHED("finished")
}