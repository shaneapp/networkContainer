
fun main(args: Array<String>) {
    println("hello world")
}

interface IHttpClient
interface Network {
    fun protocolAllowed(endpoint: String): Boolean
}