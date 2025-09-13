package com.mshetty.stockpilot.utils

enum class ErrorType(val messageResId: Int) {
    NO_INTERNET(com.mshetty.stockpilot.R.string.error_no_internet),
    TIMEOUT(com.mshetty.stockpilot.R.string.error_timeout),
    SERVER_ERROR(com.mshetty.stockpilot.R.string.error_server),
    GENERIC(com.mshetty.stockpilot.R.string.error_generic)
}
