package db.example

import com.github.kagkarlsson.scheduler.Scheduler
import com.github.kagkarlsson.scheduler.task.*
import com.github.kagkarlsson.scheduler.task.CompletionHandler.OnCompleteRemove
import com.github.kagkarlsson.scheduler.task.helper.Tasks
import java.io.Serializable
import java.time.Duration
import java.time.Instant
import javax.sql.DataSource

val STEP1_TASK: TaskDescriptor<Void> = TaskDescriptor.of("job-step-1")
val STEP2_TASK: TaskDescriptor<Void> = TaskDescriptor.of("job-step-2")

internal class OnCompleteRemoveAndCreateNextStep(private val newTaskName: String) :
    CompletionHandler<Void> {
    override fun complete(
        executionComplete: ExecutionComplete, executionOperations: ExecutionOperations<Void>
    ) {
        val taskInstance = executionComplete.execution.taskInstance
        val nextInstance = TaskInstance<Void>(newTaskName, taskInstance.id)
        executionOperations.removeAndScheduleNew(SchedulableInstance.of(nextInstance, Instant.now()))
    }
}

fun ChainDBExample(dataSource: DataSource) {
    val jobStep1 =
        Tasks.custom(STEP1_TASK)
            .execute {_, _ ->
                println("Step one")
                OnCompleteRemoveAndCreateNextStep(STEP2_TASK.taskName)
            }

    val jobStep2 =
        Tasks.custom(STEP2_TASK)
            .execute { _, _ ->
                println("Step two")
                OnCompleteRemove()
            }

    val scheduler =
        Scheduler.create(dataSource, jobStep1, jobStep2)
            .build()

    scheduler.start()

    scheduler.schedule(
        STEP1_TASK.instance("unique-id").scheduledTo(Instant.now())
    )
}

//internal class OnCompleteRemoveAndCreateNextStep(private val newTaskName: String) :
//    CompletionHandler<Void> {
//    override fun complete(
//        executionComplete: ExecutionComplete, executionOperations: ExecutionOperations<Void>
//    ) {
//        val taskInstance = executionComplete.execution.taskInstance
//        val nextInstance = TaskInstance<Void>(newTaskName, taskInstance.id)
//        executionOperations.removeAndScheduleNew(SchedulableInstance.of(nextInstance, Instant.now()))
//    }
//}
//
//fun ChainDBExample(dataSource: DataSource) {
//    val jobStep1 =
//        Tasks.custom<Void>("STEP1", null)
//            .execute {_, _ ->
//                println("Step one")
//                OnCompleteRemoveAndCreateNextStep("STEP2")
//            }
//
//    val jobStep2 =
//        Tasks.custom<Void>("STEP2", null)
//            .execute { _, _ ->
//                println("Step two")
//                OnCompleteRemove()
//            }
//
//    val scheduler =
//        Scheduler.create(dataSource, jobStep1, jobStep2)
//            .build()
//
//    scheduler.start()
//
//    scheduler.schedule(jobStep1.instance("unique-id"), Instant.now())
//}