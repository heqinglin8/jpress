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
package io.jpress.module.example.controller.front;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;
import io.jboot.utils.CookieUtil;
import io.jboot.utils.RequestUtil;
import io.jboot.utils.StrUtil;
import io.jboot.web.controller.annotation.RequestMapping;
import io.jpress.JPressOptions;
import io.jpress.commons.dfa.DFAUtil;
import io.jpress.commons.utils.CommonsUtils;
import io.jpress.model.User;
import io.jpress.module.example.ExampleNotifyKit;
import io.jpress.module.example.interceptor.ExampleValidate;
import io.jpress.module.example.model.Example;
import io.jpress.module.example.model.ExampleCategory;
import io.jpress.module.example.model.ExampleComment;
import io.jpress.module.example.model.ExampleImage;
import io.jpress.module.example.service.ExampleCategoryService;
import io.jpress.module.example.service.ExampleCommentService;
import io.jpress.module.example.service.ExampleImageService;
import io.jpress.module.example.service.ExampleService;
import io.jpress.service.OptionService;
import io.jpress.service.UserCartService;
import io.jpress.service.UserFavoriteService;
import io.jpress.service.UserService;
import io.jpress.web.base.TemplateControllerBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Michael Yang 杨福海 （fuhai999@gmail.com）
 * @version V1.0
 * @Title: 产品前台页面Controller
 * @Package io.jpress.module.example.controller
 */
@RequestMapping("/example")
public class ExampleController extends TemplateControllerBase {

    @Inject
    private ExampleService exampleService;

    @Inject
    private ExampleImageService imageService;

    @Inject
    private UserService userService;

    @Inject
    private ExampleCategoryService categoryService;

    @Inject
    private OptionService optionService;

    @Inject
    private ExampleCommentService commentService;

    @Inject
    private UserCartService cartService;

    @Inject
    private UserFavoriteService favoriteService;


    public void index() {
        Example example = getProduct();

        //当产品处于下架等的时候，显示404
        render404If(example == null || !example.isNormal());

        if (getLoginedUser() == null) {
            setAttr("exampleShareUrl", RequestUtil.getBaseUrl() + example.getUrl());
        }else {
            setAttr("exampleShareUrl", RequestUtil.getBaseUrl() + example.getUrl() + "?did=" + getLoginedUser().getId());
        }

        //设置页面的seo信息
        setSeoInfos(example);


        //设置菜单高亮
        doFlagMenuActive(example);

        //记录当前浏览量
        exampleService.doIncProductViewCount(example.getId());

        User exampleAuthor = example.getUserId() != null
                ? userService.findById(example.getUserId())
                : null;

        example.put("user", exampleAuthor);
        setAttr("example", example);

        List<ExampleImage> exampleImages = imageService.findListByProductId(example.getId());
        setAttr("exampleImages", exampleImages);

        String distUserId = getPara("did");
        if (StrUtil.isNotBlank(distUserId)) {
            CookieUtil.put(this, buildDistUserCookieName(example.getId()), distUserId);
        }

        render(example.getHtmlView());
    }

    private String buildDistUserCookieName(long exampleId) {
        return "did-" + exampleId;
    }

    private void setSeoInfos(Example example) {
        setSeoTitle(example.getTitle());
        setSeoKeywords(example.getMetaKeywords());
        setSeoDescription(StrUtil.isBlank(example.getMetaDescription())
                ? CommonsUtils.maxLength(example.getText(), 100)
                : example.getMetaDescription());
    }


    private Example getProduct() {
        String idOrSlug = getPara(0);
        return StrUtil.isNumeric(idOrSlug)
                ? exampleService.findById(idOrSlug)
                : exampleService.findFirstBySlug(StrUtil.urlDecode(idOrSlug));
    }


    private void doFlagMenuActive(Example example) {

        setMenuActive(menu -> menu.isUrlStartWidth(example.getUrl()));

        List<ExampleCategory> exampleCategories = categoryService.findCategoryListByProductId(example.getId());
        if (exampleCategories == null || exampleCategories.isEmpty()) {
            return;
        }

        setMenuActive(menu -> {
            if ("example_category".equals(menu.getRelativeTable())) {
                for (ExampleCategory category : exampleCategories) {
                    if (category.getId().equals(menu.getRelativeId())) {
                        return true;
                    }
                }
            }
            return false;
        });

    }


