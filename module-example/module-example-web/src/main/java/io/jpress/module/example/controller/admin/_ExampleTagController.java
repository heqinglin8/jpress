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
import io.jpress.core.template.Template;
import io.jpress.core.template.TemplateManager;
import io.jpress.model.Menu;
import io.jpress.module.example.model.ExampleCategory;
import io.jpress.module.example.service.ExampleCategoryService;
import io.jpress.service.MenuService;
import io.jpress.web.base.AdminControllerBase;

import java.util.List;


@RequestMapping(value = "/admin/example/tag", viewPath = JPressConsts.DEFAULT_ADMIN_VIEW)
public class _ExampleTagController extends AdminControllerBase {

    @Inject
    private ExampleCategoryService exampleCategoryService;

    @Inject
    private MenuService menuService;

    @AdminMenu(text = "标签", groupId = "example", order = 3)
    public void index() {
        Page<ExampleCategory> page = exampleCategoryService.paginateByType(getPagePara(), 10, ExampleCategory.TYPE_TAG);
        setAttr("page", page);

        int id = getParaToInt(0, 0);
        if (id > 0) {
            setAttr("category", exampleCategoryService.findById(id));
            setAttr("isDisplayInMenu", menuService.findFirstByRelatives("article_category", id) != null);
        }

        initStylesAttr("prolist_");
        render("example/example_tag_list.html");
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


    @EmptyValidate({@Form(name = "category.title", message = "标签名称不能为空")})
    public void doSave() {

        ExampleCategory tag = getModel(ExampleCategory.class, "category");

        String slug = tag.getTitle().contains(".")
                ? tag.getTitle().replace(".", "_")
                : tag.getTitle();

        //新增 tag
        if (tag.getId() == null) {
            ExampleCategory indbTag = exampleCategoryService.findFirstByTypeAndSlug(ExampleCategory.TYPE_TAG, slug);
            if (indbTag != null) {
                renderJson(Ret.fail().set("message", "该标签已经存在，不能新增。"));
                return;
            }
        }

        tag.setSlug(slug);
        saveCategory(tag);
    }


    private void saveCategory(ExampleCategory category) {
        if (!validateSlug(category)) {
            renderJson(Ret.fail("message", "slug不能全是数字且不能包含字符：- "));
            return;
        }

        Object id = exampleCategoryService.saveOrUpdate(category);
//        exampleCategoryService.updateCount(category.getId());

        Menu displayMenu = menuService.findFirstByRelatives("example_category", id);
        Boolean isDisplayInMenu = getParaToBoolean("displayInMenu");
        if (isDisplayInMenu != null && isDisplayInMenu) {
            if (displayMenu == null) {
                displayMenu = new Menu();
            }

            displayMenu.setUrl(category.getUrl());
            displayMenu.setText(category.getTitle());
            displayMenu.setType(Menu.TYPE_MAIN);
            displayMenu.setOrderNumber(category.getOrderNumber());
            displayMenu.setRelativeTable("example_category");
            displayMenu.setRelativeId((Long) id);

            if (displayMenu.getPid() == null) {
                displayMenu.setPid(0L);
            }

            if (displayMenu.getOrderNumber() == null) {
                displayMenu.setOrderNumber(99);
            }

            menuService.saveOrUpdate(displayMenu);

        } else if (displayMenu != null) {
            menuService.delete(displayMenu);
        }

        renderOkJson();
    }


    public void doDel() {
        Long id = getIdPara();
        render(exampleCategoryService.deleteById(id) ? Ret.ok() : Ret.fail());
    }
}