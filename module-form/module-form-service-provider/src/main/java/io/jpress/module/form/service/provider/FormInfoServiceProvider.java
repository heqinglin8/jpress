package io.jpress.module.form.service.provider;

import com.jfinal.plugin.activerecord.Page;
import io.jboot.aop.annotation.Bean;
import io.jboot.db.model.Columns;
import io.jboot.service.JbootServiceBase;
import io.jboot.utils.StrUtil;
import io.jpress.module.form.model.FormInfo;
import io.jpress.module.form.service.FormInfoService;

@Bean
public class FormInfoServiceProvider extends JbootServiceBase<FormInfo> implements FormInfoService {

    @Override
    public Page<FormInfo> _paginateByStatus(int page, int pagesize, String name, String status) {
        Columns columns = Columns.create("status", status);
        if (StrUtil.isNotBlank(name)) {
            columns.like("name", "%" + name + "%");
        }
        return paginateByColumns(page,pagesize,columns);
    }

    @Override
    public Page<FormInfo> _paginateWithoutStatus(int page, int pagesize, String name) {
        Columns columns = Columns.create();
        if (StrUtil.isNotBlank(name)) {
            columns.like("name", "%" + name + "%");
        }
        return paginateByColumns(page,pagesize,columns);
    }

}