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
package io.jpress.module.example.directive;

import com.jfinal.aop.Inject;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import io.jboot.web.directive.annotation.JFinalDirective;
import io.jboot.web.directive.base.JbootDirectiveBase;
import io.jpress.module.example.model.Example;
import io.jpress.module.example.service.ExampleCategoryService;
import io.jpress.module.example.service.ExampleService;

import java.util.List;

/**
 * @author Michael Yang 杨福海 （fuhai999@gmail.com）
 * @version V1.0
 * @Title: 相关产品
 */
@JFinalDirective("relevantExamples")
public class RelevantExampleDirective extends JbootDirectiveBase {

    @Inject
    private ExampleService service;

    @Inject
    private ExampleCategoryService categoryService;

    @Override
    public void onRender(Env env, Scope scope, Writer writer) {
        Example product = getPara(0, scope);
        if (product == null) {
            throw new IllegalArgumentException("#relevantProducts(...) argument must not be null or empty." + getLocation());
        }

        //默认值 3
        int count = getParaToInt(1, scope, 3);

        List<Example> relevantProducts = service.findRelevantListByProductId(product.getId(), Example.STATUS_NORMAL, count);

        if (relevantProducts == null || relevantProducts.isEmpty()) {
            return;
        }

        scope.setLocal("relevantExamples", relevantProducts);
        renderBody(env, scope, writer);
    }

    @Override
    public boolean hasEnd() {
        return true;
    }
}
