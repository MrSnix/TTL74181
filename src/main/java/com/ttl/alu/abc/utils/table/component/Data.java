package com.ttl.alu.abc.utils.table.component;

import com.ttl.alu.abc.utils.table.utils.Type;

import java.util.Objects;

public final class Data<T> {

    private final String key;
    private final T value;
    private final Type type;

    public Data(String key, T value, Type type) {
        this.key = key;
        this.value = value;
        this.type = type;
    }

    public String key() {
        return key;
    }

    public T value() {
        return value;
    }

    public Type type() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Data<?> data = (Data<?>) o;
        return Objects.equals(key, data.key) &&
                Objects.equals(value, data.value) &&
                type == data.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value, type);
    }

    @Override
    public String toString() {
        return "Data{" +
                "key='" + key + '\'' +
                ", value=" + value +
                ", type=" + type +
                '}';
    }
}
