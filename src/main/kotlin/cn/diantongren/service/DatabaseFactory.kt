package cn.diantongren.service

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.ktorm.database.Database

class DatabaseFactory {
    val database = Database.connect(hikari())

    private fun hikari() : HikariDataSource {
        val config = HikariConfig().apply {
            driverClassName = "com.mysql.cj.jdbc.Driver"
            jdbcUrl = "jdbc:mysql://localhost:3306/ktorm-example?serverTimezone=UTC&useSSL=false"
            username = "root"
            password = "123456"
        }
        return HikariDataSource(config)
    }
}