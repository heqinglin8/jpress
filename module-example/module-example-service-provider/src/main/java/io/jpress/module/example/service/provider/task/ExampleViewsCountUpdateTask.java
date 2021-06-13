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
package io.jpress.module.example.service.provider.task;

import com.jfinal.aop.Aop;
import com.jfinal.plugin.activerecord.Db;
import io.jboot.components.schedule.annotation.FixedRate;
import io.jpress.module.example.service.ExampleService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Michael Yang 杨福海 （fuhai999@gmail.com）
 * @version V1.0
 * @Title: 用于更新商品的 访问 数量
 * @Package io.jpress.module.example.task
 */
@FixedRate(period = 60, initialDelay = 60)
public class ExampleViewsCountUpdateTask implements Runnable {

    private static Map<Long, AtomicLong> countsMap = new ConcurrentHashMap<>();

    public static void recordCount(Long id) {
        AtomicLong count = countsMap.get(id);
        if (count == null) {
            count = new AtomicLong(0);
            countsMap.put(id, count);
        }
        count.getAndIncrement();
    }


    @Override
    public void run() {
        if (countsMap.isEmpty()) {
            return;
        }

        Map<Long, AtomicLong> articleViews = new HashMap<>(countsMap);
        countsMap.clear();

        for (Map.Entry<Long, AtomicLong> entry : articleViews.entrySet()) {
            Db.update("update example set real_view_count = real_view_count + "
                    + entry.getValue().get()
                    + " where id = ? ", entry.getKey());
            Db.update("update example set view_count = view_count + "
                    + entry.getValue().get()
                    + " where id = ? ", entry.getKey());
            Aop.get(ExampleService.class).removeCacheById(entry.getKey());
        }
    }
}
