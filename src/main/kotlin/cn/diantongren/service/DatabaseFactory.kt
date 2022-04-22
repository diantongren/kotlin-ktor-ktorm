package cn.diantongren.service

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.ktorm.database.Database

class DatabaseFactory {
    val database = Database.connect(hikari())

    private fun hikari() : HikariDataSource {
        val config = HikariConfig().apply {
            driverClassName = "com.mysql.cj.jdbc.Driver"
            jdbcUrl = "jdbc:mysql://rm-bp19dz57310ul43604o.mysql.rds.aliyuncs.com:3306/real_world"
            username = "diantongren"
            password = "Yanyuxi19990110"
        }
        return HikariDataSource(config)
    }
}