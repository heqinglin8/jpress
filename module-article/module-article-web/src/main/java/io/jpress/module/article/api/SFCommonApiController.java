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

import com.jfinal.aop.Aop;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Page;
import io.jboot.db.model.Columns;
import io.jboot.utils.StrUtil;
import io.jboot.web.controller.annotation.RequestMapping;
import io.jboot.web.cors.EnableCORS;
import io.jpress.JPressOptions;
import io.jpress.commons.dfa.DFAUtil;
import io.jpress.commons.layer.SortKit;
import io.jpress.commons.oauth2.OauthConnector;
import io.jpress.model.Menu;
import io.jpress.model.User;
import io.jpress.module.article.kit.ArticleNotifyKit;
import io.jpress.module.article.model.Article;
import io.jpress.module.article.model.ArticleCategory;
import io.jpress.module.article.model.ArticleComment;
import io.jpress.module.article.service.ArticleCategoryService;
import io.jpress.module.article.service.ArticleCommentService;
import io.jpress.module.article.service.ArticleService;
import io.jpress.service.MenuService;
import io.jpress.service.OptionService;
import io.jpress.service.UserService;
import io.jpress.web.base.SFApiControllerBase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
//        SortKit.toTree(secondaryMenus);
        renderJson(Ret.ok("tabs", navList));
    }


}
