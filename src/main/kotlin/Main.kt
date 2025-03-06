package org.example

import org.jobrunr.configuration.JobRunr
import org.jobrunr.scheduling.BackgroundJob
import org.jobrunr.scheduling.cron.Cron
import org.jobrunr.storage.sql.postgres.PostgresStorageProvider
import org.postgresql.ds.PGSimpleDataSource
import java.time.OffsetDateTime
import javax.sql.DataSource


fun dataPGDataSource(pgUrl: String, pgUser: String, pgPassword: String): DataSource = PGSimpleDataSource().apply {
    setUrl(pgUrl)
    user = pgUser
    password = pgPassword
}


fun main(args: Array<String>) {
    if (args.size != 3) {
        println("Usage: <pgUrl> <pgUser> <pgPassword>")
        return
    }

    val (pgUrl, pgUser, pgPassword) = args

    JobRunr.configure()
        .useStorageProvider(PostgresStorageProvider(dataPGDataSource(pgUrl, pgUser, pgPassword)))
        .useBackgroundJobServer()
        .useDashboard()
        .initialize()


    BackgroundJob.scheduleRecurrently("my-recurring-job", "*/5 * * * * *") {
        println("Run task at ${OffsetDateTime.now()}")
    }
}