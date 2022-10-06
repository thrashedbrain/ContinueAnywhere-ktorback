package com.ContinueAnywhere.database.users

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class UserDao {

    suspend fun insertUser(user: UserDTO) = newSuspendedTransaction {
        val id = Users.insertAndGetId {
            it[email] = user.email ?: ""
            it[password] = user.password
        }
        return@newSuspendedTransaction UserRow.findById(id)?.toUserDTO()
    }

    suspend fun getUserId(email: String) = newSuspendedTransaction {
        val user = UserRow.find { Users.email eq email }.first()
        return@newSuspendedTransaction user.id.value

    }

    suspend fun findUser(login: String) = newSuspendedTransaction {
        val user = UserRow.find { Users.email eq login }
        return@newSuspendedTransaction if (user.empty()) null
        else user.first().toUserDTO()
    }
}