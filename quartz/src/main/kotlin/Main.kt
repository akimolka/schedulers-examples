package quartz

import org.postgresql.ds.PGSimpleDataSource
import javax.sql.DataSource

import org.quartz.*
import org.quartz.impl.StdSchedulerFactory
import org.quartz.JobBuilder.*
import org.quartz.TriggerBuilder.*
import org.quartz.SimpleScheduleBuilder.*

class HelloJob(): Job {
    override fun execute(p0: JobExecutionContext): Unit {
        println("HelloJob")
    }
}

fun main(args: Array<String>) {
    // Grab the Scheduler instance from the Factory
    val scheduler: Scheduler = StdSchedulerFactory.getDefaultScheduler()

    // and start it off
    scheduler.start()

    // define the job and tie it to our HelloJob class
    //val helloJob = HelloJob()
    val job: JobDetail = newJob(HelloJob::class.java)
        .withIdentity("job1", "group1")
        .build()


    // Trigger the job to run now, and then repeat every 40 seconds
    val trigger = newTrigger()
        .withIdentity("trigger1", "group1")
        .startNow()
        .withSchedule(
            simpleSchedule()
                .withIntervalInSeconds(15)
                .repeatForever()
        )
        .build()


    // Tell quartz to schedule the job using our trigger
    scheduler.scheduleJob(job, trigger)

    //scheduler.shutdown()
}