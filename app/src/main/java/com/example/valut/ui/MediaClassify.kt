package com.example.valut.ui

class MediaClassify {
    var key: String? = null
    var title: String? = null
    var number = 0
    var totalKB: Long = 0
    var image: String? = null

    constructor() {}
    constructor(key: String?, title: String?, number: Int, totalKB: Long, image: String?) {
        this.key = key
        this.title = title
        this.number = number
        this.totalKB = totalKB
        this.image = image
    }

    override fun toString(): String {
        return "MediaClassify{" +
                "key='" + key + '\'' +
                ", title='" + title + '\'' +
                ", number=" + number +
                ", totalKB=" + totalKB +
                ", image='" + image + '\'' +
                '}'
    }
}