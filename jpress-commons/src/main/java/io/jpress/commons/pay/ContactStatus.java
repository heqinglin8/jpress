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
package io.jpress.commons.pay;


import org.apache.commons.lang3.StringUtils;

/**
 * @author michael
 */

public enum ContactStatus {


    /**
     * 未处理
     */
    UNDEAL(1, "未处理"),

    /**
     * 已经联系
     */
    CONTACTED(2, "已经联系"),


    /**
     * 认为无用的咨询，废弃
     */
    ABANDONED(3, "废弃");

    /**
     * 状态
     */
    private int status;

    /**
     * 文本内容
     */
    private String text;

    ContactStatus(int status, String text) {
        this.status = status;
        this.text = text;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static ContactStatus getByStatus(int status) {
        for (ContactStatus payStatus : values()) {
            if (payStatus.status == status) {
                return payStatus;
            }
        }
        return null;
    }

    public static ContactStatus getStatusByType(int payType) {
        switch (payType) {
            case 1:
                return UNDEAL;
            case 2:
                return CONTACTED;
            case 3:
                return ABANDONED;
            default:
                return null;
        }
    }


    public static String getTextByInt(Integer status) {
        if (status == null) {
            return StringUtils.EMPTY;
        }
        ContactStatus payStatus = getByStatus(status);
        return payStatus != null ? payStatus.text : StringUtils.EMPTY;
    }


}
