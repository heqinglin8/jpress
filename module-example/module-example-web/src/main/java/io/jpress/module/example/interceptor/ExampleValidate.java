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
package io.jpress.module.example.interceptor;

import com.jfinal.aop.Inject;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.core.JFinal;
import com.jfinal.kit.Ret;
import io.jboot.utils.RequestUtil;
import io.jboot.web.controller.JbootControllerContext;
import io.jpress.model.User;
import io.jpress.module.example.model.Example;
import io.jpress.module.example.service.ExampleService;
import io.jpress.web.interceptor.UserInterceptor;

/**
 * @author michael yang (fuhai999@gmail.com)
 * @Date: 2019/12/5
 */
public class ExampleValidate implements Interceptor {

    @Inject
    private ExampleService exampleService;
    private static final String ATTR_EXAMPLE = "example";

    public static Example getThreadLocalProduct() {
        return JbootControllerContext.get().getAttr(ATTR_EXAMPLE);
    }

    @Override
    public void intercept(Invocation inv) {

        Controller c = inv.getController();

        Long productId = inv.getController().getLong("id");
        Example example = exampleService.findById(productId);

        if (example == null || !example.isNormal()) {
            if (RequestUtil.isAjaxRequest(c.getRequest())) {
                c.renderJson(Ret.fail().set("code", "2").set("message", "案例不存在或已下架。"));
            } else {
                c.renderError(404);
            }
            return;
        }


        User user = UserInterceptor.getThreadLocalUser();
        if (user == null) {
            if (RequestUtil.isAjaxRequest(c.getRequest())) {
                c.renderJson(Ret.fail()
                        .set("code", 1)
                        .set("message", "用户未登录")
                        .set("gotoUrl", JFinal.me().getContextPath() + "/user/login?gotoUrl=" + example.getUrl()));
            } else {
                c.redirect("/user/login?gotoUrl=" + example.getUrl());
            }
            return;
        }

        c.setAttr(ATTR_EXAMPLE,example);
        inv.invoke();
    }
}
