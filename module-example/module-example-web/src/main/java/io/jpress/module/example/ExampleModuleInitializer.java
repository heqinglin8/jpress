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
package io.jpress.module.example;

import com.jfinal.core.Controller;
import com.jfinal.template.Engine;
import io.jboot.core.listener.JbootAppListenerBase;
import io.jpress.core.menu.MenuGroup;
import io.jpress.core.module.ModuleListener;

import java.util.List;

/**
 * @author Michael Yang 杨福海 （fuhai999@gmail.com）
 * @version V1.0
 * @Title: 案例模块初始化
 * @Package io.jpress
 */
public class ExampleModuleInitializer extends JbootAppListenerBase implements ModuleListener {


    @Override
    public String onRenderDashboardBox(Controller controller) {
        return null;
    }

    @Override
    public String onRenderToolsBox(Controller controller) {
        return null;
    }

    @Override
    public void onConfigAdminMenu(List<MenuGroup> adminMenus) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId("example");
        menuGroup.setText("案例");
        menuGroup.setIcon("<i class=\"fas fa-radiation\"></i>");
        menuGroup.setOrder(100);
        adminMenus.add(menuGroup);
    }

    @Override
    public void onConfigUcenterMenu(List<MenuGroup> ucenterMenus) {


    }


    @Override
    public void onEngineConfig(Engine engine) {
        engine.addSharedFunction("/WEB-INF/views/commons/example/defaultExampleCommentPage.html");
        engine.addSharedFunction("/WEB-INF/views/commons/example/defaultExampleHeader.html");
    }
}
