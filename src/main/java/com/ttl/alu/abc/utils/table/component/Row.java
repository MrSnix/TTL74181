package com.ttl.alu.abc.utils.table.component;

import java.util.*;
import java.util.stream.Collectors;

import static com.ttl.alu.abc.utils.table.utils.Type.*;

public final class Row<I, O> {

    private final int id;
    private final Map<Column, Data<I>> inputs;
    private final Map<Column, Data<O>> outputs;

    public Row(int id, List<Data<I>> inputs, List<Data<O>> outputs) {

        this.id = id;
        this.inputs = new LinkedHashMap<>();
        this.outputs = new LinkedHashMap<>();

        for (Data<I> value : inputs) {

            if(value.type() == OUT)
                throw new IllegalArgumentException("You cannot insert an output type inside an input field => " + value);

            this.inputs.put(new Column(value.key(), value.type()), value);
        }

        for (Data<O> value : outputs) {

            if(value.type() == IN)
                throw new IllegalArgumentException("You cannot insert an input type inside an output field => " + value);

            this.outputs.put(new Column(value.key(), value.type()), value);
        }
    }

    public int id() {
        return id;
    }

    public I in(Column key){

        if(key == null)
            throw new IllegalArgumentException("The key cannot be null");

        if(this.inputs.get(key) == null)
            throw new IllegalArgumentException("The following column does not exists => " + key);

        return this.inputs.get(key).value();
    }

    public I in(String key){

        if(key == null)
            throw new IllegalArgumentException("The key cannot be null");

        Optional<Column> col = this.inputs.keySet().stream().filter(column -> column.name().equals(key)).findFirst();

        if(!col.isPresent()){
            throw new IllegalArgumentException("The following column does not exists => " + key);
        }

        return in(col.get());
    }

    public O out(Column key){

        if(key == null)
            throw new IllegalArgumentException("The key cannot be null");

        if(this.outputs.get(key) == null)
            throw new IllegalArgumentException("The following column does not exists => " + key);

        return this.outputs.get(key).value();
    }

}
