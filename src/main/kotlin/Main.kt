package org.example

import org.postgresql.ds.PGSimpleDataSource
import javax.sql.DataSource
//import org.quartz.*
import org.quartz.impl.StdSchedulerFactory
//import org.quartz.JobBuilder.*
//import org.quartz.TriggerBuilder.*
//import org.quartz.SimpleScheduleBuilder.*
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

//class HelloJob(): Job {
//    override fun execute(p0: JobExecutionContext): Unit {
//        println("HelloJob")
//    }
//}


fun main(args: Array<String>) {
    if (args.size != 3) {
        println("Usage: <pgUrl> <pgUser> <pgPassword>")
        return
    }

    val (pgUrl, pgUser, pgPassword) = args

//    JobRunr.configure()
//        .useStorageProvider(PostgresStorageProvider(dataPGDataSource(pgUrl, pgUser, pgPassword)))
//        .useBackgroundJobServer()
//        .useDashboard()
//        .initialize()
//
//
//    BackgroundJob.scheduleRecurrently("my-recurring-job", "*/5 * * * * *") {
//        println("Run task at ${OffsetDateTime.now()}")
//    }


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
    //scheduler.schedule(hourlyTask.instance("my-id"), Instant.now());


//    // Grab the Scheduler instance from the Factory
//    val scheduler: Scheduler = StdSchedulerFactory.getDefaultScheduler()
//
//    // and start it off
//    scheduler.start()
//
//    // define the job and tie it to our HelloJob class
//    //val helloJob = HelloJob()
//    val job: JobDetail = newJob(HelloJob::class.java)
//        .withIdentity("job1", "group1")
//        .build()
//
//
//    // Trigger the job to run now, and then repeat every 40 seconds
//    val trigger = newTrigger()
//        .withIdentity("trigger1", "group1")
//        .startNow()
//        .withSchedule(
//            simpleSchedule()
//                .withIntervalInSeconds(15)
//                .repeatForever()
//        )
//        .build()
//
//
//    // Tell quartz to schedule the job using our trigger
//    scheduler.scheduleJob(job, trigger)
//
//    //scheduler.shutdown()
}