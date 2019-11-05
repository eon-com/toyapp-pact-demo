package com.toyapp.pact.demo.core.data.converters

import org.joda.time.DateTime
import java.time.LocalDate

object DateConverters {

    fun LocalDate.toJodaDateTime(): DateTime {
        return org.joda.time.LocalDate(
                this.year,
                this.monthValue,
                this.dayOfMonth
        ).toDateTimeAtStartOfDay()
    }

    fun DateTime.toJavaLocalDate(): LocalDate {
        return LocalDate.of(
                this.year,
                this.monthOfYear,
                this.dayOfMonth
        )
    }

}