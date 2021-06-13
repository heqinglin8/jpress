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

import io.jboot.utils.StrUtil;
import io.jpress.core.support.smartfield.SmartField;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Michael Yang 杨福海 （fuhai999@gmail.com）
 * @version V1.0
 * @Title: 商品字段
 */
public class ExampleFields {

    private List<SmartField> fields = new ArrayList<>();
    private static ExampleFields me = new ExampleFields();

    private ExampleFields() {
        initDefaultFields();
    }

    private void initDefaultFields() {

        fields.add(new SmartField("usp",
                "案例亮点",
                "example.usp",
                "请输入",
                SmartField.TYPE_TEXTAREA,
                null,
                null,
                "此卖点内容会显示在案例详情的下方",
                10).addAttr("rows", 4));

        fields.add(new SmartField("specs",
                "规格",
                "example.specs",
                "请输入",
                SmartField.TYPE_TEXTAREA,
                null,
                null,
                "多个规格用英文逗号（,）隔开",
                11).addAttr("rows", 4));

        fields.add(new SmartField("summary",
                "摘要",
                "example.summary",
                "请输入",
                SmartField.TYPE_TEXTAREA,
                null,
                null,
                null,
                12).addAttr("rows", 4));


        fields.add(new SmartField("meta_keywords",
                "SEO关键字",
                "example.meta_keywords",
                "请输入",
                SmartField.TYPE_TEXTAREA,
                null,
                null,
                null,
                20).addAttr("rows", 3));

        fields.add(new SmartField("meta_description",
                "SEO描述",
                "example.meta_description",
                "请输入",
                SmartField.TYPE_TEXTAREA,
                null,
                null,
                null,
                21).addAttr("rows", 4));

        fields.add(new SmartField("view_count",
                "访问量",
                "example.view_count",
                "请输入",
                SmartField.TYPE_INPUT,
                null,
                null,
                "",
                30));

        fields.add(new SmartField("video",
                "视频链接",
                "example.video",
                "请输入",
                SmartField.TYPE_INPUT,
                null,
                null,
                "",
                60));

        fields.add(new SmartField("video_cover",
                "视频封面链接",
                "example.video_cover",
                "请输入",
                SmartField.TYPE_INPUT,
                null,
                null,
                "",
                61));

        fields.add(new SmartField("comment_status",
                "允许评论",
                "example.comment_status",
                "请输入",
                SmartField.TYPE_SWITCH,
                "true",
                null,
                null,
                70));

        fields.sort(Comparator.comparingInt(SmartField::getOrderNo));

    }

    public static ExampleFields me() {
        return me;
    }

    public void addField(SmartField field) {
        removeField(field.getId()); //防止添加重复的Field
        fields.add(field);
        fields.sort(Comparator.comparingInt(SmartField::getOrderNo));
    }

    public void removeField(String id) {
        if (StrUtil.isBlank(id)) {
            return;
        }
        fields.removeIf(field -> id.equals(field.getId()));
        fields.sort(Comparator.comparingInt(SmartField::getOrderNo));
    }

    public List<SmartField> getFields() {
        return fields;
    }

    public String render() {
        StringBuilder s = new StringBuilder();
        for (SmartField field : fields) {
            String html = field.render();
            if (html != null) {
                s.append(html);
            }
        }
        return s.toString();
    }

}
