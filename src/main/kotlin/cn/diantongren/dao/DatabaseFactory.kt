package cn.diantongren.dao

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.ktorm.database.Database

val database = Database.connect(HikariDataSource(HikariConfig().apply {
    driverClassName = "com.mysql.cj.jdbc.Driver"
    jdbcUrl = "jdbc:mysql://rm-bp19dz57310ul43604o.mysql.rds.aliyuncs.com:3306/real_world"
    username = "diantongren"
    password = "Yanyuxi19990110"
    maximumPoolSize = 3
}))

//class DatabaseFactory {
//    val database = Database.connect(hikari())
//
//    private fun hikari(): HikariDataSource {
//        val config = HikariConfig().apply {
//            driverClassName = "com.mysql.cj.jdbc.Driver"
//            jdbcUrl = "jdbc:mysql://rm-bp19dz57310ul43604o.mysql.rds.aliyuncs.com:3306/real_world"
//            username = "diantongren"
//            password = "Yanyuxi19990110"
//            maximumPoolSize = 3
//        }
//        return HikariDataSource(config)
//    }
//}