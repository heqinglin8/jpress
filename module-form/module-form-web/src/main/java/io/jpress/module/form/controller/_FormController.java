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
        String keyword = getPara("keyword");
        String columnType = getPara("columnType",FormInfo.COLUMN_TYPE_NAME);

        Page<FormInfo> page =
                StrUtil.isBlank(status)
                        ? fis._paginateWithoutStatus(getPagePara(), 10,columnType, keyword)
                        : fis._paginateByStatus(getPagePara(), 10,columnType, keyword, status);

        setAttr("page", page);

        long normalCount = fis.findCountByStatus(FormInfo.STATUS_NORMAL);
        long processedCount = fis.findCountByStatus(FormInfo.STATUS_PROCESSED);
        long trashCount = fis.findCountByStatus(FormInfo.STATUS_TRASH);

        setAttr("processedCount", processedCount);
        setAttr("normalCount", normalCount);
        setAttr("trashCount", trashCount);
        setAttr("totalCount", processedCount + trashCount + normalCount);

        render("form/form_list.html");
    }

    @EmptyValidate(@Form(name = "ids"))
    public void doDelByIds() {
        Set<String> idsSet = getParaSet("ids");
        render(fis.batchDeleteByIds(idsSet.toArray()) ? OK : FAIL);
    }

    public void doTrash() {
        Long id = getIdPara();
        FormInfo model = new FormInfo();
        model.setId(id);
        model.setStatus(2);
        render(fis.update(model) ? OK : FAIL);
    }

    public void doProcessed() {
        Long id = getIdPara();
        FormInfo model = new FormInfo();
        model.setId(id);
        model.setStatus(1);
        render(fis.update(model) ? OK : FAIL);
    }

    public void doNormal() {
        Long id = getIdPara();
        FormInfo model = new FormInfo();
        model.setId(id);
        model.setStatus(0);
        render(fis.update(model) ? OK : FAIL);
    }

}
