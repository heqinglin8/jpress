package io.jpress.module.page.service.provider;

import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import io.jboot.aop.annotation.Bean;
import io.jboot.db.model.Column;
import io.jboot.db.model.Columns;
import io.jboot.service.JbootServiceBase;
import io.jboot.utils.ArrayUtil;
import io.jpress.commons.utils.SqlUtils;
import io.jpress.module.page.model.PageContact;
import io.jpress.module.page.service.PageContactService;

@Bean
public class PageContactServiceProvider extends JbootServiceBase<PageContact> implements PageContactService {

    @Inject
    private PageContactService pageService;


    @Override
    public PageContact findById(Object id) {
        PageContact comment = super.findById(id);
        return comment;
    }

    @Override
    public long findCountByStatus(String status) {
        return DAO.findCountByColumn(Column.create("status", status));
    }

    @Override
    public Page<PageContact> _paginateByStatus(int page, int pagesize, Long articleId, String keyword, String status) {
        Columns columns = Columns.create("article_id", articleId)
                .eq("status", status)
                .likeAppendPercent("content", keyword);

        Page<PageContact> p = DAO.paginateByColumns(page,
                pagesize,
                columns,
                "id desc");

        return p;
    }

    @Override
    public Page<PageContact> _paginateWithoutTrash(int page, int pagesize, Long articleId, String keyword) {
        Columns columns = Columns.create("article_id", articleId)
                .ne("status", PageContact.STATUS_TRASH)
                .likeAppendPercent("content", keyword);

        Page<PageContact> p = DAO.paginateByColumns(
                page,
                pagesize,
                columns,
                "id desc");


        return p;
    }

    @Override
    public Page<PageContact> paginateByPageIdInNormal(int page, int pagesize, long pageId) {
        Columns columns = Columns.create("page_id", pageId);
        columns.eq("status", PageContact.STATUS_NORMAL);


        Page<PageContact> p = DAO.paginateByColumns(
                page,
                pagesize,
                columns,
                "id desc");

        join(p, "pid", "parent");

        return p;
    }



    @Override
    public void doIncCommentReplyCount(long commentId) {
        Db.update("update single_page_comment set reply_count = reply_count + 1"
                + " where id = ? ", commentId);
    }

    @Override
    public boolean doChangeStatus(Long id, String status) {
        PageContact comment = findById(id);
        return update(comment);
    }

    @Override
    public boolean batchChangeStatusByIds(String status, Object... ids) {
        Columns c = Columns.create().in("id", ids);
        Object[] paras = ArrayUtil.concat(new Object[]{status}, c.getValueArray());

        return Db.update("update single_page_comment SET `status` = ? " + SqlUtils.toWhereSql(c), paras) > 0;
    }
}