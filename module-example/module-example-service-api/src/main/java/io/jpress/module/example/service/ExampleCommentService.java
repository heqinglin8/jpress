package io.jpress.module.example.service;

import com.jfinal.plugin.activerecord.Page;
import io.jpress.module.example.model.ExampleComment;
import io.jboot.db.model.Columns;

import java.util.List;

public interface ExampleCommentService  {

    /**
     * 根据主键查找Model
     *
     * @param id
     * @return
     */
    public ExampleComment findById(Object id);


    /**
     * 根据 Columns 查找单条数据
     *
     * @param columns
     * @return
     */
    public ExampleComment findFirstByColumns(Columns columns);

    /**
     * 根据 Columns 查找单条数据
     *
     * @param columns
     * @param orderBy
     * @return
     */
    public ExampleComment findFirstByColumns(Columns columns, String orderBy);


    /**
     * 查找全部数据
     *
     * @return
     */
    public List<ExampleComment> findAll();


    /**
     * 根据 Columns 查找数据
     *
     * @param columns
     * @return
     */
    public List<ExampleComment> findListByColumns(Columns columns);


    /**
     * 根据 Columns 查找数据
     *
     * @param columns
     * @param orderBy
     * @return
     */
    public List<ExampleComment> findListByColumns(Columns columns, String orderBy);

    /**
     * 根据 Columns 查找数据
     *
     * @param columns
     * @param count
     * @return
     */
    public List<ExampleComment> findListByColumns(Columns columns, Integer count);

    /**
     * 根据 Columns 查找数据
     *
     * @param columns
     * @param orderBy
     * @param count
     * @return
     */
    public List<ExampleComment> findListByColumns(Columns columns, String orderBy, Integer count);


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
    public boolean delete(ExampleComment model);


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
    public Object save(ExampleComment model);


    /**
     * 保存或更新
     *
     * @param model
     * @return id if success
     */
    public Object saveOrUpdate(ExampleComment model);

    /**
     * 更新
     *
     * @param model
     * @return
     */
    public boolean update(ExampleComment model);


    /**
     * 分页
     *
     * @param page
     * @param pageSize
     * @return
     */
    public Page<ExampleComment> paginate(int page, int pageSize);


    /**
     * 分页
     *
     * @param page
     * @param pageSize
     * @return
     */
    public Page<ExampleComment> paginateByColumns(int page, int pageSize, Columns columns);


    /**
     * 分页
     *
     * @param page
     * @param pageSize
     * @param columns
     * @param orderBy
     * @return
     */
    public Page<ExampleComment> paginateByColumns(int page, int pageSize, Columns columns, String orderBy);


    public long findCountByProductId(Long productId);


    /**
     * batch del
     * @param ids
     * @return
     */
    public boolean deleteByIds(Object... ids);


    public void deleteCacheById(Object id);
    /**
     * count
     * @param status
     * @return
     */
    public long findCountByStatus(int status);


    public Page<ExampleComment> _paginateByStatus(int page, int pagesize, Long productId, String keyword, int status);

    public Page<ExampleComment> _paginateWithoutTrash(int page, int pagesize, Long productId, String keyword);

    public Page<ExampleComment> _paginateByUserId(int page, int pagesize, long userId);

    public Page<ExampleComment> paginateByProductIdInNormal(int page, int pagesize, long productId);

    public void doIncCommentReplyCount(long commentId);

    public boolean doChangeStatus(Long id, int status);

    public boolean deleteByProductId(Object productId);

}