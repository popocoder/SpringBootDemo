package bootdemo.demo.redis;

public interface RedisFunction<T, E> {

    /**
     * @Description 根据不同参数返回不同对象
     * @param e
     * @return
     */
    T callBack(E e);
}
