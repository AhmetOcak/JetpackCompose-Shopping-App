package com.ahmetocak.shoppingapp.common.helpers

object AuthFieldCheckers {

    fun checkEmailField(
        email: String,
        onBlank: () -> Unit,
        onUnValid: () -> Unit,
        onCheckSuccess: () -> Unit
    ): Boolean {
        return if (email.isBlank()) {
            onBlank()
            false
        } else if (!ValidEmail.verifyEmailType(email)) {
            onUnValid()
            false
        } else {
            onCheckSuccess()
            true
        }
    }

    fun checkPassword(
        password: String,
        onBlank: () -> Unit,
        onUnValid: () -> Unit,
        onCheckSuccess: () -> Unit
    ): Boolean {
        return if (password.isBlank()) {
            onBlank()
            false
        } else if (password.length < 6) {
            onUnValid()
            false
        } else {
            onCheckSuccess()
            true
        }
    }
}