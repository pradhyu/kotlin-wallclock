package com.example.utils.services

/**
 * Created by pkshrestha on 4/4/18.
 */
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser
import java.io.File


private const val PRODUCTS_JSON_PATH ="/Users/pkshrestha/AndroidStudioProjects/wallclock/app/product.json"

fun main(args: Array<String>) {
    println("testing")
}



object mockData{

    fun productsList () = {
        val jsonContent = File(PRODUCTS_JSON_PATH).readText()
        //println(jsonContent)
        val jsonObjArray = Klaxon().parseArray<HashMap<String, Any>>(jsonContent)
    }


}