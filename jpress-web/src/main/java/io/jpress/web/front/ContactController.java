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
package io.jpress.web.front;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;
import io.jboot.utils.StrUtil;
import io.jboot.web.controller.annotation.RequestMapping;
import io.jpress.commons.pay.ContactStatus;
import io.jpress.model.Contact;
import io.jpress.service.ContactService;
import io.jpress.web.base.TemplateControllerBase;

import java.util.Date;


/**
 * @author Michael Yang 杨福海 （fuhai999@gmail.com）
 * @version V1.0
 */
@RequestMapping("/contact")
public class ContactController extends TemplateControllerBase {

    @Inject
    private ContactService contactService;



    /**
     * 提交联系方式
     */
    public void post() {
        String name = getPara("name");
        String phone = getPara("phone");
        String city = getPara("city");
        String area = getPara("area");
        String service = getPara("service");

        if (validateCaptcha("captcha") == false) {
            renderJson(Ret.fail().set("message", "验证码错误,请重新输入").set("errorCode", 2));
            return;
        }

        if (StrUtil.isBlank(name)) {
            renderJson(Ret.fail().set("message", "用户名不能为空"));
            return;
        }

        if (StrUtil.isBlank(phone)) {
            renderJson(Ret.fail().set("message", "电话不能为空"));
            return;
        }

        if (!StrUtil.isMobileNumber(phone)) {
            renderJson(Ret.fail().set("message", "你输入的手机号码不正确"));
            return;
        }

        if (StrUtil.isBlank(city)) {
            renderJson(Ret.fail().set("message", "城市不能为空"));
            return;
        }

        if (StrUtil.isBlank(area)) {
            renderJson(Ret.fail().set("message", "区域不能为空"));
            return;
        }


        Contact contact = new Contact();

        contact.setName(name);
        contact.setMobile(phone);
        contact.setCity(city);
        contact.setArea(area);
        contact.setService(service);
        contact.setStatus(ContactStatus.UNDEAL.getStatus());
        contact.setCreated(new Date());


        contactService.saveOrUpdate(contact);

        renderOkJson();

    }

}
