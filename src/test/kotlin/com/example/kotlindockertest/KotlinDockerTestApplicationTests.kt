package com.example.kotlindockertest

import com.example.kotlindockertest.controller.QueryWrapper
import com.example.kotlindockertest.model.trigger.TriggerDto
import com.example.kotlindockertest.repository.TriggerRepository
import com.example.kotlindockertest.service.TriggerPathTokenizer
import com.example.kotlindockertest.service.FieldSearcher
import com.example.kotlindockertest.service.TriggerDocumentMatcher
import com.example.kotlindockertest.service.trigger.matcher.TriggerFloatMatcher
import com.example.kotlindockertest.service.trigger.matcher.TriggerMatcher
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.ObjectNode
import graphql.GraphQL
import graphql.language.*
import graphql.parser.Parser
import graphql.schema.GraphQLSchema
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeDefinitionRegistry
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.boot.test.context.SpringBootTest
import kotlin.reflect.jvm.jvmName

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
class KotlinDockerTestApplicationTests {

    @Autowired lateinit var fieldSearcher: FieldSearcher

    @Autowired lateinit var triggerDocumentMatcher: TriggerDocumentMatcher

    @Autowired lateinit var listableBeanFactory: ConfigurableListableBeanFactory
    @Autowired lateinit var triggerFloatMatcher: TriggerMatcher
    @Autowired lateinit var triggerRepository: TriggerRepository


    private fun getStr(): String {
        val str: String = """
            type Post {
                id: ID!
                title: String!
                text: String!
                category: String
                author: Author!
            }

            type Author {
                id: ID!
                name: String!
                thumbnail: String
                posts: [Post]!
            }

            # The Root Query for the application
            type Query {
                recentPosts(count: Int, offset: Int): [Post]!
            }

            # The Root Mutation for the application
            type Mutation {
                createPost(title: String!, text: String!, category: String, authorId: String!) : Post!
            }
        """.trimIndent()

        return """
            type Book {
                id: ID!
                name: String!
            }
            type Query {
                books: [Book!]!
            }
        """.trimIndent()
    }
    @Test
    fun schema() {
        val parser = SchemaParser()
        val str = getStr()
        val parsedScheme: TypeDefinitionRegistry = parser.parse(str)
        val schema: GraphQLSchema = SchemaGenerator().makeExecutableSchema(parsedScheme, RuntimeWiring.MOCKED_WIRING)
        val graphQL: GraphQL = GraphQL.newGraphQL(schema).build()

        val query = "query{users{name}}"
        val result = graphQL.execute(query)
        println(result)
}
    @Test
    fun test() {
        val beanProxy = listableBeanFactory.getBean(TriggerFloatMatcher::class.java)
        println(beanProxy::class.jvmName)
        val a = 3
    }


    data class Address(val street: Long)
    data class Person(val name: String, val address: Address)
    @Test
    fun contextLoads() {
        val mapper = ObjectMapper()
        val person = Person("Dima", Address(222))
        val jsonNode = mapper.readTree(mapper.writeValueAsString(person))

        val addressNode = jsonNode.get("address")

        (addressNode as ObjectNode).set<IntNode>("street", IntNode(43))

        println(addressNode.toPrettyString())
        println(jsonNode.toPrettyString())
    }

    @Autowired
    lateinit var tokenizer: TriggerPathTokenizer

    @Test
    fun testTokenizer() {
        val path = "['123'][0]['hello'][0]"
        val result = tokenizer.tokenize(path)
        result.forEach {
            println(it)
        }
    }

    @Test
    fun test23() {
        val query = QueryWrapper("321")
        val writeValueAsString = ObjectMapper().writeValueAsString(query)
        println(writeValueAsString)
    }


    private fun JsonNode.getGraphqlQuery() =
        this.get("query")
            .toString()
            .replace("\\r\\n", "")
            .trim('"')

    private fun Document.findField(name: String): Field? {
        definitions.forEach { definition ->
            val operationDefinition = definition as OperationDefinition

            return operationDefinition.selectionSet.findField(name) ?: return@forEach
        }

        return null
    }

    private fun SelectionSet.findField(fieldName: String): Field? {
        selections?.forEach { selection ->
            if ((selection as Field).name == fieldName) {
                return selection
            }

            selection.findFieldAmongChildren(fieldName)?.let { return it }
        }

        return null
    }


    private fun Selection<*>.findFieldAmongChildren(fieldName: String): Field? {
        children?.forEach { child ->
            val childSelection = child as SelectionSet

            return childSelection.findField(fieldName) ?: return@forEach
        }

        return null
    }
}
