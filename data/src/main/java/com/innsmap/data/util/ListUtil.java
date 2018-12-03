package com.innsmap.data.util;

import android.os.Build;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Queue;

/**
 * @author yongning.yang@tescomm.com.cn
 * @date 2018/11/2
 * @Description
 */
public class ListUtil {
    public static <T> List<T> sort(List<T> points, Comparator<T> comparator) {
        Object[] a = points.toArray();
        Arrays.sort(a, (Comparator) comparator);
        ListIterator<T> i = points.listIterator();
        for (Object e : a) {
            i.next();
            i.set((T) e);
        }

        return points;
    }

    public interface Consumer<T> {

        /**
         * Performs this operation on the given argument.
         *
         * @param t the input argument
         */
        void accept(T t);

        /**
         * Returns a composed {@code Consumer} that performs, in sequence, this
         * operation followed by the {@code after} operation. If performing either
         * operation throws an exception, it is relayed to the caller of the
         * composed operation.  If performing this operation throws an exception,
         * the {@code after} operation will not be performed.
         *
         * @param after the operation to perform after this operation
         * @return a composed {@code Consumer} that performs in sequence this
         * operation followed by the {@code after} operation
         * @throws NullPointerException if {@code after} is null
         */
        default java.util.function.Consumer<T> andThen(Consumer<? super T> after) {
            Objects.requireNonNull(after);
            return (T t) -> {
                accept(t);
                after.accept(t);
            };
        }
    }


