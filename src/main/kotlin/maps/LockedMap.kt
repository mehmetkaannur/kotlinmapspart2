package maps

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

abstract class LockedMap<K, V>(private val targetMap: CustomMutableMap<K, V>)
    : CustomMutableMap<K, V> {
    val lock: Lock = ReentrantLock()
    override val entries: Iterable<Entry<K, V>>
        get() = lock.withLock { return targetMap.entries }
    override fun get(key: K): V? {
        return lock.withLock { targetMap.get(key) }
    }

    override fun set(key: K, value: V): V? {
        return lock.withLock { targetMap.set(key, value) }    }

    override fun put(key: K, value: V): V? {
        return lock.withLock { targetMap.put(key, value) }    }

    override fun put(entry: Entry<K, V>): V? {
        return lock.withLock { targetMap.put(entry) }    }

    override fun remove(key: K): V? {
        return lock.withLock { targetMap.remove(key) }    }

    override fun contains(key: K): Boolean {
        return lock.withLock { targetMap.contains(key) }    }
}