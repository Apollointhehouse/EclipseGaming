package eclipsegaming.events

import java.lang.reflect.InvocationTargetException

abstract class Event {
    var cancelled = false
		private set
    fun cancel() {
        cancelled = true
    }

    var modified = false

    fun call() {
        try {
			EventManager.invoke(this)
        } catch (e: IllegalAccessException) {
            throw RuntimeException("Failed to Invoke Method", e)
        } catch (e: InvocationTargetException) {
            throw RuntimeException("Failed to Invoke Method", e)
        } catch (e: InstantiationException) {
            throw RuntimeException("Failed to Invoke Method", e)
        }
    }
}
