import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.getOrElse


fun main(args: Array<String>) {
    //testHttp()
    val tempValue = openWeatherApi.getWeatherInFah("cary,us")
    Log.i("Weather temperature", tempValue)


   // System.exit(0)

}


object Log {
    fun i(tag: String, msg: String?) {
        println("$tag : $msg")
    }

    fun e(tag: String, msg: String?) {
        println("$tag : $msg")

    }
}

object openWeatherApi {

    fun main(args: Array<String>) {
        //testHttp()
        val tempValue = openWeatherApi.getWeatherInFah("cary,us")
        Log.i("Weather temperature", tempValue)


        // System.exit(0)

    }
    fun testHttp() {
        FuelManager.instance.basePath = "http://httpbin.org"
        "/get".httpGet().responseString { request, response, result ->
            //make a GET to http://httpbin.org/get and do something with response
            val (data, error) = result
            if (error == null) {
                //do something when success
                //println(data)
            } else {
                //error handling
            }
        }
    }

    private fun withFahParam(params: List<Pair<String, Any?>>): List<Pair<String, Any?>> {
        val param = listOf("units" to "imperial")
        return (params + param)
    }

    fun getWeatherInFah(cityParam: String): String {
        val cityParam = listOf("q" to cityParam)
        return openWeatherMapApiCall(withFahParam(cityParam))
    }

    private fun openWeatherMapApiCall(params: List<Pair<String, Any?>>): String {
        Log.i("weather", "Get Weather")
        val apiUrl = "http://openweathermap.org/data/2.5/weather"
        val apiIdParam = ("appid" to "b6907d289e10d714a6e88b30761fae22")

        //blocking mode
        val (request, response, result) =  Fuel.get(apiUrl,(params + apiIdParam)).responseString()
        val jsonResult=result.getOrElse("Error")

        return getWeatherValue(jsonResult)
    }

    // parse json file as resource
    fun parse(name: String): Any? {
        val cls = Parser::class.java
        return cls.getResourceAsStream(name)?.let { inputStream ->
            return Parser().parse(inputStream)
        }
    }

    fun getWeatherValue(data: String?): String {
        val parser: Parser = Parser()
        val jsonObj = parser.parse(StringBuilder(data.toString())) as JsonObject

        val tempValue = (jsonObj.get("main") as JsonObject).get("temp").toString()
        // print all keys
        // jsonObj.mapKeys { println(it) }
        return tempValue
    }

}
