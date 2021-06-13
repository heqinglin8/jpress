package io.jpress.module.example.service;

import com.jfinal.plugin.activerecord.Page;
import io.jpress.module.example.model.ExampleCategory;
import io.jboot.db.model.Columns;

import java.util.List;

public interface ExampleCategoryService  {

    /**
     * 根据主键查找Model
     *
     * @param id
     * @return
     */
    public ExampleCategory findById(Object id);


    /**
     * 根据 Columns 查找单条数据
     *
     * @param columns
     * @return
     */
    public ExampleCategory findFirstByColumns(Columns columns);

    /**
     * 根据 Columns 查找单条数据
     *
     * @param columns
     * @param orderBy
     * @return
     */
    public ExampleCategory findFirstByColumns(Columns columns, String orderBy);


    /**
     * 查找全部数据
     *
     * @return
     */
    public List<ExampleCategory> findAll();


    /**
     * 根据 Columns 查找数据
     *
     * @param columns
     * @return
     */
    public List<ExampleCategory> findListByColumns(Columns columns);


    /**
     * 根据 Columns 查找数据
     *
     * @param columns
     * @param orderBy
     * @return
     */
    public List<ExampleCategory> findListByColumns(Columns columns, String orderBy);

    /**
     * 根据 Columns 查找数据
     *
     * @param columns
     * @param count
     * @return
     */
    public List<ExampleCategory> findListByColumns(Columns columns, Integer count);

    /**
     * 根据 Columns 查找数据
     *
     * @param columns
     * @param orderBy
     * @param count
     * @return
     */
    public List<ExampleCategory> findListByColumns(Columns columns, String orderBy, Integer count);


    /**
     * 根据提交查询数据量
     *
     * @param columns
     * @return
     */
    public long findCountByColumns(Columns columns);


    /**
     * 根据ID 删除model
     *
     * @param id
     * @return
     */
    public boolean deleteById(Object id);


    /**
     * 删除
     *
     * @param model
     * @return
     */
    public boolean delete(ExampleCategory model);


    /**
     * 根据 多个 id 批量删除
     *
     * @param ids
     * @return
     */
    public boolean batchDeleteByIds(Object... ids);


    /**
     * 保存到数据库
     *
     * @param model
     * @return id if success
     */
    public Object save(ExampleCategory model);


    /**
     * 保存或更新
     *
     * @param model
     * @return id if success
     */
    public Object saveOrUpdate(ExampleCategory model);

    /**
     * 更新
     *
     * @param model
     * @return
     */
    public boolean update(ExampleCategory model);


    /**
     * 分页
     *
     * @param page
     * @param pageSize
     * @return
     */
    public Page<ExampleCategory> paginate(int page, int pageSize);


    /**
     * 分页
     *
     * @param page
     * @param pageSize
     * @return
     */
    public Page<ExampleCategory> paginateByColumns(int page, int pageSize, Columns columns);


    /**
     * 分页
     *
     * @param page
     * @param pageSize
     * @param columns
     * @param orderBy
     * @return
     */
    public Page<ExampleCategory> paginateByColumns(int page, int pageSize, Columns columns, String orderBy);


    public List<ExampleCategory> findListByType(String type);

    public List<ExampleCategory> findListByType(String type,String orderBy,Integer count);

    public void doUpdateProductCount(long categoryId);

    public List<ExampleCategory> findListByProductId(long productId, String type);

    public List<ExampleCategory> findListByProductId(long exampleId);

    public List<ExampleCategory> findTagListByProductId(long productId);

    public Long[] findCategoryIdsByProductId(long productId);

    public List<ExampleCategory> findOrCreateByTagString(String[] tags);

    public ExampleCategory findFirstByTypeAndSlug(String type, String slug);

    public List<ExampleCategory> findCategoryListByProductId(long productId);

    public Page<ExampleCategory> paginateByType(int page, int pagesize, String type);

}