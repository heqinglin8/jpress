/**
 * Copyright (c) 2016-2020, Michael Yang 杨福海 (fuhai999@gmail.com).
 * <p>
 * Licensed under the GNU Lesser General Public License (LGPL) ,Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jpress.module.example.controller.admin;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import io.jboot.web.controller.annotation.RequestMapping;
import io.jboot.web.validate.EmptyValidate;
import io.jboot.web.validate.Form;
import io.jpress.JPressConsts;
import io.jpress.core.menu.annotation.AdminMenu;
import io.jpress.model.User;
import io.jpress.module.example.model.Example;
import io.jpress.module.example.model.ExampleComment;
import io.jpress.module.example.service.ExampleCommentService;
import io.jpress.web.base.AdminControllerBase;

import java.util.Date;
import java.util.Set;


@RequestMapping(value = "/admin/example/comment", viewPath = JPressConsts.DEFAULT_ADMIN_VIEW)
public class _ExampleCommentController extends AdminControllerBase {

    @Inject
    private ExampleCommentService commentService;

    @AdminMenu(text = "评论", groupId = "example", order = 88)
    public void list() {
        Integer status = getParaToInt("status");
        String key = getPara("keyword");
        Long exampleId = getParaToLong("exampleId");

        Page<ExampleComment> page =
                status == null
                        ? commentService._paginateWithoutTrash(getPagePara(), getPageSizePara(), exampleId, key)
                        : commentService._paginateByStatus(getPagePara(), getPageSizePara(), exampleId, key, status);

        setAttr("page", page);


        long unauditedCount = commentService.findCountByStatus(ExampleComment.STATUS_UNAUDITED);
        long trashCount = commentService.findCountByStatus(ExampleComment.STATUS_TRASH);
        long normalCount = commentService.findCountByStatus(ExampleComment.STATUS_NORMAL);

        setAttr("unauditedCount", unauditedCount);
        setAttr("trashCount", trashCount);
        setAttr("normalCount", normalCount);
        setAttr("totalCount", unauditedCount + trashCount + normalCount);

        render("product/comment_list.html");
    }


    public void edit() {
        int entryId = getParaToInt(0, 0);

        ExampleComment entry = entryId > 0 ? commentService.findById(entryId) : null;
        setAttr("productComment", entry);
        set("now", new Date());
        render("product/comment_edit.html");
    }


    public void reply() {
        long id = getIdPara();
        ExampleComment comment = commentService.findById(id);
        setAttr("comment", comment);
        render("product/comment_reply.html");
    }


    public void doReply(String content, Long productId, Long pid) {
        User user = getLoginedUser();

        ExampleComment comment = new ExampleComment();
        comment.setContent(content);
        comment.setUserId(user.getId());
        comment.setAuthor(user.getNickname());
        comment.setStatus(ExampleComment.STATUS_NORMAL);
        comment.setExampleId(productId);
        comment.setPid(pid);

        commentService.save(comment);
        renderOkJson();
    }




    public void doSave() {
        ExampleComment entry = getModel(ExampleComment.class, "exampleComment");
        commentService.saveOrUpdate(entry);
        renderJson(Ret.ok().set("id", entry.getId()));
    }


    public void doDel() {
        Long id = getIdPara();
        render(commentService.deleteById(id) ? Ret.ok() : Ret.fail());
    }


    public void doChangeStatus(Long id, int status) {
        render(commentService.doChangeStatus(id, status) ? OK : FAIL);
    }

    @EmptyValidate(@Form(name = "ids"))
    public void doDelByIds(){
        Set<String> idsSet = getParaSet("ids");
        render(commentService.deleteByIds(idsSet.toArray()) ? OK : FAIL);
    }
}