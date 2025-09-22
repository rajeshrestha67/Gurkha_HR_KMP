package com.gurkha.hr.res

import gurkhahr.core.ui.res.generated.resources.Poppins_Bold
import gurkhahr.core.ui.res.generated.resources.Poppins_Italic
import gurkhahr.core.ui.res.generated.resources.Poppins_Medium
import gurkhahr.core.ui.res.generated.resources.Res
import gurkhahr.core.ui.res.generated.resources.account
import gurkhahr.core.ui.res.generated.resources.allocated_leave
import gurkhahr.core.ui.res.generated.resources.are_you_sure
import gurkhahr.core.ui.res.generated.resources.change_password
import gurkhahr.core.ui.res.generated.resources.company_assets
import gurkhahr.core.ui.res.generated.resources.confirm
import gurkhahr.core.ui.res.generated.resources.confirm_password
import gurkhahr.core.ui.res.generated.resources.current_password
import gurkhahr.core.ui.res.generated.resources.dark_mode
import gurkhahr.core.ui.res.generated.resources.do_you_really_want_to_logout
import gurkhahr.core.ui.res.generated.resources.document
import gurkhahr.core.ui.res.generated.resources.email
import gurkhahr.core.ui.res.generated.resources.emergency_contact
import gurkhahr.core.ui.res.generated.resources.enter_your_email
import gurkhahr.core.ui.res.generated.resources.enter_your_password
import gurkhahr.core.ui.res.generated.resources.enter_your_username
import gurkhahr.core.ui.res.generated.resources.faq
import gurkhahr.core.ui.res.generated.resources.general
import gurkhahr.core.ui.res.generated.resources.getStarted
import gurkhahr.core.ui.res.generated.resources.history
import gurkhahr.core.ui.res.generated.resources.home
import gurkhahr.core.ui.res.generated.resources.invalid_email_address
import gurkhahr.core.ui.res.generated.resources.invalid_password_digit
import gurkhahr.core.ui.res.generated.resources.invalid_password_length
import gurkhahr.core.ui.res.generated.resources.invalid_password_lowercase
import gurkhahr.core.ui.res.generated.resources.invalid_password_special_char
import gurkhahr.core.ui.res.generated.resources.invalid_password_uppercase
import gurkhahr.core.ui.res.generated.resources.language
import gurkhahr.core.ui.res.generated.resources.leave
import gurkhahr.core.ui.res.generated.resources.light_mode
import gurkhahr.core.ui.res.generated.resources.log_out
import gurkhahr.core.ui.res.generated.resources.login
import gurkhahr.core.ui.res.generated.resources.my_attendance
import gurkhahr.core.ui.res.generated.resources.new_password
import gurkhahr.core.ui.res.generated.resources.next
import gurkhahr.core.ui.res.generated.resources.no
import gurkhahr.core.ui.res.generated.resources.password
import gurkhahr.core.ui.res.generated.resources.password_does_not_match
import gurkhahr.core.ui.res.generated.resources.personal_info
import gurkhahr.core.ui.res.generated.resources.profile
import gurkhahr.core.ui.res.generated.resources.privacy_policy
import gurkhahr.core.ui.res.generated.resources.report
import gurkhahr.core.ui.res.generated.resources.required
import gurkhahr.core.ui.res.generated.resources.setting
import gurkhahr.core.ui.res.generated.resources.skill_and_qualification
import gurkhahr.core.ui.res.generated.resources.skip
import gurkhahr.core.ui.res.generated.resources.support
import gurkhahr.core.ui.res.generated.resources.system_default
import gurkhahr.core.ui.res.generated.resources.terms_and_services
import gurkhahr.core.ui.res.generated.resources.theme
import gurkhahr.core.ui.res.generated.resources.time_and_attendance
import gurkhahr.core.ui.res.generated.resources.unknown_error_occurred
import gurkhahr.core.ui.res.generated.resources.username
import gurkhahr.core.ui.res.generated.resources.welcome
import gurkhahr.core.ui.res.generated.resources.yes


object SharedRes {

    fun getRes(path: String): String {
        return Res.getUri(path)
    }

    object Fonts {
        val poppinsMedium = Res.font.Poppins_Medium
        val poppinsBold = Res.font.Poppins_Bold
        val poppinsItalic = Res.font.Poppins_Italic
    }

    object Strings {
        val welcome = Res.string.welcome
        val required = Res.string.required
        val next = Res.string.next
        val skip = Res.string.skip
        val getStarted = Res.string.getStarted
        val home = Res.string.home
        val my_attendance = Res.string.my_attendance
        val profile = Res.string.profile
        val leave = Res.string.leave
        val report = Res.string.report
        val allocated_leave= Res.string.allocated_leave
        val time_and_attendance = Res.string.time_and_attendance
        val document = Res.string.document
        val company_assets = Res.string.company_assets
        val history = Res.string.history
        val general = Res.string.general
        val account = Res.string.account
        val terms_and_services = Res.string.terms_and_services
        val faq = Res.string.faq
        val privacy_policy = Res.string.privacy_policy
        val support = Res.string.support
        val setting = Res.string.setting
        val log_out = Res.string.log_out
        val no = Res.string.no
        val yes = Res.string.yes
        val are_you_sure = Res.string.are_you_sure
        val do_you_really_want_to_logout = Res.string.do_you_really_want_to_logout
        val change_password = Res.string.change_password
        val confirm_password = Res.string.confirm_password
        val new_password = Res.string.new_password
        val current_password = Res.string.current_password
        val light_mode = Res.string.light_mode
        val dark_mode = Res.string.dark_mode
        val theme = Res.string.theme
        val system_default = Res.string.system_default
        val confirm = Res.string.confirm
        val language = Res.string.language
        val personal_info = Res.string.personal_info
        val emergency_contact = Res.string.emergency_contact
        val skill_and_qualification = Res.string.skill_and_qualification







        val invalidEmailAddress = Res.string.invalid_email_address
        val email = Res.string.email
        val enterYourEmail = Res.string.enter_your_email
        val login = Res.string.login
        val username = Res.string.username
        val enterYourUsername = Res.string.enter_your_username
        val password = Res.string.password
        val enterYourPassword = Res.string.enter_your_password
        val invalidPasswordLength = Res.string.invalid_password_length
        val invalidPasswordUppercase = Res.string.invalid_password_uppercase
        val invalidPasswordLowercase = Res.string.invalid_password_lowercase
        val invalidPasswordDigit = Res.string.invalid_password_digit
        val invalidPasswordSpecialChar = Res.string.invalid_password_special_char
        val password_does_not_match = Res.string.password_does_not_match
        val unknown_error_occurred = Res.string.unknown_error_occurred
    }
}