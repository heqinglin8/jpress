package io.jpress.module.example.service.provider;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import io.jboot.aop.annotation.Bean;
import io.jboot.components.cache.AopCache;
import io.jboot.components.cache.CacheTime;
import io.jboot.components.cache.annotation.CacheEvict;
import io.jboot.components.cache.annotation.Cacheable;
import io.jboot.components.cache.annotation.CachesEvict;
import io.jboot.db.model.Column;
import io.jboot.db.model.Columns;
import io.jboot.utils.StrUtil;
import io.jpress.module.example.service.ExampleCategoryService;
import io.jpress.module.example.model.ExampleCategory;
import io.jboot.service.JbootServiceBase;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Bean
public class ExampleCategoryServiceProvider extends JbootServiceBase<ExampleCategory> implements ExampleCategoryService {

    @Override
    @Cacheable(name = "exampleCategory", key = "type:#(type)", returnCopyEnable = true)
    public List<ExampleCategory> findListByType(String type) {
        return DAO.findListByColumns(Columns.create("type", type), "order_number asc,id desc");
    }

    @Override
    @Cacheable(name = "exampleCategory")
    public List<ExampleCategory> findListByType(String type, String orderBy, Integer count) {
        return DAO.findListByColumns(Columns.create("type", type), StrUtil.isNotBlank(orderBy) ? orderBy : "id desc", count);
    }

    @Override
    public void doUpdateProductCount(long categoryId) {
        long exampleCount = Db.queryLong("select count(*) from example_category_mapping where category_id = ? ", categoryId);
        ExampleCategory category = findById(categoryId);
        if (category != null) {
            category.setCount(exampleCount);
            update(category);
        }
    }

    /**
     * @param exampleId
     * @return
     */
    @Override
    @Cacheable(name = "example-category", key = "#(exampleId)", liveSeconds = 2 * CacheTime.HOUR, nullCacheEnable = true)
    public List<ExampleCategory> findListByProductId(long exampleId) {
        List<Record> mappings = Db.find("select * from example_category_mapping where example_id = ?", exampleId);
        if (mappings == null || mappings.isEmpty()) {
            return null;
        }

        return mappings
                .stream()
                .map(record -> DAO.findById(record.get("category_id")))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExampleCategory> findListByProductId(long exampleId, String type) {
        List<ExampleCategory> categoryList = findListByProductId(exampleId);
        if (categoryList == null || categoryList.isEmpty()) {
            return null;
        }
        return categoryList
                .stream()
                .filter(category -> type.equals(category.getType()))
                .collect(Collectors.toList());
    }


    @Override
    public List<ExampleCategory> findTagListByProductId(long exampleId) {
        return findListByProductId(exampleId, ExampleCategory.TYPE_TAG);
    }

    @Override
    public Long[] findCategoryIdsByProductId(long exampleId) {
        List<Record> records = Db.find("select * from example_category_mapping where example_id = ?", exampleId);
        if (records == null || records.isEmpty()) {
            return null;
        }

        return ArrayUtils.toObject(records.stream().mapToLong(record -> record.get("category_id")).toArray());
    }

    @Override
    public List<ExampleCategory> findOrCreateByTagString(String[] tags) {
        if (tags == null || tags.length == 0) {
            return null;
        }

        List<ExampleCategory> exampleCategories = new ArrayList<>();

        boolean needClearCache = false;

        for (String tag : tags) {

            if (StrUtil.isBlank(tag)) {
                continue;
            }

            //slug不能包含字符串点 " . "，否则url不能被访问
            String slug = tag.contains(".")
                    ? tag.replace(".", "_")
                    : tag;

            Columns columns = Columns.create("type", ExampleCategory.TYPE_TAG);
            columns.add(Column.create("slug", slug));

            ExampleCategory exampleCategory = DAO.findFirstByColumns(columns);

            if (exampleCategory == null) {
                exampleCategory = new ExampleCategory();
                exampleCategory.setTitle(tag);
                exampleCategory.setSlug(slug);
                exampleCategory.setType(ExampleCategory.TYPE_TAG);
                exampleCategory.save();
                needClearCache = true;
            }

            exampleCategories.add(exampleCategory);
        }

        if (needClearCache) {
            AopCache.removeAll("exampleCategory");
        }

        return exampleCategories;
    }

    @Override
    public ExampleCategory findFirstByTypeAndSlug(String type, String slug) {
        return DAO.findFirstByColumns(Columns.create("type", type).eq("slug", slug));
    }

    @Override
    public List<ExampleCategory> findCategoryListByProductId(long exampleId) {
        return findListByProductId(exampleId, ExampleCategory.TYPE_CATEGORY);
    }

    @Override
    public Page<ExampleCategory> paginateByType(int page, int pagesize, String type) {
        return DAO.paginateByColumn(page, pagesize, Column.create("type", type), "order_number asc,id desc");
    }

    @Override
    @CachesEvict({
            @CacheEvict(name = "exampleCategory", key = "*"),
            @CacheEvict(name = "example-category", key = "*"),
    })
    public void shouldUpdateCache(int action, Model model, Object id) {
        super.shouldUpdateCache(action, model, id);
    }

}