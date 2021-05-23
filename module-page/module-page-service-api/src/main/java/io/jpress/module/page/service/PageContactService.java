package io.jpress.module.page.service;

import com.jfinal.plugin.activerecord.Page;
import io.jboot.db.model.Columns;
import io.jpress.module.page.model.PageContact;

import java.util.List;

public interface PageContactService {

    /**
     * 根据主键查找Model
     *
     * @param id
     * @return
     */
    public PageContact findById(Object id);


    /**
     * 根据 Columns 查找单条数据
     *
     * @param columns
     * @return
     */
    public PageContact findFirstByColumns(Columns columns);

    /**
     * 根据 Columns 查找单条数据
     *
     * @param columns
     * @param orderBy
     * @return
     */
    public PageContact findFirstByColumns(Columns columns, String orderBy);


    /**
     * 查找全部数据
     *
     * @return
     */
    public List<PageContact> findAll();


    /**
     * 根据 Columns 查找数据
     *
     * @param columns
     * @return
     */
    public List<PageContact> findListByColumns(Columns columns);


    /**
     * 根据 Columns 查找数据
     *
     * @param columns
     * @param orderBy
     * @return
     */
    public List<PageContact> findListByColumns(Columns columns, String orderBy);

    /**
     * 根据 Columns 查找数据
     *
     * @param columns
     * @param count
     * @return
     */
    public List<PageContact> findListByColumns(Columns columns, Integer count);

    /**
     * 根据 Columns 查找数据
     *
     * @param columns
     * @param orderBy
     * @param count
     * @return
     */
    public List<PageContact> findListByColumns(Columns columns, String orderBy, Integer count);


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
    public boolean delete(PageContact model);


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
    public Object save(PageContact model);


    /**
     * 保存或更新
     *
     * @param model
     * @return id if success
     */
    public Object saveOrUpdate(PageContact model);

    /**
     * 更新
     *
     * @param model
     * @return
     */
    public boolean update(PageContact model);


    /**
     * 分页
     *
     * @param page
     * @param pageSize
     * @return
     */
    public Page<PageContact> paginate(int page, int pageSize);


    /**
     * 分页
     *
     * @param page
     * @param pageSize
     * @return
     */
    public Page<PageContact> paginateByColumns(int page, int pageSize, Columns columns);


    /**
     * 分页
     *
     * @param page
     * @param pageSize
     * @param columns
     * @param orderBy
     * @return
     */
    public Page<PageContact> paginateByColumns(int page, int pageSize, Columns columns, String orderBy);

    public long findCountByStatus(String status);

    public Page<PageContact> _paginateByStatus(int page, int pagesize, Long articleId, String keyword, String status);

    public Page<PageContact> _paginateWithoutTrash(int page, int pagesize, Long articleId, String keyword);

    public Page<PageContact> paginateByPageIdInNormal(int page, int pagesize, long pageId);

    public void doIncCommentReplyCount(long commentId);


    public boolean doChangeStatus(Long id, String status);

    public boolean batchChangeStatusByIds(String statusNormal, Object... toArray);
}