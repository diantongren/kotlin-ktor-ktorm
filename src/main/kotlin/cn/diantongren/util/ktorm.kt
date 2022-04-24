package cn.diantongren.util

import org.ktorm.entity.EntitySequence
import org.ktorm.entity.filter
import org.ktorm.schema.BaseTable
import org.ktorm.schema.ColumnDeclaring

inline fun <E : Any, T : BaseTable<E>> EntitySequence<E, T>.filterIf(
    condition: Boolean,
    predicate: (T) -> ColumnDeclaring<Boolean>
): EntitySequence<E, T> {
    return if (condition) {
        this.filter(predicate)
    } else {
        this
    }
}