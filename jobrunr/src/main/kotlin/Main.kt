package jobrunr

import org.postgresql.ds.PGSimpleDataSource
import javax.sql.DataSource

import org.jobrunr.configuration.JobRunr
import org.jobrunr.storage.InMemoryStorageProvider
import org.jobrunr.scheduling.BackgroundJob
import org.jobrunr.scheduling.JobBuilder
import org.jobrunr.storage.sql.postgres.PostgresStorageProvider
import java.time.OffsetDateTime

fun dataPGDataSource(pgUrl: String, pgUser: String, pgPassword: String): DataSource = PGSimpleDataSource().apply {
    setUrl(pgUrl)
    user = pgUser
    password = pgPassword
}

fun recurringJob(args: Array<String>) {
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

    BackgroundJob.scheduleRecurrently("my-recurring-job1", "*/5 * * * * *"){
        println("Run task at ${OffsetDateTime.now()}")
    }
}

fun basic() {
    JobRunr.configure()
        .useStorageProvider(InMemoryStorageProvider())
        .useBackgroundJobServer()
        .useDashboard()
        .initialize()

    BackgroundJob.enqueue {
        println("Simple!")
        //println("Simple!") only one method!!
    }
    // dashboard at http://localhost:8000/dashboard/overview


    // BackgroundJob.enqueue( lambda )
    // BackgroundJobRequest.enqueue( JobRequest )
    // jobScheduler methods = static methods of BackgroundJob
    // jobRequestScheduler methods = static methods of BackgroundJobRequest
}

fun builder() {
    val jobScheduler = JobRunr.configure()
        .useStorageProvider(InMemoryStorageProvider())
        .useBackgroundJobServer()
        .useDashboard()
        .initialize().jobScheduler

    // uses JobBuilder
    jobScheduler.create(
        JobBuilder.aJob()
        .withName("Very funny name")
        .withAmountOfRetries(3)
        .withLabels("tenant-A", "from-rest-api")
        .withDetails { println("Very funny job!") })
}

fun stream() {
    JobRunr.configure()
        .useStorageProvider(InMemoryStorageProvider())
        .useBackgroundJobServer()
        .useDashboard()
        .initialize()

    val numbers = listOf(1, 2, 3, 4, 5)
    BackgroundJob.enqueue(numbers.stream()) { x -> println("Number $x!") }
//  possible output:
//    Number 4!
//    Number 1!
//    Number 5!
//    Number 3!
//    Number 2!
}

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