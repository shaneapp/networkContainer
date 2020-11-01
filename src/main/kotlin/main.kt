import sun.security.ssl.Debug

fun main(args: Array<String>) {
    println("hello world")
}

class NetworkContainer(private val httpClient: IHttpClient) : Network {
    override fun requestSync(endpoint: String, httpMethod: HttpMethod, responseDecoder: IResponseDecoder<DebugResponse>): DebugResponse {
        TODO("Not yet implemented")
    }

    override fun protocolAllowed(endpoint: String): Boolean {
        return httpClient.validateDomain(endpoint)
    }
}

class UnsecureHttpClient : IHttpClient {
    override fun performFetch(): ByteArray {
        TODO("Not yet implemented")
    }

    override fun validateDomain(endpoint: String): Boolean = endpoint.startsWith("http")
}

class SecureHttpClient : IHttpClient {
    override fun performFetch(): ByteArray {
        TODO("Not yet implemented")
    }

    override fun validateDomain(endpoint: String) = endpoint.startsWith("https")
}

interface IHttpClient {
    fun performFetch(): ByteArray
    fun validateDomain(endpoint: String): Boolean
}

interface Network {
    fun protocolAllowed(endpoint: String): Boolean
    fun requestSync(endpoint: String, httpMethod: HttpMethod, responseDecoder: IResponseDecoder<DebugResponse>): DebugResponse
}

enum class HttpMethod { GET, POST, PUT, DELETE }

interface IResponseDecoder<T: IDecoderResult> {
    fun deserialize(rawResult: ByteArray): T
}

interface IDecoderResult
data class DebugResponse(val debugOutput: String) : IDecoderResult