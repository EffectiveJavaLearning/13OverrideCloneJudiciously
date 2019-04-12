package hashtable;

/**
 * 使用深层复制，解决{@link BadHashTable}中克隆前后对象共用相同的bucket[]而造成的不稳定性
 * <p>
 * 本类在Entity中提供了两种复制方式，第一种是递归调用自身，但这种方式当递归次数特别多时容易爆栈，
 * 参考{@link Entry#deepCopy1()};;
 * 另一种是用迭代方式构建，这种方式可以防止前一个方式所述的问题{@link Entry#deepCopy2()}
 *
 * @author LightDance
 */
public class RecommendHashTable implements Cloneable {

    private Entry[] buckets;

    public RecommendHashTable(Entry[] buckets) {
        this.buckets = buckets;
    }

    @Override
    public RecommendHashTable clone() {
        try {
            RecommendHashTable result = (RecommendHashTable) super.clone();
            result.buckets = new Entry[buckets.length];
            for (int i = 0; i < buckets.length; i++) {
                if (buckets[i] != null) {
                    result.buckets[i] = buckets[i].deepCopy1();
                }
            }
            return result;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
    //...

    public static class Entry {
        final Object key;
        Object value;
        Entry next;

        public Entry(Object key, Object value, Entry next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        /**递归方式的深层克隆(deep clone)*/
        public Entry deepCopy1() {
            return new Entry(key, value, next == null ? null : next.deepCopy1());
        }

        /**迭代方式的深层克隆(deep clone)*/
        public Entry deepCopy2() {
            Entry result = new Entry(key, value, next);
            for (Entry p = result; p.next != null; p = p.next) {
                p.next = new Entry(p.next.key, p.next.value, p.next.next);
            }
            return result;
        }

        @Override
        public String toString() {
            return "key: " + key + "; value :" + value;
        }
    }
}
