package fr.sdv.b3dev.gameapp.presentation

import fr.sdv.b3dev.gameapp.domain.User
class AuthRepository {

    private val users = mutableListOf<User>()
    private var nextId = 1L

    suspend fun register(user: User) {
        if (users.any { it.email == user.email }) {
            throw Exception("User already exists")
        }

        val newUser = user.copy(id = nextId++)
        users.add(newUser)
    }

    suspend fun login(email: String, password: String) {
        val user = users.find { it.email == email && it.password == password }
        if (user == null) {
            throw Exception("Invalid credentials")
        }
    }
}
