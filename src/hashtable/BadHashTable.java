package hashtable;

/**
 * 用于演示为什么有时候仅仅递归地调用父类的clone()方法还不够
 *
 * 该类包含一个数组{@link #buckets}，数组中每个元素都是{@link Entry}类型键值对(K-V)链表。
 * (为了提高性能，该类实现了自己的轻量级单链接列表，而没有使用{@link java.util.LinkedList})
 *
 * 但是，这种复制方式仅仅将数组中对于Entry的引用复制了过来，这就导致了克隆前后对象共用相同的bucket[]，
 * 造成不稳定。为了消除这种不稳定性，可以考虑这样做：{@link RecommendHashTable}
 *
 * @author LightDance
 */
public class BadHashTable implements Cloneable{
    private Entry[] buckets;

    BadHashTable(Entry[] buckets) {
        this.buckets = buckets;
    }

    @Override
    protected Object clone(){
        BadHashTable hashTable = null;
        try {
            hashTable = (BadHashTable) super.clone();
            hashTable.buckets = buckets.clone();
            return hashTable;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    private static class Entry {
        final Object key;
        Object value;
        Entry next;

        public Entry(Object key, Object value, Entry next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public String toString() {
            return "key: " + key + "; value :" + value;
        }

        //...
    }

    public static void main(String[] args) {
        Entry[] list = new Entry[3];
        for (int i = 0; i < 3; i++) {
            list[i] = new Entry(i , i + " - 1" , new Entry(i , i + " - 2" , null));
        }

        BadHashTable hashTable1 = new BadHashTable(list);
        BadHashTable hashTable2 = (BadHashTable) hashTable1.clone();

        hashTable1.buckets[0].next = null;

        System.out.println(hashTable2.buckets[0].next);
        System.out.println(hashTable1.buckets[1].equals(hashTable2.buckets[1]));
    }
}
