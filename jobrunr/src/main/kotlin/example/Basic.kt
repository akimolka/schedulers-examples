package jobrunr.example

import jobrunr.dataPGDataSource
import org.jobrunr.configuration.JobRunr
import org.jobrunr.scheduling.BackgroundJob
import org.jobrunr.storage.InMemoryStorageProvider
import org.jobrunr.storage.sql.postgres.PostgresStorageProvider
import java.time.OffsetDateTime

fun basicJobrunrExample() {
    JobRunr.configure()
        .useStorageProvider(InMemoryStorageProvider())
        .useBackgroundJobServer()
        .useDashboard()
        .initialize()

    BackgroundJob.scheduleRecurrently("my-recurring-job", "*/30 * * * * *") {
        println("Hello World!")
    }
}