package fr.sdv.b3dev.gameapp.presentation

import fr.sdv.b3dev.gameapp.datasource.local.UserDao
import fr.sdv.b3dev.gameapp.domain.User

class UserRepository(
    private val userDao: UserDao
) {

    suspend fun register(username: String, email: String, password: String) {

        val existingUser = userDao.getUserByEmail(email)
        if (existingUser != null) {
            throw Exception("User already exists")
        }

        val user = User(
            username = username,
            email = email,
            password = password
        )

        userDao.insert(user)
    }

    suspend fun login(email: String, password: String) {

        val user = userDao.login(email, password)
            ?: throw Exception("Invalid credentials")
    }
}