package maps

class HashMapBackedByTrees<K, V>(keyComp: Comparator<K>) : GenericHashMap<K, V>({ TreeBasedMap(keyComp) })
