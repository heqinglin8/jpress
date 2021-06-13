package io.jpress.module.example.model;

import io.jboot.db.annotation.Table;
import io.jboot.utils.StrUtil;
import io.jpress.commons.url.JPressUrlUtil;
import io.jpress.commons.utils.JsoupUtils;
import io.jpress.model.UserFavorite;
import io.jpress.module.example.model.base.BaseExample;

import java.util.Date;

/**
 * Generated by JPress.
 */
@Table(tableName = "example", primaryKey = "id")
public class Example extends BaseExample<Example> {

    private static final long serialVersionUID = 1L;

    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_DRAFT = 2;
    public static final int STATUS_TRASH = 3;

    public boolean isNormal() {
        return getStatus() != null && getStatus() == STATUS_NORMAL;
    }

    public boolean isDraft() {
        return getStatus() != null && getStatus() == STATUS_DRAFT;
    }

    public boolean isTrash() {
        return getStatus() != null && getStatus() == STATUS_TRASH;
    }

    public String getUrl() {
        return JPressUrlUtil.getUrl("/example/", StrUtil.isNotBlank(getSlug()) ? getSlug() : getId());
    }

    public String getHtmlView() {
        return StrUtil.isBlank(getStyle()) ? "example.html" : "example_" + getStyle().trim() + ".html";
    }

    public String getText() {
        return JsoupUtils.getText(getContent());
    }

    public String getHighlightContent() {
        return getStr("highlightContent");
    }

    public void setHighlightContent(String highlightContent) {
        put("highlightContent", highlightContent);
    }

    public String getHighlightTitle() {
        return getStr("highlightTitle");
    }

    public void setHighlightTitle(String highlightTitle) {
        put("highlightTitle", highlightTitle);
    }

    public boolean isCommentEnable() {
        Boolean cs = getCommentStatus();
        return cs != null && cs == true;
    }

    public UserFavorite toFavorite(Long userId) {
        UserFavorite favorite = new UserFavorite();
        favorite.setUserId(userId);
        favorite.setType(UserFavorite.FAV_TYPE_PRODUCT);
        favorite.setTypeText(UserFavorite.FAV_TYPE_PRODUCT_TEXT);
        favorite.setTypeId(String.valueOf(getId()));
        favorite.setTitle(getTitle());
        favorite.setSummary(getSummary());
        favorite.setThumbnail(getShowImage());
        favorite.setLink(getUrl());
        favorite.setCreated(new Date());
        return favorite;
    }

    public String getShowImage() {
        String thumbnail = getThumbnail();
        return StrUtil.isNotBlank(thumbnail) ? thumbnail : getFirstImage();
    }

    public String getFirstImage() {
        return JsoupUtils.getFirstImageSrc(getContent());
    }
	
}
