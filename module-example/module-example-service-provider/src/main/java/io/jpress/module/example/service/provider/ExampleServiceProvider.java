package io.jpress.module.example.service.provider;

import com.jfinal.aop.Inject;
import com.jfinal.kit.LogKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import io.jboot.aop.annotation.Bean;
import io.jboot.components.cache.annotation.CacheEvict;
import io.jboot.components.cache.annotation.Cacheable;
import io.jboot.components.cache.annotation.CachesEvict;
import io.jboot.db.model.Column;
import io.jboot.db.model.Columns;
import io.jboot.utils.StrUtil;
import io.jpress.module.example.model.ExampleCategory;
import io.jpress.module.example.service.ExampleCategoryService;
import io.jpress.module.example.service.ExampleCommentService;
import io.jpress.module.example.service.ExampleService;
import io.jpress.module.example.model.Example;
import io.jboot.service.JbootServiceBase;
import io.jpress.module.example.service.provider.search.ExampleSearcherFactory;
import io.jpress.module.example.service.provider.task.ExampleCommentsCountUpdateTask;
import io.jpress.module.example.service.provider.task.ExampleViewsCountUpdateTask;
import io.jpress.module.product.service.search.ExampleSearcher;
import io.jpress.service.UserService;
import io.jpress.web.seoping.SeoManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Bean
public class ExampleServiceProvider extends JbootServiceBase<Example> implements ExampleService {

    private static final String DEFAULT_ORDER_BY = "order_number desc,id desc";

    @Inject
    private UserService userService;

    @Inject
    private ExampleCommentService commentService;

    @Inject
    private ExampleCategoryService categoryService;

    @Override
    @CachesEvict({
            @CacheEvict(name = "examples", key = "*"),
            @CacheEvict(name = "example-category", key = "#(exampleId)"),
    })
    public void doUpdateCategorys(long exampleId, Long[] categoryIds) {
        Db.tx(() -> {
            Db.update("delete from example_category_mapping where example_id = ?", exampleId);

            if (categoryIds != null && categoryIds.length > 0) {
                List<Record> records = new ArrayList<>();
                for (long categoryId : categoryIds) {
                    Record record = new Record();
                    record.set("example_id", exampleId);
                    record.set("category_id", categoryId);
                    records.add(record);
                }
                Db.batchSave("example_category_mapping", records, records.size());
            }

            return true;
        });
    }

    @Override
    public void doUpdateCommentCount(long exampleId) {
        Example example = findById(exampleId);
        if (example == null) {
            return;
        }

        long count = commentService.findCountByProductId(exampleId);
        example.setCommentCount(count);
        example.update();
    }

    @Override
    @CacheEvict(name = "examples", key = "*")
    public boolean doChangeStatus(long id, int status) {
        Example example = findById(id);
        example.setStatus(status);
        return update(example);
    }

    @Override
    public Page<Example> _paginateByStatus(int page, int pagesize, String title, Long categoryId, int status) {

        return _paginateByBaseColumns(page
                , pagesize
                , Columns.create("example.status", status).likeAppendPercent("example.title", title)
                , categoryId
                , null);
    }

    @Override
    public Page<Example> _paginateWithoutTrash(int page, int pagesize, String title, Long categoryId) {

        return _paginateByBaseColumns(page
                , pagesize
                , Columns.create().ne("example.status", Example.STATUS_TRASH).likeAppendPercent("example.title", title)
                , categoryId
                , null);
    }


    @Override
    @Cacheable(name = "examples")
    public Page<Example> paginateInNormal(int page, int pagesize) {
        return paginateInNormal(page, pagesize, null);
    }


    @Override
    @Cacheable(name = "examples")
    public Page<Example> paginateInNormal(int page, int pagesize, String orderBy) {
        orderBy = StrUtil.obtainDefaultIfBlank(orderBy, DEFAULT_ORDER_BY);
        Columns columns = new Columns();
        columns.eq("status", Example.STATUS_NORMAL);
        Page<Example> dataPage = DAO.paginateByColumns(page, pagesize, columns, orderBy);
        return joinUserInfo(dataPage);
    }


    @Override
    @Cacheable(name = "examples")
    public Page<Example> paginateByCategoryIdInNormal(int page, int pagesize, long categoryId, String orderBy) {

        Columns columns = new Columns();
        columns.eq("m.category_id", categoryId);
        columns.eq("example.status", Example.STATUS_NORMAL);

        return _paginateByBaseColumns(page, pagesize, columns, categoryId, orderBy);
    }


    public Page<Example> _paginateByBaseColumns(int page, int pagesize, Columns baseColumns, Long categoryId, String orderBy) {

        Columns columns = baseColumns;
        columns.eq("m.category_id", categoryId);

        Page<Example> dataPage = DAO.leftJoinIf("example_category_mapping",categoryId != null).as("m")
                .on("example.id = m.`example_id`")
                .paginateByColumns(page,pagesize,columns,StrUtil.obtainDefaultIfBlank(orderBy, DEFAULT_ORDER_BY));

        return joinUserInfo(dataPage);
    }


    @Override
    @Cacheable(name = "examples", key = "#(columns.cacheKey)-#(orderBy)-#(count)", liveSeconds = 60 * 60)
    public List<Example> findListByColumns(Columns columns, String orderBy, Integer count) {
        return joinUserInfo(super.findListByColumns(columns, orderBy, count));
    }

    @Override
    @CacheEvict(name = "examples", key = "*")
    public void removeCacheById(Object id) {
        DAO.deleteIdCacheById(id);
    }

    @Override
    public Example findFirstBySlug(String slug) {
        return joinUserInfo(DAO.findFirstByColumn(Column.create("slug", slug)));
    }

