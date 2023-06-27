package com.example.studyplanner.model

import com.google.gson.JsonArray

class MyJsonArray() {
    companion object {
        private lateinit var array: JsonArray
        private var count: Int = 0

        fun set(d: JsonArray, c: Int) {
            array = d
            count = c
        }
        fun getBody(): JsonArray {
            return array
        }

        fun getSize(): Int {
            return count
        }
    }
}