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
package io.jpress.module.example.service.provider.search;

import com.jfinal.plugin.activerecord.Page;
import io.jpress.module.example.model.Example;
import io.jpress.module.example.service.search.ExampleSearcher;

public class NoneSearcher implements ExampleSearcher {


    @Override
    public void addExample(Example example) {

    }

    @Override
    public void deleteExample(Object id) {

    }

    @Override
    public void updateExample(Example example) {

    }

    @Override
    public Page<Example> search(String keyword, int pageNum, int pageSize) {
        return null;
    }
}
