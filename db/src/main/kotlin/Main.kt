package db

import org.postgresql.ds.PGSimpleDataSource
import javax.sql.DataSource

import com.github.kagkarlsson.scheduler.Scheduler;
import com.github.kagkarlsson.scheduler.task.helper.RecurringTask
import com.github.kagkarlsson.scheduler.task.helper.Tasks
import com.github.kagkarlsson.scheduler.task.schedule.FixedDelay
import db.example.ChainDBExample
import jdk.jfr.snippets.Snippets
import java.time.Instant;

fun dataPGDataSource(pgUrl: String, pgUser: String, pgPassword: String): DataSource = PGSimpleDataSource().apply {
    setUrl(pgUrl)
    user = pgUser
    password = pgPassword
}

fun firstExample(dataSource: DataSource) {
    val hourlyTask: RecurringTask<Void> = Tasks.recurring("my-hourly-task", FixedDelay.ofSeconds(30))
        .execute { inst, ctx ->
            println("Executed!")
        }

    val scheduler: Scheduler = Scheduler
        .create(dataSource)
        .startTasks(hourlyTask)
        .threads(5)
        .build()

    // hourlyTask is automatically scheduled on startup if not already started (i.e. exists in the db)
    scheduler.start()
}


fun main(args: Array<String>) {
    if (args.size != 3) {
        println("Usage: <pgUrl> <pgUser> <pgPassword>")
        return
    }

    val (pgUrl, pgUser, pgPassword) = args

    ChainDBExample(dataPGDataSource(pgUrl, pgUser, pgPassword))
}