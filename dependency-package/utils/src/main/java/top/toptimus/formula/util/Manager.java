package top.toptimus.formula.util;

import java.util.Collection;
import java.util.Hashtable;

/**
 * 通用对象管理器
 * <p>本对象管理器基于{@link Hashtable}构建，实现了基本的管理功能</p>
 *
 * @param <object> 对象的类名
 * @author gaoyu
 * @version 1.6.4.7 [20150915 gaoyu] <br>
 * - 增加{@link #values() values()}方法 <br>
 */
public class Manager<object> {

    /**
     * 所管理的对象列表
     */
    private Hashtable<String, object> objs = new Hashtable<String, object>();

    /**
     * 按照id来获取对象
     * <p>如果在当前对象列表中找不到该ID的对象，则返回为null.
     *
     * @param id 对象id
     * @return 对象实例
     */
    public object get(String id) {
        return objs.get(id);
    }

    /**
     * 删除指定id的对象
     *
     * @param id 对象id
     */
    public void remove(String id) {
        objs.remove(id);
    }

    /**
     * 向管理器中增加对象
     *
     * @param id  对象id
     * @param obj 对象实例
     */
    public void add(String id, object obj) {
        objs.put(id, obj);
    }

    /**
     * 获取当前列表中所有对象ID列表
     *
     * @return ID列表
     */
    public String[] keys() {
        return objs.keySet().toArray(new String[0]);
    }

    /**
     * 获取values
     *
     * @return values
     */
    public Collection<object> values() {
        return objs.values();
    }

    /**
     * 获取对象列表的大小
     *
     * @return 大小
     */
    public int size() {
        return objs.size();
    }

    /**
     * 清空对象
     *
     * @since 1.2.1
     */
    public void clear() {
        objs.clear();
    }
}
