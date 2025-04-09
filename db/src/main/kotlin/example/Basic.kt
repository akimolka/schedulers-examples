package db.example
import com.github.kagkarlsson.scheduler.Scheduler
import com.github.kagkarlsson.scheduler.task.helper.RecurringTask
import com.github.kagkarlsson.scheduler.task.helper.Tasks
import com.github.kagkarlsson.scheduler.task.schedule.FixedDelay
import db.dataPGDataSource
import javax.sql.DataSource

fun basicDBExample(dataSource: DataSource) {
    val hourlyTask: RecurringTask<Void> = Tasks.recurring("helloJob", FixedDelay.ofSeconds(30))
        .execute { inst, ctx ->
            println("Hello World!")
        }

    val scheduler: Scheduler = Scheduler
        .create(dataSource)
        .startTasks(hourlyTask)
        .threads(5)
        .build()

    scheduler.start()
}