package io.jpress.module.example.service.provider;

import io.jboot.Jboot;
import io.jboot.aop.annotation.Bean;
import io.jboot.components.cache.annotation.Cacheable;
import io.jboot.db.model.Column;
import io.jboot.db.model.Columns;
import io.jpress.module.example.model.Example;
import io.jpress.module.example.model.ExampleCategory;
import io.jpress.module.example.model.base.BaseExampleImage;
import io.jpress.module.example.service.ExampleImageService;
import io.jpress.module.example.model.ExampleImage;
import io.jboot.service.JbootServiceBase;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Comparator;
import java.util.List;

@Bean
public class ExampleImageServiceProvider extends JbootServiceBase<ExampleImage> implements ExampleImageService {

    private static final String cacheName = "exampleimage";



    @Override
    @Cacheable(name = cacheName, key = "exampleId:#(exampleId)", nullCacheEnable = true)
    public List<ExampleImage> findListByProductId(Long exampleId) {
        List<ExampleImage> list = DAO.findListByColumn(Column.create("example_id", exampleId));
        if (list != null && !list.isEmpty()) {
            list.sort(Comparator.comparingInt(BaseExampleImage::getOrderNumber));
        }
        return list == null || list.isEmpty() ? null : list;
    }

    @Override
    public void saveOrUpdateByProductId(Long exampleId, String[] imageIds, String[] imageSrcs) {

        if (imageIds == null || imageSrcs == null || imageIds.length == 0) {
            Jboot.getCache().remove(cacheName, "exampleId:" + exampleId);
            deleteByProductId(exampleId);
            return;
        }

        //这种情况应该不可能出现
        if (imageIds.length != imageSrcs.length) {
            return;
        }



        List<ExampleImage> exampleImages = findListByProductId(exampleId);
        if (exampleImages != null) {
            for (ExampleImage image : exampleImages) {
                if (!ArrayUtils.contains(imageIds, image.getId().toString())) {
                    DAO.deleteById(image.getId());
                }
            }
        }

        Jboot.getCache().remove(cacheName, "exampleId:" + exampleId);

        for (int i = 0; i < imageIds.length; i++) {
            Long imageId = Long.parseLong(imageIds[i]);

            ExampleImage image = new ExampleImage();
            image.setOrderNumber(i);
            image.setSrc(imageSrcs[i]);
            image.setExampleId(exampleId);

            if (imageId > 0 ){
                image.setId(imageId);
            }

            saveOrUpdate(image);
        }
    }

    @Override
    public boolean deleteByProductId(Long exampleId) {
        return  DAO.deleteByColumn(Column.create("example_id",exampleId));
    }

}