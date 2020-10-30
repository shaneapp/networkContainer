import kotlin.random.Random

fun main(args: Array<String>) {
    println("hello world")
}

class NetworkContainer(private val httpClient: IHttpClient) : Network {
    override fun protocolAllowed(endpoint: String): Boolean {
        return httpClient.validateDomain(endpoint)
    }
}

class UnsecureHttpClient : IHttpClient {
    override fun validateDomain(endpoint: String): Boolean = endpoint.startsWith("http")
}

class SecureHttpClient : IHttpClient {
    override fun validateDomain(endpoint: String) = endpoint.startsWith("https")
}

interface IHttpClient {
    fun validateDomain(endpoint: String): Boolean
}
interface Network {
    fun protocolAllowed(endpoint: String): Boolean
}