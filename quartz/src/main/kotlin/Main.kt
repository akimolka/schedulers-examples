package quartz

import org.quartz.Job
import org.quartz.JobBuilder.newJob
import org.quartz.JobDetail
import org.quartz.JobExecutionContext
import org.quartz.Scheduler
import org.quartz.SimpleScheduleBuilder.simpleSchedule
import org.quartz.TriggerBuilder.newTrigger
import org.quartz.impl.StdSchedulerFactory

import quartz.example.basicQuartzExample
import quartz.example.chainQuartzExample

class HelloJob(): Job {
    override fun execute(p0: JobExecutionContext): Unit {
        println("HelloJob")
    }
}

fun hello() {
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

class DumbJob : Job {
    override fun execute(context: JobExecutionContext) {
        val key = context.jobDetail.key

        val dataMap = context.jobDetail.jobDataMap

        val jobSays = dataMap.getString("jobSays")
        val myFloatValue = dataMap.getFloat("myFloatValue")

        println("Instance $key of DumbJob says: $jobSays, and val is: $myFloatValue")
    }
}

fun main(args: Array<String>) {
    chainQuartzExample()
}