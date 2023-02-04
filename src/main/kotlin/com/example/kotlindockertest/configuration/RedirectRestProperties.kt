package com.example.kotlindockertest.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

// Todo ������� � �� �����
@ConfigurationProperties(prefix = "rest.redirect")
class RedirectRestProperties {
    lateinit var url: String
}
