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
package io.jpress.module.article.api;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Inject;
import com.jfinal.json.FastJson;
import com.jfinal.kit.Ret;
import com.jfinal.log.Log;
import io.jboot.web.controller.annotation.RequestMapping;
import io.jboot.web.cors.EnableCORS;
import io.jpress.commons.bean.RenderList;
import io.jpress.model.Menu;
import io.jpress.module.article.service.ArticleCategoryService;
import io.jpress.module.article.service.ArticleCommentService;
import io.jpress.module.article.service.ArticleService;
import io.jpress.service.MenuService;
import io.jpress.service.OptionService;
import io.jpress.service.UserService;
import io.jpress.web.base.SFApiControllerBase;
import io.jpress.web.functions.JPressCoreFunctions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Michael Yang 杨福海 （fuhai999@gmail.com）
 * @version V1.0
 * @Title: 文章前台页面Controller
 * @Package io.jpress.module.article.admin
 */
@RequestMapping("/sf/api/common")
public class SFCommonApiController extends SFApiControllerBase {
    private static final Log LOGGER = Log.getLog(SFCommonApiController.class);

    @Inject
    private ArticleService articleService;

    @Inject
    private ArticleCategoryService categoryService;

    @Inject
    private OptionService optionService;

    @Inject
    private ArticleCommentService commentService;

    @Inject
    private UserService userService;

    /**
     * 获取分类详情的API
     * <p>
     * 可以通过 id 获取文章分类，也可以通过 type + slug 定位到分类
     * 分类可能是后台对应的分类，有可以是一个tag（tag也是一种分类）
     * <p>
     * 例如：
     * http://127.0.0.1:8080/sf/api/common/config
     */
    @EnableCORS
    public void config() {
        MenuService menuService = Aop.get(MenuService.class);
        List<Menu> secondaryMenus = menuService.findListByType(Menu.TYPE_SECOND);
        List<NavItem> navList = new ArrayList<>();
        for(int i=0;i<secondaryMenus.size();i++){
            Menu m = secondaryMenus.get(i);
            NavItem ni = new NavItem();
            ni.setText(m.getText());
            ni.setType(m.getType());
            ni.setUrl(m.getUrl());
            ni.setCategoryId(m.getRelativeId());
            navList.add(ni);
        }
        LOGGER.info("navList:"+navList);
        LOGGER.info("secondaryMenus:"+secondaryMenus);
        renderJson(Ret.ok("tabs", navList));
    }

    /**
     * 获取首页信息
     * http://127.0.0.1:8080/sf/api/common/home
     */
    @EnableCORS
    public void home() {
        List<RenderList> slides = JPressCoreFunctions.linesOption("calmlog_slides","\\|");
        renderJson(Ret.ok("slides", slides).set("funs",getFunList()));
    }


    private List<Map<String,Object>> getFunList(){

        List<Map<String,Object>> funs = new ArrayList<>();
        HashMap<String,Object> jb = new HashMap<>();
        jb.put("icon","https://mued3.jia.com/image/mobile/m_sygb/first_page/icon_list_znbj.png");
        jb.put("text","智能报价");
        jb.put("type",1);
        funs.add(jb);

        HashMap<String,Object> jb2 = new HashMap<>();
        jb2.put("icon","https://mued3.jia.com/image/mobile/m_sygb/first_page/icon_list_mfsj.png");
        jb2.put("text","免费设计");
        jb2.put("type",2);
        funs.add(jb2);

        HashMap<String,Object> jb3 = new HashMap<>();
        jb3.put("icon","https://mued3.jia.com/image/mobile/m_sygb/first_page/icon_list_vrqj.png");
        jb3.put("text","全景家装");
        jb3.put("type",3);
        funs.add(jb3);

        HashMap<String,Object> jb4 = new HashMap<>();
        jb4.put("icon","https://mued3.jia.com/image/mobile/m_sygb/first_page/icon_list_kbwp.png");
        jb4.put("text","空调安装");
        jb4.put("type",4);
        funs.add(jb4);

        HashMap<String,Object> jb5 = new HashMap<>();
        jb5.put("icon","https://mued3.jia.com/image/mobile/m_sygb/first_page/icon_list_zxal.png");
        jb5.put("text","空调维修");
        jb5.put("type",5);
        funs.add(jb5);

        HashMap<String,Object> jb6 = new HashMap<>();
        jb6.put("icon","https://mued3.jia.com/image/mobile/m_sygb/first_page/icon_list_znbj.png");
        jb6.put("text","工程案例");
        jb6.put("type",6);
        funs.add(jb6);

        HashMap<String,Object> jb7 = new HashMap<>();
        jb7.put("icon","https://mued3.jia.com/image/mobile/m_sygb/first_page/icon_list_lt.png");
        jb7.put("text","智家论坛");
        jb7.put("type",7);
        funs.add(jb7);

        HashMap<String,Object> jb8 = new HashMap<>();
        jb8.put("icon","https://mued3.jia.com/image/mobile/wxStore/ppg-icon.png");
        jb8.put("text","空调知识");
        jb8.put("type",8);
        funs.add(jb8);

        return funs;
    }


}
