package com.example.utils.services
import android.util.Log
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.getOrElse

/**
 * Created by pkshrestha on 2/19/18.
 */

/*
Using open weather map API


https://openweathermap.org/current
By city ID
Description:
You can call by city ID. API responds with exact result.

List of city ID city.list.json.gz can be downloaded here http://bulk.openweathermap.org/sample/

We recommend to call API by city ID to get unambiguous result for your city.

Parameters:
id City ID
Examples of API calls:
http://openweathermap.org/data/2.5/weather?id=2172797
  {
    "id": 4459467,
    "name": "Cary",
    "country": "US",
    "coord": {
      "lon": -78.78112,
      "lat": 35.791538
    }

 */
object RestCalls {

    fun getCaryWeather(): String{
        return openWeatherApi.getWeatherInFah("cary,us")
    }
}
object openWeatherApi {
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
        println(jsonResult)

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
        Log.i("weather", "data: " + data.toString())
        val jsonObj = parser.parse(StringBuilder(data.toString())) as JsonObject

        val tempValue = (jsonObj.get("main") as JsonObject).get("temp").toString()
        // print all keys
        // jsonObj.mapKeys { println(it) }
        return tempValue
    }

}