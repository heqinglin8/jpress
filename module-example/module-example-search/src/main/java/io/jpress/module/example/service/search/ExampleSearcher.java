package io.jpress.module.example.service.search;

import com.jfinal.plugin.activerecord.Page;
import io.jpress.module.example.model.Example;

public interface ExampleSearcher {

    String HIGH_LIGHT_CLASS = "search-highlight";

    public void addExample(Example example);

    public void deleteExample(Object id);

    public void updateExample(Example example);

    public Page<Example> search(String keyword, int pageNum, int pageSize);
}
