package jobrunr

import org.postgresql.ds.PGSimpleDataSource
import javax.sql.DataSource

import org.jobrunr.configuration.JobRunr
import org.jobrunr.storage.InMemoryStorageProvider
import org.jobrunr.scheduling.BackgroundJob
import java.time.OffsetDateTime

fun main(args: Array<String>) {
    JobRunr.configure()
        .useStorageProvider(InMemoryStorageProvider())
        .useBackgroundJobServer()
        .useDashboard()
        .initialize()


    BackgroundJob.scheduleRecurrently("my-recurring-job", "*/5 * * * * *") {
        println("Run task at ${OffsetDateTime.now()}")
    }
}