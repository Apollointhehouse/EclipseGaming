package eclipsegaming.events

import java.lang.reflect.Method

object EventManager {
    private val methodCache = mutableMapOf<Class<out Any>, MutableList<Pair<Any, Method>>>()
    private val objectEventMap = mutableMapOf<Any, MutableList<Class<out Any>>>()
    @JvmStatic
    fun subscribe(obj: Any) {
        val klass = obj::class.java

        // Get all valid EventHandler methods
        val methods = klass.declaredMethods
            .asSequence()
            .filter(EventManager::isValid)
            .prioritize()
            .toList()

        methods.forEach { method ->
            // Cache the methods for each event
            val eventClass = method.parameterTypes[0]
            if (!methodCache.containsKey(eventClass)) {
                methodCache[eventClass] = mutableListOf()
            }
            methodCache[eventClass]?.add(obj to method)

            // Cache the events for each object
            if (!objectEventMap.containsKey(obj)) {
                objectEventMap[obj] = mutableListOf()
            }
            objectEventMap[obj]?.add(eventClass)
        }
    }
    @JvmStatic
    fun unsubscribe(obj: Any) {
        objectEventMap[obj]?.forEach { eventClass ->
            methodCache[eventClass]?.removeIf { it.first == obj }
        }
        objectEventMap.remove(obj)
    }

    /**
     * Method Invoker using Reflections
     * @param event Event that Called Invoke with [Event.call]
     */
    @JvmStatic
    fun invoke(event: Event) {
        val methods = methodCache[event::class.java]?.toList() ?: return

        for ((obj, method) in methods) {
            method.isAccessible = true
            method.invoke(obj, event)
        }
    }

    /**
     * Used to Check if Method is Valid for Event Handling
     * @param method Method to Check for Validity
     * @return True if Method is Valid
     */

    private fun isValid(method: Method): Boolean {
        if (!method.isAnnotationPresent(EventHandler::class.java)) return false
        if (method.returnType != Void.TYPE) return false
        if (method.parameterCount != 1) return false

        return method.parameterTypes[0]::class.java.isInstance(Event::class.java)
    }

    /**
     * Used for Setting Up Event Invocation Priority
     * @return Sorted List by Annotation Priority
     */
    private fun Sequence<Method>.prioritize(): Sequence<Method> {
        return sortedWith { m1: Method, m2: Method ->
            val p1: Int = m1.getAnnotation(EventHandler::class.java).priority
            val p2: Int = m2.getAnnotation(EventHandler::class.java).priority
            p2 - p1
        }
    }
}
