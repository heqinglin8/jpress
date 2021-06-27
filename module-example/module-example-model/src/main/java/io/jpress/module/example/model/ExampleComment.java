package io.jpress.module.example.model;

import io.jboot.db.annotation.Table;
import io.jpress.module.example.model.base.BaseExampleComment;

import java.util.HashMap;
import java.util.Map;

/**
 * Generated by JPress.
 */
@Table(tableName = "example_comment", primaryKey = "id")
public class ExampleComment extends BaseExampleComment<ExampleComment> {

    private static final long serialVersionUID = 1L;

    public static final int STATUS_NORMAL = 1; //正常
    public static final int STATUS_UNAUDITED = 2; //待审核
    public static final int STATUS_TRASH = 9; //垃圾箱

    public static final Map<Integer, String> statusStrMap = new HashMap<>();

    static {
        statusStrMap.put(STATUS_NORMAL, "正常");
        statusStrMap.put(STATUS_UNAUDITED, "待审核");
        statusStrMap.put(STATUS_TRASH, "垃圾箱");
    }

    public boolean isNormal() {
        return STATUS_NORMAL == getStatus();
    }

    public boolean isUnaudited() {
        return STATUS_UNAUDITED == getStatus();
    }

    public boolean isTrash() {
        return STATUS_TRASH == getStatus();
    }
	
}