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
import io.jboot.utils.StrUtil;
import io.jboot.web.controller.annotation.RequestMapping;
import io.jboot.web.validate.EmptyValidate;
import io.jboot.web.validate.Form;
import io.jpress.JPressConsts;
import io.jpress.commons.layer.SortKit;
import io.jpress.core.menu.annotation.AdminMenu;
import io.jpress.core.template.Template;
import io.jpress.core.template.TemplateManager;
import io.jpress.model.MemberGroup;
import io.jpress.module.example.ExampleFields;
import io.jpress.module.example.model.Example;
import io.jpress.module.example.model.ExampleCategory;
import io.jpress.module.example.service.ExampleCategoryService;
import io.jpress.module.example.service.ExampleImageService;
import io.jpress.module.example.service.ExampleService;
import io.jpress.service.MemberGroupService;
import io.jpress.service.MemberPriceService;
import io.jpress.web.base.AdminControllerBase;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;
import java.util.Set;


@RequestMapping(value = "/admin/example", viewPath = JPressConsts.DEFAULT_ADMIN_VIEW)
public class _ExampleController extends AdminControllerBase {

    @Inject
    private ExampleService productService;

    @Inject
    private ExampleCategoryService categoryService;

    @Inject
    private ExampleImageService imageService;


    @AdminMenu(text = "案例列表", groupId = "example", order = 1)
    public void list() {
        Integer status = getParaToInt("status");
        String title = getPara("title");
        Long categoryId = getParaToLong("categoryId");

        Page<Example> page = status == null
                ? productService._paginateWithoutTrash(getPagePara(), 10, title, categoryId)
                : productService._paginateByStatus(getPagePara(), 10, title, categoryId, status);

        setAttr("page", page);


        List<ExampleCategory> categories = categoryService.findListByType(ExampleCategory.TYPE_CATEGORY);
        SortKit.toLayer(categories);
        setAttr("categories", categories);
        flagCheck(categories, categoryId);


        long draftCount = productService.findCountByStatus(Example.STATUS_DRAFT);
        long trashCount = productService.findCountByStatus(Example.STATUS_TRASH);
        long normalCount = productService.findCountByStatus(Example.STATUS_NORMAL);

        setAttr("draftCount", draftCount);
        setAttr("trashCount", trashCount);
        setAttr("normalCount", normalCount);
        setAttr("totalCount", draftCount + trashCount + normalCount);


        render("example/example_list.html");
    }


    public void edit() {
        List<ExampleCategory> categories = categoryService.findListByType(ExampleCategory.TYPE_CATEGORY);
        SortKit.toLayer(categories);
        setAttr("categories", categories);

        setAttr("fields", ExampleFields.me());


        Long exampleId = getParaToLong(0, 0L);

        Example example = null;
        if (exampleId != null && exampleId > 0) {
            example = productService.findById(exampleId);
            if (example == null) {
                renderError(404);
                return;
            }
            setAttr("example", example);

            List<ExampleCategory> tags = categoryService.findTagListByProductId(exampleId);
            setAttr("tags", tags);

            Long[] categoryIds = categoryService.findCategoryIdsByProductId(exampleId);
            flagCheck(categories, categoryIds);

            setAttr("images", imageService.findListByProductId(exampleId));
        }
        initStylesAttr("example_");
        render("example/example_edit.html");
    }


    private void flagCheck(List<ExampleCategory> categories, Long... checkIds) {
        if (checkIds == null || checkIds.length == 0
                || categories == null || categories.size() == 0) {
            return;
        }

        for (ExampleCategory category : categories) {
            for (Long id : checkIds) {
                if (id != null && id.equals(category.getId())) {
                    category.put("isCheck", true);
                }
            }
        }
    }


    private void initStylesAttr(String prefix) {
        Template template = TemplateManager.me().getCurrentTemplate();
        if (template == null) {
            return;
        }
        setAttr("flags", template.getFlags());
        List<String> styles = template.getSupportStyles(prefix);
        setAttr("styles", styles);
    }

    @EmptyValidate({
            @Form(name = "example.title", message = "案例标题不能为空"),
    })
    public void doSave() {
        Example example = getModel(Example.class, "example");

        if (!validateSlug(example)) {
            renderJson(Ret.fail("message", "slug不能全是数字且不能包含字符：- "));
            return;
        }


        if (StrUtil.isNotBlank(example.getSlug())) {
            Example existModel = productService.findFirstBySlug(example.getSlug());
            if (existModel != null && existModel.getId().equals(example.getId()) == false) {
                renderJson(Ret.fail("message", "该slug已经存在"));
                return;
            }
        }

        if (example.getOrderNumber() == null) {
            example.setOrderNumber(0);
        }

        if (example.getViewCount() == null){
            example.setViewCount(0L);
        }
        long id = (long) productService.saveOrUpdate(example);
        productService.doUpdateCommentCount(id);

        setAttr("exampleId", id);
        setAttr("example", example);


        Long[] saveBeforeCategoryIds = null;
        if (example.getId() != null){
            saveBeforeCategoryIds = categoryService.findCategoryIdsByProductId(example.getId());
        }


        Long[] categoryIds = getParaValuesToLong("category");
        Long[] tagIds = getTagIds(getParaValues("tag"));

        Long[] updateCategoryIds = ArrayUtils.addAll(categoryIds, tagIds);

        productService.doUpdateCategorys(id, updateCategoryIds);

        if (updateCategoryIds != null && updateCategoryIds.length > 0) {
            for (Long categoryId : updateCategoryIds) {
                categoryService.doUpdateProductCount(categoryId);
            }
        }

        if (saveBeforeCategoryIds != null && saveBeforeCategoryIds.length > 0) {
            for (Long categoryId : saveBeforeCategoryIds) {
                categoryService.doUpdateProductCount(categoryId);
            }
        }

        String[] imageIds = getParaValues("imageIds");
        String[] imageSrcs = getParaValues("imageSrcs");
        imageService.saveOrUpdateByProductId(example.getId(), imageIds, imageSrcs);


        Ret ret = id > 0 ? Ret.ok().set("id", id) : Ret.fail();
        renderJson(ret);
    }


    private Long[] getTagIds(String[] tags) {
        if (tags == null || tags.length == 0) {
            return null;
        }

        List<ExampleCategory> categories = categoryService.findOrCreateByTagString(tags);
        long[] ids = categories.stream().mapToLong(value -> value.getId()).toArray();
        return ArrayUtils.toObject(ids);
    }


    public void doDel() {
        Long id = getIdPara();
        if (productService.deleteById(id)){
            imageService.deleteByProductId(id);
        }

        renderOkJson();
    }

    public void doTrash() {
        Long id = getIdPara();
        render(productService.doChangeStatus(id, Example.STATUS_TRASH) ? OK : FAIL);
    }

    public void doDraft() {
        Long id = getIdPara();
        render(productService.doChangeStatus(id, Example.STATUS_DRAFT) ? OK : FAIL);
    }

    public void doNormal() {
        Long id = getIdPara();
        render(productService.doChangeStatus(id, Example.STATUS_NORMAL) ? OK : FAIL);
    }

    @EmptyValidate(@Form(name = "ids"))
    public void doDelByIds() {
        Set<String> idsSet = getParaSet("ids");
        if (productService.batchDeleteByIds(idsSet.toArray())){
            for (String id : idsSet){
                imageService.deleteByProductId(Long.valueOf(id));
            }
        }
        renderOkJson();
    }

    @AdminMenu(text = "设置", groupId = "example", order = 99)
    public void setting() {
        render("example/setting.html");
    }
}
