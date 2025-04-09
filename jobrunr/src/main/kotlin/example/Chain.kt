package jobrunr.example

import jobrunr.dataPGDataSource
import org.jobrunr.configuration.JobRunr
import org.jobrunr.scheduling.BackgroundJob
import org.jobrunr.storage.InMemoryStorageProvider
import org.jobrunr.storage.sql.postgres.PostgresStorageProvider
import java.time.OffsetDateTime

fun chainJobrunrExample() {
    JobRunr.configure()
        .useStorageProvider(InMemoryStorageProvider())
        .useBackgroundJobServer()
        .useDashboard()
        .initialize()

    // JobRunrPro
    BackgroundJob.enqueue{ println("Step one") }
   // .continueWith{ println("Step two") }
}