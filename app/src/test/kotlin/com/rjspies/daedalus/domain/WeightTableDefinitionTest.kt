package com.rjspies.daedalus.domain

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class WeightTableDefinitionTest {
    @Test
    fun `WEIGHT_TABLE_IDENTIFIER should be Weight`() {
        WEIGHT_TABLE_IDENTIFIER shouldBe "Weight"
    }

    @Test
    fun `WEIGHT_COLUMN_IDENTIFIER_ID should be id`() {
        WEIGHT_COLUMN_IDENTIFIER_ID shouldBe "id"
    }

    @Test
    fun `WEIGHT_COLUMN_IDENTIFIER_VALUE should be value`() {
        WEIGHT_COLUMN_IDENTIFIER_VALUE shouldBe "value"
    }

    @Test
    fun `WEIGHT_COLUMN_IDENTIFIER_NOTE should be note`() {
        WEIGHT_COLUMN_IDENTIFIER_NOTE shouldBe "note"
    }

    @Test
    fun `WEIGHT_COLUMN_IDENTIFIER_DATE_TIME should be dateTime`() {
        WEIGHT_COLUMN_IDENTIFIER_DATE_TIME shouldBe "dateTime"
    }
}
