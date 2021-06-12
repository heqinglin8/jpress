package io.jpress.module.example.service;

import com.jfinal.plugin.activerecord.Page;
import io.jboot.service.JbootServiceJoiner;
import io.jpress.module.example.model.Example;
import io.jboot.db.model.Columns;

import java.util.List;

public interface ExampleService extends JbootServiceJoiner {

    /**
     * 根据主键查找Model
     *
     * @param id
     * @return
     */
    public Example findById(Object id);


    /**
     * 根据 Columns 查找单条数据
     *
     * @param columns
     * @return
     */
    public Example findFirstByColumns(Columns columns);

    /**
     * 根据 Columns 查找单条数据
     *
     * @param columns
     * @param orderBy
     * @return
     */
    public Example findFirstByColumns(Columns columns, String orderBy);


    /**
     * 查找全部数据
     *
     * @return
     */
    public List<Example> findAll();


    /**
     * 根据 Columns 查找数据
     *
     * @param columns
     * @return
     */
    public List<Example> findListByColumns(Columns columns);


    /**
     * 根据 Columns 查找数据
     *
     * @param columns
     * @param orderBy
     * @return
     */
    public List<Example> findListByColumns(Columns columns, String orderBy);

    /**
     * 根据 Columns 查找数据
     *
     * @param columns
     * @param count
     * @return
     */
    public List<Example> findListByColumns(Columns columns, Integer count);

    /**
     * 根据 Columns 查找数据
     *
     * @param columns
     * @param orderBy
     * @param count
     * @return
     */
    public List<Example> findListByColumns(Columns columns, String orderBy, Integer count);


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
    public boolean delete(Example model);


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
    public Object save(Example model);


    /**
     * 保存或更新
     *
     * @param model
     * @return id if success
     */
    public Object saveOrUpdate(Example model);

    /**
     * 更新
     *
     * @param model
     * @return
     */
    public boolean update(Example model);


    /**
     * 分页
     *
     * @param page
     * @param pageSize
     * @return
     */
    public Page<Example> paginate(int page, int pageSize);


    /**
     * 分页
     *
     * @param page
     * @param pageSize
     * @return
     */
    public Page<Example> paginateByColumns(int page, int pageSize, Columns columns);


    /**
     * 分页
     *
     * @param page
     * @param pageSize
     * @param columns
     * @param orderBy
     * @return
     */
    public Page<Example> paginateByColumns(int page, int pageSize, Columns columns, String orderBy);

    public void removeCacheById(Object id);

    public void doUpdateCategorys(long productId, Long[] categoryIds);

    public void doUpdateCommentCount(long productId);

    public boolean doChangeStatus(long id, int status);

    public Page<Example> _paginateByStatus(int page, int pagesize, String title, Long categoryId, int status);

    public Page<Example> _paginateWithoutTrash(int page, int pagesize, String title, Long categoryId);

    public Page<Example> paginateInNormal(int page, int pagesize);

    public Page<Example> paginateInNormal(int page, int pagesize, String orderBy);

    public Page<Example> paginateByCategoryIdInNormal(int page, int pagesize, long categoryId, String orderBy);

    public Example findFirstBySlug(String slug);

    public long findCountByStatus(int status);

    public void doIncProductViewCount(long productId);

    public void doIncProductCommentCount(long productId);

    public List<Example> findRelevantListByProductId(Long productId, int status, Integer count);

    public List<Example> findListByCategoryId(long categoryId, Boolean hasThumbnail, String orderBy, Integer count);

    public Example findNextById(long id);

    public Example findPreviousById(long id);

    public Page<Example> search(String queryString, int pageNum, int pageSize);

    public Page<Example> searchIndb(String queryString, int pageNum, int pageSize);

}