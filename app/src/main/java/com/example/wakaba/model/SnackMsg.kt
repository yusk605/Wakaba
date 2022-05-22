package com.example.wakaba.model

enum class SnackMsg(val get:String) {
    DELETE_MSG("削除しました"),
    UPDATE_MSG("変更しました"),
    INSERT_MSG("追加しました"),
    COMPLETE_MSG("タスク完了"),
    RETURN_TASK("タスクを戻しました")
}