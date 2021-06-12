package io.jpress.module.example.service.provider;

import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;
import io.jboot.aop.annotation.Bean;
import io.jboot.db.model.Column;
import io.jboot.db.model.Columns;
import io.jpress.module.example.service.ExampleCommentService;
import io.jpress.module.example.model.ExampleComment;
import io.jboot.service.JbootServiceBase;
import io.jpress.module.example.service.ExampleService;
import io.jpress.module.example.service.provider.task.ExampleCommentReplyCountUpdateTask;
import io.jpress.service.UserService;

@Bean
public class ExampleCommentServiceProvider extends JbootServiceBase<ExampleComment> implements ExampleCommentService {

    @Inject
    private UserService userService;

    @Inject
    private ExampleService productService;

    @Override
    public long findCountByProductId(Long productId) {
        return DAO.findCountByColumn(Column.create("example_id",productId));
    }

    @Override
    public boolean deleteByIds(Object... ids) {
        for (Object id : ids) {
            deleteById(id);
        }
        return true;
    }

    @Override
    public void deleteCacheById(Object id) {
        DAO.deleteIdCacheById(id);
    }

    @Override
    public long findCountByStatus(int status) {
        return DAO.findCountByColumn(Column.create("status",status));
    }

    @Override
    public Page<ExampleComment> _paginateByStatus(int page, int pagesize, Long productId, String keyword, int status) {

        Columns columns = Columns.create("product_id", productId)
                .eq("status", status)
                .likeAppendPercent("content", keyword);

        Page<ExampleComment> p = DAO.paginateByColumns(page,
                pagesize,
                columns,
                "id desc");

        userService.join(p, "user_id");
        productService.join(p, "example_id");
        return p;
    }


    @Override
    public Page<ExampleComment> _paginateWithoutTrash(int page, int pagesize, Long productId, String keyword) {

        Columns columns = Columns.create("example_id", productId)
                .ne("status", ExampleComment.STATUS_TRASH)
                .likeAppendPercent("content", keyword);

        Page<ExampleComment> p = DAO.paginateByColumns(
                page,
                pagesize,
                columns,
                "id desc");


        userService.join(p, "user_id");
        productService.join(p, "example_id");
        return p;
    }

    @Override
    public Page<ExampleComment> _paginateByUserId(int page, int pagesize, long userId) {
        Page<ExampleComment> p = DAO.paginateByColumn(page, pagesize, Column.create("user_id", userId), "id desc");
        userService.join(p, "user_id");
        productService.join(p, "example_id");
        return p;
    }

    @Override
    public Page<ExampleComment> paginateByProductIdInNormal(int page, int pagesize, long productId) {
        Columns columns = Columns.create("example_id", productId);
        columns.eq("status", ExampleComment.STATUS_NORMAL);


        Page<ExampleComment> p = DAO.paginateByColumns(
                page,
                pagesize,
                columns,
                "id desc");

        join(p, "pid", "parent");
        joinParentUser(p);
        userService.join(p, "user_id");

        return p;
    }

    @Override
    public void doIncCommentReplyCount(long commentId) {
        ExampleCommentReplyCountUpdateTask.recordCount(commentId);
    }

    @Override
    public boolean doChangeStatus(Long id, int status) {
        ExampleComment comment = findById(id);
        comment.setStatus(status);
        return update(comment);
    }

    @Override
    public boolean deleteByProductId(Object productId) {
        return DAO.deleteByColumn(Column.create("example_id",productId));
    }

    private void joinParentUser(Page<ExampleComment> p) {
        if (p == null || p.getList().isEmpty()) {
            return;
        }

        for (ExampleComment comment : p.getList()) {
            userService.join((ExampleComment) comment.get("parent"), "user_id");
        }
    }

}