package io.jpress.addon.submitform;

import com.jfinal.core.ActionKey;
import com.jfinal.kit.Ret;
import io.jboot.web.controller.JbootController;
import io.jboot.web.controller.annotation.RequestMapping;
import io.jpress.JPressConsts;
import io.jpress.core.menu.annotation.AdminMenu;


@RequestMapping(value = "/submitform",viewPath = "/")
public class SubmitFormAddonController extends JbootController {

    public void index() {
        setAttr("version","1.0.2");
        render("submitform/index.html");
    }

    public void json() {
        renderJson(Ret.ok().set("message", "json ok...."));
    }

    @ActionKey("/admin/addon/test")
    @AdminMenu(groupId = JPressConsts.SYSTEM_MENU_ADDON, text = "插件测试")
    public void adminmenutest() {
        renderText("addon test abc");
    }
}
