package com.example.studyplanner.model

import com.google.gson.JsonObject

class MyJsonObject() {
    companion object {
        private var array: JsonObject? = null
        private var count: Int = -1

        fun setBody(d: JsonObject?) {
            array = d
            setSize(array?.size()!!)
        }
        fun setSize(c: Int){
            if (array != null )
                count = c
        }
        fun getBody(): JsonObject? {
            return array
        }

        fun getSize(): Int {
            return count
        }
    }
}