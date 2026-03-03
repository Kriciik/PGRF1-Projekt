package model;

public interface Vectorizable<T> {
    T mul(double d);
    T add(T v);
}