    /**
     * 发布评论
     */
    public void postComment() {

        Long exampleId = getParaToLong("exampleId");
        Long pid = getParaToLong("pid");
        String nickname = getPara("nickname");
        String content = getPara("content");

        if (exampleId == null || exampleId <= 0) {
            renderFailJson();
            return;
        }

        if (StrUtil.isBlank(content)) {
            renderJson(Ret.fail().set("message", "评论内容不能为空"));
            return;
        } else {
            content = StrUtil.escapeHtml(content);
        }

        //是否对用户输入验证码进行验证
        Boolean vCodeEnable = JPressOptions.isTrueOrEmpty("example_comment_vcode_enable");
        if (vCodeEnable != null && vCodeEnable == true) {
            if (validateCaptcha("captcha") == false) {
                renderJson(Ret.fail().set("message", "验证码错误").set("errorCode", 2));
                return;
            }
        }

        if (DFAUtil.isContainsSensitiveWords(content)) {
            renderJson(Ret.fail().set("message", "非法内容，无法发布评论信息"));
            return;
        }


        Example example = exampleService.findById(exampleId);
        if (example == null) {
            renderFailJson();
            return;
        }

        // 关闭了评论的功能
        if (!example.isCommentEnable()) {
            renderJson(Ret.fail().set("message", "该产品的评论功能已关闭"));
            return;
        }

        //是否开启评论功能
        Boolean commentEnable = JPressOptions.isTrueOrEmpty("example_comment_enable");
        if (commentEnable == null || commentEnable == false) {
            renderJson(Ret.fail().set("message", "评论功能已关闭"));
            return;
        }


        //是否允许未登录用户参与评论
        Boolean unLoginEnable = optionService.findAsBoolByKey("example_comment_unlogin_enable");
        if (unLoginEnable == null || unLoginEnable == false) {
            if (getLoginedUser() == null) {
                renderJson(Ret.fail().set("message", "未登录用户不能评论").set("errorCode", 9));
                return;
            }
        }

        ExampleComment comment = new ExampleComment();

        comment.setExampleId(exampleId);
        comment.setContent(content);
        comment.setAuthor(nickname);
        comment.setPid(pid);

        User user = getLoginedUser();
        if (user != null) {
            comment.setUserId(user.getId());
            comment.setAuthor(user.getNickname());
        }

        //是否是管理员必须审核
        Boolean reviewEnable = optionService.findAsBoolByKey("example_comment_review_enable");
        if (reviewEnable != null && reviewEnable == true) {
            comment.setStatus(ExampleComment.STATUS_UNAUDITED);
        }
        /**
         * 无需管理员审核、直接发布
         */
        else {
            comment.setStatus(ExampleComment.STATUS_NORMAL);
        }

        //记录产品的评论量
        exampleService.doIncProductCommentCount(exampleId);

        commentService.saveOrUpdate(comment);

        if (pid != null) {
            //记录评论的回复数量
            commentService.doIncCommentReplyCount(pid);

            ExampleComment parent = commentService.findById(pid);
            if (parent != null && parent.isNormal()) {
                comment.put("parent", parent);
            }
        }

        Ret ret = Ret.ok().set("code", 0);

        Map<String, Object> paras = new HashMap<>();
        paras.put("comment", comment);
        paras.put("example", example);

        if (user != null) {
            paras.put("user", user.keepSafe());
            comment.put("user", user.keepSafe());
        }


        renderHtmltoRet("/WEB-INF/views/commons/example/defaultExampleCommentItem.html", paras, ret);
        renderJson(ret);

        ExampleNotifyKit.doNotifyAdministrator(example, comment, user);

    }



    @Before(ExampleValidate.class)
    public void doAddFavorite() {
        Example example = ExampleValidate.getThreadLocalProduct();
        User user = getLoginedUser();
        if (favoriteService.doAddToFavorite(example.toFavorite(user.getId()))) {
            renderOkJson();
        } else {
            renderFailJson("已经收藏过了!");
        }
    }


}
