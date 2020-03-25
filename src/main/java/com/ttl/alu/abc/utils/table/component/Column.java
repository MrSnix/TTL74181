package com.ttl.alu.abc.utils.table.component;

import com.ttl.alu.abc.utils.table.utils.Type;

import java.util.Objects;

public final class Column {

    private final String name;
    private final Type type;

    public Column(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String name() {
        return name;
    }

    public Type type() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Column column = (Column) o;
        return Objects.equals(name, column.name) &&
                type == column.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public String toString() {
        return "Column{" + "name='" + name + '\'' + ", type=" + type + '}';
    }
}
