package io.jpress.module.form.controller;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;
import io.jboot.utils.StrUtil;
import io.jboot.web.controller.annotation.RequestMapping;
import io.jpress.JPressOptions;
import io.jpress.module.form.model.FormInfo;
import io.jpress.module.form.service.FormInfoService;
import io.jpress.web.base.TemplateControllerBase;


@RequestMapping("/form")
public class FormController extends TemplateControllerBase {

    @Inject
    private FormInfoService formInfoService;

    public void index() {
        String formView = StrUtil.isBlank(JPressOptions.getIndexStyle())
                ? "form.html"
                : "index_" + JPressOptions.getIndexStyle() + ".html";
        //渲染 模板下的 form.html
        render(formView);
    }

    /**
     * 新增用户
     */
    public void doAdd() {

        String name = getPara("name");
        String mobile = getPara("mobile");

        int area = getParaToInt("area",0);
        int roomType = getParaToInt("roomType",0);
        String province = getPara("province");
        String city = getPara("city");
        String district = getPara("district");
        String address = getPara("address");
        String desc = getPara("desc");
        FormInfo form = getBean(FormInfo.class);
        if (StrUtil.isBlank(name)) {
            renderJson(Ret.fail().set("message", "姓名不能为空").set("errorCode", 1));
            return;
        }

        if (StrUtil.isBlank(mobile)) {
            renderJson(Ret.fail().set("message", "联系方式不能为空").set("errorCode", 2));
            return;
        }

        if (StrUtil.isBlank(address)) {
            renderJson(Ret.fail().set("message", "地址不能为空").set("errorCode", 3));
            return;
        }

        if (area <= 0) {
            renderJson(Ret.fail().set("message", "请填写面积").set("errorCode", 4));
            return;
        }

        if (roomType <= 0) {
            renderJson(Ret.fail().set("message", "请选择你的房间类型").set("errorCode", 5));
            return;
        }

        form.setName(name);
        form.setMobile(mobile);
        form.setArea(area);

        form.setHousetype(roomType);
        form.setProvince(province);
        form.setCity(city);
        form.setDistrict(district);
        form.setAddress(address);
        form.setDesc(desc);

        formInfoService.save(form);

        renderOkJson();
    }

    public void showResult() {
        renderJson("表单保存成功");
    }

}
