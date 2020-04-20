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
package io.jpress.module.form.controller;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;
import io.jboot.utils.StrUtil;
import io.jboot.web.controller.annotation.RequestMapping;
import io.jboot.web.validate.EmptyValidate;
import io.jboot.web.validate.Form;
import io.jpress.JPressConsts;
import io.jpress.core.menu.annotation.AdminMenu;
import io.jpress.module.form.model.FormInfo;
import io.jpress.module.form.service.FormInfoService;
import io.jpress.web.base.AdminControllerBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Michael Yang 杨福海 （fuhai999@gmail.com）
 * @version V1.0
 * @Package io.jpress.module.page.controller.admin
 */
@RequestMapping(value = "/admin/form", viewPath = JPressConsts.DEFAULT_ADMIN_VIEW)
public class _FormController extends AdminControllerBase {

    @Inject
    private FormInfoService fis;

    @AdminMenu(text = "表单管理", groupId = "form", order = 1)
    public void index() {

        String status = getPara("status");
        String name = getPara("name");

        Page<FormInfo> page =
                StrUtil.isBlank(status)
                        ? fis._paginateWithoutStatus(getPagePara(), 10, name)
                        : fis._paginateByStatus(getPagePara(), 10, name, status);

        setAttr("page", page);

//        int draftCount = sps.findCountByStatus(SinglePage.STATUS_DRAFT);
//        int trashCount = sps.findCountByStatus(SinglePage.STATUS_TRASH);
//        int normalCount = sps.findCountByStatus(SinglePage.STATUS_NORMAL);
//
//        setAttr("draftCount", draftCount);
//        setAttr("trashCount", trashCount);
//        setAttr("normalCount", normalCount);
//        setAttr("totalCount", draftCount + trashCount + normalCount);

        render("form/form_list.html");
    }

    @EmptyValidate(@Form(name = "ids"))
    public void doDelByIds() {
        Set<String> idsSet = getParaSet("ids");
        render(fis.batchDeleteByIds(idsSet.toArray()) ? OK : FAIL);
    }

}