    public static <T> void forEach(List<T> list, Consumer<T> consumer) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            java.util.function.Consumer<T> consumer1 = new java.util.function.Consumer<T>() {
                @Override
                public void accept(T o) {
                    consumer.accept(o);
                }
            };
            list.forEach(consumer1);
        } else {
            Objects.requireNonNull(consumer);
            for (T t : list) {
                consumer.accept(t);
            }

        }
    }

    public static <T> void forEach(Collection<T> list, Consumer<T> consumer) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            java.util.function.Consumer<T> consumer1 = new java.util.function.Consumer<T>() {
                @Override
                public void accept(T o) {
                    consumer.accept(o);
                }
            };
            list.forEach(consumer1);
        } else {
            Objects.requireNonNull(consumer);
            for (T t : list) {
                consumer.accept(t);
            }

        }
    }


    /**
     * 获取列表中符合条件的项
     *
     * @param sources
     * @param selector
     * @param <T>
     * @return
     */
    public static <T> List<T> select(List<T> sources, Selector<T> selector) {
        List<T> results = new ArrayList<>();
        for (T src : sources) {
            if (selector.match(src)) {
                results.add(src);
            }
        }
        return results;
    }

    /**
     * 从列表中查找制定的对象
     *
     * @param sources
     * @param matchSingle
     * @param <T>
     * @return
     */
    public static <T> T find(List<T> sources, Matcher<T> matchSingle) {
        for (T src : sources)
            if (matchSingle.match(src))
                return src;
        return null;
    }

    /**
     * 将列表降为字符串传递
     *
     * @param types
     * @param split
     * @param <T>
     * @return
     */
    public static <T> String downToString(List<T> types, String split) {
        StringBuilder sb = new StringBuilder();
        for (T src : types)
            sb.append(src).append(split);
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 将降围后的字符串还原为列表
     *
     * @param content
     * @param split
     * @param <T>
     * @return
     */
    public static <T> List<T> upToList(String content, String split, Chooser<String, T> chooser) {
        String[] sources = content.split(split);
        return query(Arrays.asList(sources), chooser);
    }

    public static abstract class Selector<T> {
        public abstract boolean match(T src);
    }

    /**
     * 获取一个列表中的某个列表型字段的合集
     *
     * @param sources
     * @param chooser
     * @param <T1>
     * @param <T2>
     * @return
     */
    public static <T1, T2> List<T2> query(List<T1> sources, Chooser<T1, T2> chooser) {
        List<T2> results = new ArrayList<>();
        if (chooser instanceof ChooserSingle) {
            for (T1 src : sources) {
                results.add((T2) (((ChooserSingle) chooser).chooseSingle(src)));
            }
        } else if (chooser instanceof ChooserSingleWithoutNull) {
            for (T1 src : sources) {
                T2 object = (T2) (((ChooserSingleWithoutNull) chooser).chooseSingle(src));
                if (object != null)
                    results.add(object);
            }
        } else if (chooser instanceof ChooserSingleWithoutExistAndNull) {
            for (T1 src : sources) {
                T2 object = (T2) (((ChooserSingleWithoutExistAndNull) chooser).chooseSingle(src));
                if (object != null && !results.contains(object))
                    results.add(object);
            }
        } else {
            for (T1 src : sources) {
                results.addAll(chooser.choose(src));
            }
        }

        return results;
    }

    public static <T1, T2> List<T2> query(Queue<T1> sources, Chooser<T1, T2> chooser) {
        List<T2> results = new ArrayList<>();
        if (sources == null || sources.isEmpty()) {
            return results;
        }
        if (chooser instanceof ChooserSingle) {
            for (T1 src : sources) {
                results.add((T2) (((ChooserSingle) chooser).chooseSingle(src)));
            }
        } else if (chooser instanceof ChooserSingleWithoutNull) {
            for (T1 src : sources) {
                T2 object = (T2) (((ChooserSingleWithoutNull) chooser).chooseSingle(src));
                if (object != null)
                    results.add(object);
            }
        } else if (chooser instanceof ChooserSingleWithoutExistAndNull) {
            for (T1 src : sources) {
                T2 object = (T2) (((ChooserSingleWithoutExistAndNull) chooser).chooseSingle(src));
                if (object != null && !results.contains(object))
                    results.add(object);
            }
        } else {
            for (T1 src : sources) {
                results.addAll(chooser.choose(src));
            }
        }

        return results;
    }

    public static <T1, T2> List<T2> empty(Queue<T1> sources, Chooser<T1, T2> chooser) {
        List<T2> results = new ArrayList<>();
        if (sources == null || sources.isEmpty()) {
            return results;
        }
        if (chooser instanceof ChooserSingle) {
            while (!sources.isEmpty()) {
                T1 src = sources.poll();
                results.add((T2) (((ChooserSingle) chooser).chooseSingle(src)));
            }
        } else if (chooser instanceof ChooserSingleWithoutNull) {
            while (!sources.isEmpty()) {
                T1 src = sources.poll();
                T2 object = (T2) (((ChooserSingleWithoutNull) chooser).chooseSingle(src));
                if (object != null)
                    results.add(object);
            }
        } else if (chooser instanceof ChooserSingleWithoutExistAndNull) {
            while (!sources.isEmpty()) {
                T1 src = sources.poll();
                T2 object = (T2) (((ChooserSingleWithoutExistAndNull) chooser).chooseSingle(src));
                if (object != null && !results.contains(object))
                    results.add(object);
            }
        } else {
            while (!sources.isEmpty()) {
                T1 src = sources.poll();
                results.addAll(chooser.choose(src));
            }
        }

        return results;
    }

    /**
     * 从类型 T1 返回类型T2
     *
     * @param <T1>
     * @param <T2>
     */
    public static abstract class Chooser<T1, T2> {
        public abstract List<T2> choose(T1 source);
    }

    /**
     * 从类型T1 返回类型T2
     *
     * @param <T1>
     * @param <T2>
     */
    public static abstract class ChooserSingle<T1, T2> extends Chooser<T1, T2> {
        @Override
        public List<T2> choose(T1 source) {
            return null;
        }

        public abstract T2 chooseSingle(T1 source);
    }

    public static abstract class ChooserSingleWithoutNull<T1, T2> extends Chooser<T1, T2> {
        @Override
        public List<T2> choose(T1 source) {
            return null;
        }

        public abstract T2 chooseSingle(T1 source);
    }

    public static abstract class ChooserSingleWithoutExistAndNull<T1, T2> extends Chooser<T1, T2> {
        @Override
        public List<T2> choose(T1 source) {
            return null;
        }

        public abstract T2 chooseSingle(T1 source);
    }

    public static abstract class Matcher<T> {
        public abstract boolean match(T source);
    }

    /**
     * 在集合中检测是否包含一个项
     *
     * @param sources
     * @param matcher
     * @param <T>
     * @return
     */
    public static <T> boolean contains(List<T> sources, Matcher<T> matcher) {
        T find = find(sources, matcher);

        return find != null;
    }

    /**
     * 在列表中查找符合条件的第一个项的索引
     *
     * @param sources
     * @param matcher
     * @return
     */
    public static <T> int indexOf(List<T> sources, Matcher<T> matcher) {
        if (isNullOrEmpty(sources)) {
            return -1;
        }
        int i = 0;
        for (T source : sources) {
            if (matcher.match(source)) {
                return i;
            } else {
                i++;
            }
        }

        return -1;
    }

    /**
     * 判断列表是否未空或empty
     *
     * @param sources
     * @param <T>
     * @return
     */
    public static <T> boolean isNullOrEmpty(List<T> sources) {
        if (sources == null || sources.isEmpty()) {
            return true;
        }

        return false;
    }

    /**
     * 判断两个列表完全相同
     *
     * @param listA
     * @param listB
     * @param <T>
     * @return
     */
    public static <T> boolean isSame(List<T> listA, List<T> listB) {
        if (listA == null && listB == null) {
            return true;
        }

        if (listA == null || listB == null) {
            return false;
        }

        if (listA.size() != listB.size()) {
            return false;
        }

        int size = listA.size();
        for (int i = 0; i < size; i++) {
            if (listA.get(i) == null && listB.get(i) == null) {

            } else if (listA.get(i) == null || listB.get(i) == null) {
                return false;
            } else if (!listA.get(i).equals(listB.get(i))) {
                return false;
            }
        }

        return true;
    }
}
