/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 02.07.11
 * Time: 15:28
 * To change this template use File | Settings | File Templates.
 */
public class Pair<T> {
    private T first;
    private T second;

    public Pair(T first, T second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public T getSecond() {
        return second;
    }
}