    @Override
    public long findCountByStatus(int status) {
        return DAO.findCountByColumn(Column.create("status", status));
    }

    @Override
    public boolean batchDeleteByIds(Object... ids) {
        for (Object id : ids) {
            deleteById(id);
        }
        return true;
    }


    @Override
    @CachesEvict({
            @CacheEvict(name = "example-category", key = "#(id)"),
            @CacheEvict(name = "examples", key = "*")
    })
    public boolean deleteById(Object id) {

        //搜索搜索引擎的内容
        ExampleSearcherFactory.getSearcher().deleteExample(id);

        return Db.tx(() -> {
            boolean delOk = ExampleServiceProvider.super.deleteById(id);
            if (delOk == false) {
                return false;
            }

            //删除文章的管理分类
            List<Record> records = Db.find("select * from example_category_mapping where example_id = ? ", id);
            if (records != null && !records.isEmpty()) {
                //更新文章数量
                Db.update("delete from example_category_mapping where example_id = ?", id);
                records.forEach(record -> categoryService.doUpdateProductCount(record.get("category_id")));
            }


            //删除产品的所有评论
            commentService.deleteByProductId(id);
            return true;
        });
    }



    @Override
    public Object save(Example model) {
        Object id = super.save(model);
        if (id != null && model.isNormal()) {
            ExampleSearcherFactory.getSearcher().addExample(model);
        }
        return id;
    }



    @Override
    public boolean update(Example model) {
        boolean success = super.update(model);
        if (success) {
            if (model.isNormal()) {
                ExampleSearcherFactory.getSearcher().updateExample(model);
                SeoManager.me().baiduUpdate(model.getUrl());
            } else {
                ExampleSearcherFactory.getSearcher().deleteExample(model.getId());
            }
        }
        return success;
    }


    @Override
    @CachesEvict({
            @CacheEvict(name = "examples", key = "*"),
            @CacheEvict(name = "example-category", key = "#(id)", unless = "id == null"),
    })
    public void shouldUpdateCache(int action, Model model, Object id) {
        super.shouldUpdateCache(action, model, id);
    }

    @Override
    public void doIncProductViewCount(long exampleId) {
        ExampleViewsCountUpdateTask.recordCount(exampleId);
    }

    @Override
    public void doIncProductCommentCount(long exampleId) {
        ExampleCommentsCountUpdateTask.recordCount(exampleId);
    }

    @Override
    @Cacheable(name = "examples", liveSeconds = 60 * 60)
    public List<Example> findRelevantListByProductId(Long exampleId, int status, Integer count) {
        List<ExampleCategory> tags = categoryService.findListByProductId(exampleId, ExampleCategory.TYPE_TAG);
        if (tags == null || tags.isEmpty()) {
            return null;
        }

        List<Long> tagIds = tags.stream().map(category -> category.getId()).collect(Collectors.toList());

        Columns columns = Columns.create();
        columns.in("m.category_id", tagIds.toArray());
        columns.ne("example.id", exampleId);
        columns.eq("example.status", status);

        List<Example> list = DAO.leftJoin("example_category_mapping").as("m")
                .on("example.id = m.`example_id`")
                .findListByColumns(columns,count);

        return joinUserInfo(list);
    }

    @Override
    @Cacheable(name = "examples", liveSeconds = 60 * 60)
    public List<Example> findListByCategoryId(long categoryId, Boolean hasThumbnail, String orderBy, Integer count) {

        Columns columns = Columns.create()
                .eq("m.category_id",categoryId)
                .eq("example.status",Example.STATUS_NORMAL)
                .isNotNullIf("example.thumbnail",hasThumbnail != null && hasThumbnail)
                .isNullIf("example.thumbnail",hasThumbnail != null && !hasThumbnail);

        List<Example> list = DAO.leftJoin("example_category_mapping").as("m")
                .on("example.id = m.`example_id`")
                .findListByColumns(columns,orderBy,count);

        return joinUserInfo(list);
    }


    @Override
    public Example findNextById(long id) {
        Columns columns = Columns.create();
        columns.add(Column.create("id", id, Column.LOGIC_GT));
        columns.add(Column.create("status", Example.STATUS_NORMAL));
        return joinUserInfo(DAO.findFirstByColumns(columns));
    }

    @Override
    public Example findPreviousById(long id) {
        Columns columns = Columns.create();
        columns.add(Column.create("id", id, Column.LOGIC_LT));
        columns.add(Column.create("status", Example.STATUS_NORMAL));
        return joinUserInfo(DAO.findFirstByColumns(columns, "id desc"));
    }


    private Page<Example> joinUserInfo(Page<Example> page) {
        userService.join(page, "user_id");
        return page;
    }

    private List<Example> joinUserInfo(List<Example> list) {
        userService.join(list, "user_id");
        return list;
    }

    private Example joinUserInfo(Example example) {
        userService.join(example, "user_id");
        return example;
    }



    @Override
    public Page<Example> search(String queryString, int pageNum, int pageSize) {
        try {
            ExampleSearcher searcher = ExampleSearcherFactory.getSearcher();
            Page<Example> page = searcher.search(queryString, pageNum, pageSize);
            if (page != null) {
                return page;
            }
        } catch (Exception ex) {
            LogKit.error(ex.toString(), ex);
        }
        return new Page<>(new ArrayList<>(), pageNum, pageSize, 0, 0);
    }

    @Override
    @Cacheable(name = "examples")
    public Page<Example> searchIndb(String queryString, int pageNum, int pageSize) {
        Columns columns = Columns.create("status", Example.STATUS_NORMAL)
                .likeAppendPercent("title", queryString);
        return joinUserInfo(paginateByColumns(pageNum, pageSize, columns, "order_number desc,id desc"));
    }

}