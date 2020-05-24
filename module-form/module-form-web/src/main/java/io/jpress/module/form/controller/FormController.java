package io.jpress.module.form.controller;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;
import io.jboot.utils.StrUtil;
import io.jboot.web.controller.annotation.RequestMapping;
import io.jpress.JPressConsts;
import io.jpress.JPressOptions;
import io.jpress.module.form.model.FormInfo;
import io.jpress.module.form.service.FormInfoService;
import io.jpress.web.base.TemplateControllerBase;

import java.util.Date;

@RequestMapping(value = "/form")
public class FormController extends TemplateControllerBase {

    @Inject
    private FormInfoService formInfoService;

    public void index() {
        String formView = StrUtil.isBlank(JPressOptions.getIndexStyle())
                ? "ask_form.html"
                : "ask_form_" + JPressOptions.getIndexStyle() + ".html";
        //渲染 模板下的 form.html
//        '/WEB-INF/views/commons/page/defaultPageCommentItem.html'
        render(formView);
    }

    /**
     * 新增用户
     */
    public void doAdd() {

        String name = getPara("name");
        String mobile = getPara("mobile");
        int area;
        try {
            area = getParaToInt("area",0);
        }catch (Exception e){
            area = 0;
        }
        int roomType = getParaToInt("roomType",0);
        String address = getPara("address");
        String desc = getPara("desc");
        FormInfo form = getBean(FormInfo.class);

        if (StrUtil.isBlank(name)) {
            renderJson(Ret.fail().set("message", "姓名不能为空"));
            return;
        }

        if (StrUtil.isBlank(mobile) || mobile.length()!=11) {
            renderJson(Ret.fail().set("message", "请填写正确的手机号"));
            return;
        }

        if (StrUtil.isBlank(address)) {
            renderJson(Ret.fail().set("message", "请选择地址"));
            return;
        }

        String[] addressArray = address.split("/");

        if(addressArray.length<2){
            renderJson(Ret.fail().set("message", "请选择城市"));
            return;
        }

        if(addressArray.length<3){
            renderJson(Ret.fail().set("message", "请选择县/区"));
            return;
        }

        if (area <= 0) {
            renderJson(Ret.fail().set("message", "面积请填写数字"));
            return;
        }

        if (roomType <= 0) {
            renderJson(Ret.fail().set("message", "请选择你的房间类型"));
            return;
        }

        //是否对用户输入验证码进行验证
        Boolean vCodeEnable = JPressOptions.isTrueOrEmpty("page_comment_vcode_enable");
        if (vCodeEnable != null && vCodeEnable == true) {
            if (validateCaptcha("captcha") == false) {
                renderJson(Ret.fail().set("message", "验证码错误").set("errorCode", 2));
                return;
            }
        }

        form.setName(name);
        form.setMobile(mobile);
        form.setArea(area);
        form.setHousetype(roomType);
        form.setAddress(address);
        form.setDesc(desc);
        form.setTime(new Date());
        formInfoService.save(form);
        renderOkJson();
    }



}
