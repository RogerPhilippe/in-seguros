package br.com.inseguros.data.enums

enum class QuoteTypeEnum(val value: String) {
    UNDER_ANALYSIS("under_analysis"),
    PROPOSAL_SENT("proposal_sent"),
    CANCELED("canceled"),
    APPROVED("approved"),
    FINISHED("finished")
}