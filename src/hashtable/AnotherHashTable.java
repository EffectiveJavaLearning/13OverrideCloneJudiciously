package hashtable;

/**
 * 克隆复杂可变对象的最后一种方法是使用super.clone()之后，将获取到的对象中所有字段初始化，
 * 然后调用更高级别的方法重新为该实例的字段赋值。比如这里，buckets = new Entry[];
 * 然后调用put(key，value)方法(没加具体逻辑)为其中的每一个Entry赋值
 *
 * @author LightDance
 */
public class AnotherHashTable {
    private Entry[] buckets;

    AnotherHashTable(Entry[] buckets) {
        this.buckets = buckets;
    }

    @Override
    protected Object clone(){
        AnotherHashTable hashTable = null;
        try {
            hashTable = (AnotherHashTable) super.clone();
            hashTable.buckets = new Entry[buckets.length];
            for (int i = 0; i < buckets.length; i++) {
                hashTable.buckets[i].put(this.buckets[i].deepCopy1());
                //大意为将buckets中
            }
            return hashTable;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static class Entry {
        final Object key;
        Object value;
        Entry next;

        public Entry(Object key, Object value, Entry next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
        Entry deepCopy1() {
            return new Entry(key, value, next == null ? null : next.deepCopy1());
        }

        public void put(Entry entry){
            //...
        }

        @Override
        public String toString() {
            return "key: " + key + "; value :" + value;
        }

        //...
    }
}
