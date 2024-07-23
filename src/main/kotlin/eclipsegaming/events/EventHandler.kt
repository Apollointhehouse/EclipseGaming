package eclipsegaming.events

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class EventHandler(
    /** Event Execution Priority (Highest = First)  */
    val priority: Int = 0
)
