package com.ramaqq.storyapp_submission1

import com.ramaqq.storyapp_submission1.data.response.ListStoryItem

object DataDummy {
    fun generateDummyResponse(): List<ListStoryItem>{
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..10){
            val story = ListStoryItem(
                i.toString(),
                "https://www.google-$i.com",
                "2023-2-21T22:22:22Z",
                "name-$i",
                "desc-$i",
                i.toDouble(),
                i.toDouble()
            )
            items.add(story)
        }
        return items
    }
}