package com.example.kotlindockertest.service.document

import graphql.language.Document
import org.springframework.stereotype.Service

private const val regexPattern = ", arguments=\\[.*?}]"

@Service
class DocumentComparator {

    private val regex = Regex(regexPattern)

    fun areEqual(document1: Document, document2: Document): Boolean {
        val doc1Str = document1.toString().replace(regex, "")
        val doc2Str = document2.toString().replace(regex, "")

        println("Str1 = $doc1Str")
        println("Str2 = $doc2Str")

        return doc1Str == doc2Str
    }
}
