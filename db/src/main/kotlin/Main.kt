package db

import org.postgresql.ds.PGSimpleDataSource
import javax.sql.DataSource

import com.github.kagkarlsson.scheduler.Scheduler;
import com.github.kagkarlsson.scheduler.task.helper.RecurringTask
import com.github.kagkarlsson.scheduler.task.helper.Tasks
import com.github.kagkarlsson.scheduler.task.schedule.FixedDelay
import java.time.Instant;

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


    val hourlyTask: RecurringTask<Void> = Tasks.recurring("my-hourly-task", FixedDelay.ofSeconds(30))
        .execute { inst, ctx ->
            println("Executed!")
        }

    val scheduler: Scheduler = Scheduler
        .create(dataPGDataSource(pgUrl, pgUser, pgPassword))
        .startTasks(hourlyTask)
        .threads(5)
        .build()

    // hourlyTask is automatically scheduled on startup if not already started (i.e. exists in the db)
    scheduler.start()
}