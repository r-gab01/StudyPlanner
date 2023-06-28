package com.example.studyplanner.model

import com.google.gson.JsonArray

class MyJsonArray() {
    companion object {
        private var array: JsonArray? = null
        private var count: Int = 0

        fun set(d: JsonArray?) {
            array = d
        }
        fun getBody(): JsonArray? {
            return array
        }

        fun getSize(): Int {
            return array?.size() ?: -1
        }
    }
}