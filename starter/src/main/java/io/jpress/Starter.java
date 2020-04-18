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
package io.jpress;

import io.jboot.app.JbootApplication;

/**
 * @author Michael Yang 杨福海 （fuhai999@gmail.com）
 * @version V1.0
 * @Package io.jpress
 */
public class Starter {

    private static final String unScanJar = "pay-java,ahocorasick,tinypinyin,opencc4j" +
            ",mapper-extras-,jieba-,heaven,apiguardian-,core-";

    public static void main(String[] args) {
        JbootApplication.setBootArg("undertow.resourcePath", "classpath:webapp");
        JbootApplication.setBootArg("jboot.app.scanner.unScanJarPrefix", unScanJar);
        JbootApplication.run(args);
    }

}
