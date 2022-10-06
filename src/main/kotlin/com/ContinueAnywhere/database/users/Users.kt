package com.ContinueAnywhere.database.users

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Users : IntIdTable() {
    val password = Users.varchar("password", 25)
    val email = Users.varchar("email", 50)

    fun toUser(row: ResultRow): UserDTO =
        UserDTO(
            email = row[email],
            password = row[password]
        )
}

class UserRow(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<UserRow>(Users)
    var email by Users.email
    var password by Users.password

    fun toUserDTO() = UserDTO(id.value, password, email)
}